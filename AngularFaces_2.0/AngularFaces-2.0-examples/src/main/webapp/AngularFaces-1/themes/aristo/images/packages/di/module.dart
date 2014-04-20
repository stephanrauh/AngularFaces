part of di;

typedef dynamic FactoryFn(Injector injector);

/**
 * Visibility determines if the instance in the defining module is visible to
 * the requesting injector. If true is returned, then the instance from the
 * defining injector is provided. If false is returned, the injector keeps
 * walking up the tree to find another visible instance.
 */
typedef bool Visibility(Injector requesting, Injector defining);

typedef Object TypeFactory(factory(Type type, Type annotation));

/**
 * A collection of type bindings. Once the module is passed into the injector,
 * the injector creates a copy of the module and all subsequent changes to the
 * module have no effect.
 */
class Module {
  final Map<Key, _Provider> _providers = <Key, _Provider>{};
  final List<Module> _childModules = <Module>[];
  Map<Type, TypeFactory> _typeFactories = {};

  Map<Type, TypeFactory> get typeFactories {
    if (_childModules.isEmpty) {
      return _typeFactories;
    }
    var tmp = new Map.from(_typeFactories);
    _childModules.forEach((child) {
      if (child.typeFactories != null) {
        child.typeFactories.forEach((key, factory) {
          tmp[key] = factory;
        });
      }
    });
    return tmp;
  }

  set typeFactories(Map<Type, TypeFactory> factories) {
    _typeFactories = factories;
  }

  Map<Key, _Provider> _providersCache;

  /**
   * Compiles and returs bindings map by performing depth-first traversal of the
   * child (installed) modules.
   */
  Map<Key, _Provider> get _bindings {
    if (_isDirty) {
      _providersCache = <Key, _Provider>{};
      _childModules.forEach((child) => _providersCache.addAll(child._bindings));
      _providersCache.addAll(_providers);
    }
    return _providersCache;
  }

  /**
   * Register binding to a concrete value.
   *
   * The [value] is what actually will be injected.
   */
  void value(Type id, value, {Type withAnnotation, Visibility visibility}) {
    _dirty();
    Key key = new Key(id, withAnnotation);
    _providers[key] = new _ValueProvider(value, visibility);
  }

  /**
   * Register binding to a [Type].
   *
   * The [implementedBy] will be instantiated using [new] operator and the
   * resulting instance will be injected. If no type is provided, then it's
   * implied that [id] should be instantiated.
   */
  void type(Type id, {Type withAnnotation, Type implementedBy,
      Visibility visibility}) {
    _dirty();
    Key key = new Key(id, withAnnotation);
    _providers[key] = new _TypeProvider(
        implementedBy == null ? id : implementedBy, visibility);
  }

  /**
   * Register binding to a factory function.abstract
   *
   * The [factoryFn] will be called and all its arguments will get injected.
   * The result of that function is the value that will be injected.
   */
  void factory(Type id, FactoryFn factoryFn, {Type withAnnotation,
      Visibility visibility}) {
    _keyedFactory(new Key(id, withAnnotation), factoryFn,
        visibility: visibility);
  }

  void _keyedFactory(Key key, FactoryFn factoryFn, {Visibility visibility}) {
    _dirty();
    _providers[key] = new _FactoryProvider(factoryFn, visibility);
  }

  /**
   * Installs another module into this module. Bindings defined on this module
   * take precidence over the installed module.
   */
  void install(Module module) {
    _childModules.add(module);
    _dirty();
  }

  _dirty() {
    _providersCache = null;
  }

  bool get _isDirty =>
      _providersCache == null || _childModules.any((m) => m._isDirty);
}

/** Deafault creation strategy is to instantiate on the defining injector. */
dynamic _defaultCreationStrategy(Injector requesting, Injector defining,
    dynamic factory()) => factory();

/** By default all values are visible to child injectors. */
bool _defaultVisibility(_, __) => true;

typedef Object ObjectFactory(Key type, Injector requestor);

abstract class _Provider {
  final Visibility visibility;

  _Provider(this.visibility);

  dynamic get(Injector injector, Injector requestor, ObjectFactory getInstanceByKey, error);
}

class _ValueProvider extends _Provider {
  dynamic value;

  _ValueProvider(this.value, [Visibility visibility]) : super(visibility);

  dynamic get(Injector injector, Injector requestor, ObjectFactory getInstanceByKey, error) =>
      value;
}

class _TypeProvider extends _Provider {
  final Type type;

  _TypeProvider(this.type, [Visibility visibility]) : super(visibility);

  dynamic get(Injector injector, Injector requestor, ObjectFactory getInstanceByKey, error) =>
      injector.newInstanceOf(type, getInstanceByKey, requestor, error);

}

class _FactoryProvider extends _Provider {
  final Function factoryFn;

  _FactoryProvider(this.factoryFn, [Visibility visibility]) : super(visibility);

  dynamic get(Injector injector, Injector requestor, ObjectFactory getInstanceByKey, error) =>
      factoryFn(injector);
}
