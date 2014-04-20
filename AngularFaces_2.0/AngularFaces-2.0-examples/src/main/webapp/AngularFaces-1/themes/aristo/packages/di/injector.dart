part of di;

class Injector {

  /**
   * Name of the injector or null of none is given.
   */
  final String name;

  static const List<Key> _PRIMITIVE_TYPES = const <Key>[
    const Key(num), const Key(int), const Key(double), const Key(String),
    const Key(bool)
  ];

  /**
   * Returns the parent injector or null if root.
   */
  final Injector parent;

  Injector _root;

  Map<Key, _Provider> _providers = <Key, _Provider>{};

  final Map<Key, Object> instances = <Key, Object>{};

  final List<Key> resolving = <Key>[];

  final bool allowImplicitInjection;

  Iterable<Type> _typesCache;

  /**
   * List of all types which the injector can return
   */
  Iterable<Type> get _types {
    if (_typesCache == null) {
      _typesCache = _providers.keys.map((k) => k.type);
    }
    return _typesCache;
  }

  Injector({List<Module> modules, String name,
           bool allowImplicitInjection: false})
      : this.fromParent(modules, null,
          name: name, allowImplicitInjection: allowImplicitInjection);

  Injector.fromParent(List<Module> modules,
      Injector this.parent, {this.name, this.allowImplicitInjection}) {
    _root = parent == null ? this : parent._root;
    if (modules != null) {
      modules.forEach((module) {
        _providers.addAll(module._bindings);
      });
    }
    _providers[new Key(Injector)] = new _ValueProvider(this);
  }

  Injector get root => _root;

  Set<Type> get types {
    var types = new Set.from(_types);
    var parent = this.parent;
    while (parent != null) {
      types.addAll(parent._types);
      parent = parent.parent;
    }
    return types;
  }

  String _error(message, [appendDependency]) {
    if (appendDependency != null) {
      resolving.add(appendDependency);
    }

    String graph = resolving.join(' -> ');

    resolving.clear();

    return '$message (resolving $graph)';
  }

  dynamic _getInstanceByKey(Key key, Injector requester) {
    _checkKeyConditions(key);

    if (resolving.contains(key)) {
      throw new CircularDependencyError(
          _error('Cannot resolve a circular dependency!', key));
    }

    var providerWithInjector = _getProviderWithInjectorForKey(key);
    var provider = providerWithInjector.provider;
    var injector = providerWithInjector.injector;
    var visible = provider.visibility != null ?
        provider.visibility(requester, injector) :
        _defaultVisibility(requester, injector);

    if (visible && instances.containsKey(key)) {
      return instances[key];
    }

    if (providerWithInjector.injector != this || !visible) {
      if (!visible) {
        if (injector.parent == null) {
          throw new NoProviderError(
              _error('No provider found for ${key}!', key));
        }
        injector =
            injector.parent._getProviderWithInjectorForKey(key).injector;
      }
      return injector._getInstanceByKey(key, requester);
    }

    var value;
    try {
      resolving.add(key);
      value = provider.get(this, requester, _getInstanceByKey, _error);
      resolving.removeLast();
    } catch(e) {
      resolving.clear();
      rethrow;
    }

    // cache the value.
    providerWithInjector.injector.instances[key] = value;
    return value;
  }

  /// Returns a pair for provider and the injector where it's defined.
  _ProviderWithDefiningInjector _getProviderWithInjectorForKey(Key key) {
    if (_providers.containsKey(key)) {
      return new _ProviderWithDefiningInjector(_providers[key], this);
    }

    if (parent != null) {
      return parent._getProviderWithInjectorForKey(key);
    }

    if (allowImplicitInjection) {
      return new _ProviderWithDefiningInjector(
          new _TypeProvider(key.type), this);
    }

    throw new NoProviderError(_error('No provider found for '
        '${key}!', key));
  }

  void _checkKeyConditions(Key key) {
    if (_PRIMITIVE_TYPES.contains(key)) {
      throw new NoProviderError(_error('Cannot inject a primitive type '
          'of ${key.type}!', key));
    }
  }


  // PUBLIC API

  /**
   * Get an instance for given token ([Type]).
   *
   * If the injector already has an instance for this token, it returns this
   * instance. Otherwise, injector resolves all its dependencies, instantiates
   * new instance and returns this instance.
   *
   * If there is no binding for given token, injector asks parent injector.
   *
   * If there is no parent injector, an implicit binding is used. That is,
   * the token ([Type]) is instantiated.
   */
  dynamic get(Type type, [Type annotation]) =>
      _getInstanceByKey(new Key(type, annotation), this);

  /**
   * Get an instance for given key ([Key]).
   *
   * If the injector already has an instance for this key, it returns this
   * instance. Otherwise, injector resolves all its dependencies, instantiates
   * new instance and returns this instance.
   *
   * If there is no binding for given key, injector asks parent injector.
   */
  dynamic getByKey(Key key) => _getInstanceByKey(key, this);

  /**
   * Create a child injector.
   *
   * Child injector can override any bindings by adding additional modules.
   *
   * It also accepts a list of tokens that a new instance should be forced.
   * That means, even if some parent injector already has an instance for this
   * token, there will be a new instance created in the child injector.
   */
  Injector createChild(List<Module> modules,
                       {List forceNewInstances, String name}) {
    if (forceNewInstances != null) {
      Module forceNew = new Module();
      forceNewInstances.forEach((key) {
        if (key is Type) {
          key = new Key(key);
        } else if (key is! Key) {
          throw 'forceNewInstances must be List<Key|Type>';
        }
        assert(key is Key);
        var providerWithInjector = _getProviderWithInjectorForKey(key);
        var provider = providerWithInjector.provider;
        forceNew._keyedFactory(key, (Injector inj) => provider.get(this,
            inj, inj._getInstanceByKey, inj._error),
            visibility: provider.visibility);
      });

      modules = modules.toList(); // clone
      modules.add(forceNew);
    }

    return newFromParent(modules, name);
  }

  newFromParent(List<Module> modules, String name) {
    throw new UnimplementedError('This method must be overriden.');
  }

  Object newInstanceOf(Type type, ObjectFactory factory, Injector requestor,
                       errorHandler(message, [appendDependency])) {
    throw new UnimplementedError('This method must be overriden.');
  }
}

class _ProviderWithDefiningInjector {
  final _Provider provider;
  final Injector injector;
  _ProviderWithDefiningInjector(this.provider, this.injector);
}
