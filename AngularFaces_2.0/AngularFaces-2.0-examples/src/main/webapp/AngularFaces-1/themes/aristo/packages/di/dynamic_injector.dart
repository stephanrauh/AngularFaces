library di.dynamic_injector;

import 'di.dart';
import 'mirrors.dart';

/**
 * Dynamic implementation of [Injector] that uses mirrors.
 */
class DynamicInjector extends Injector {

  DynamicInjector({List<Module> modules, String name,
                  bool allowImplicitInjection: false})
      : super(modules: modules, name: name,
          allowImplicitInjection: allowImplicitInjection);

  DynamicInjector._fromParent(List<Module> modules, Injector parent, {name})
      : super.fromParent(modules, parent, name: name);

  newFromParent(List<Module> modules, String name) {
    return new DynamicInjector._fromParent(modules, this, name: name);
  }

  Object newInstanceOf(Type type, ObjectFactory getInstanceByKey,
                       Injector requestor, error) {
    var classMirror = reflectType(type);
    if (classMirror is TypedefMirror) {
      throw new NoProviderError(error('No implementation provided '
          'for ${getSymbolName(classMirror.qualifiedName)} typedef!'));
    }

    MethodMirror ctor = classMirror.declarations[classMirror.simpleName];

    if (ctor == null) {
      throw new NoProviderError('Unable to find default constructor for $type. '
          'Make sure class has a default constructor.' +
          (1.0 is int ?
              ' Make sure you have correctly configured @MirrorsUsed.' : ''));
    }

    resolveArgument(int pos) {
      ParameterMirror p = ctor.parameters[pos];
      if (MirrorSystem.getName(p.type.qualifiedName) == 'dynamic') {
        var name = MirrorSystem.getName(p.simpleName);
        throw new NoProviderError(error("The '$name' parameter must be typed"));
      }
      if (p.type is TypedefMirror) {
        throw new NoProviderError(
            error('Cannot create new instance of a typedef ${p.type}'));
      }
      if (p.metadata.isNotEmpty) {
        var annotations = p.metadata.map(
            (item) => item.type.reflectedType).toList();
        assert(annotations.length == 1);
        return getInstanceByKey(new Key((p.type as ClassMirror).reflectedType,
            annotations[0] ), requestor);
      } else {
        return getInstanceByKey(new Key((p.type as ClassMirror).reflectedType),
            requestor);
      }
    }

    var args = new List.generate(ctor.parameters.length, resolveArgument,
        growable: false);
    return classMirror.newInstance(ctor.constructorName, args).reflectee;
  }

  /**
   * Invoke given function and inject all its arguments.
   *
   * Returns whatever the function returns.
   */
  dynamic invoke(Function fn) {
    ClosureMirror cm = reflect(fn);
    MethodMirror mm = cm.function;
    int position = 0;
    List args = mm.parameters.map((ParameterMirror parameter) {
      try {
        if (parameter.metadata.isNotEmpty) {
          var annotations = parameter.metadata.map((item) => item.type.reflectedType).toList();
          return get((parameter.type as ClassMirror).reflectedType,
              annotations[0] );
        } else {
          return get((parameter.type as ClassMirror).reflectedType);
        }
      } on NoProviderError catch (e) {
        throw new NoProviderError(e.message);
      } finally {
        position++;
      }
    }).toList();

    return cm.apply(args).reflectee;
  }
}
