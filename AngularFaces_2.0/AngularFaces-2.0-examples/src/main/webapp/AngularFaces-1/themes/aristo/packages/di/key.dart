part of di;

class Key {
  final Type type;
  final Type annotation;

  const Key(this.type, [this.annotation]);

  bool operator ==(other) =>
      other is Key && other.type == type && other.annotation == annotation;

  int get hashCode => type.hashCode + annotation.hashCode;

  String toString() {
    String asString = type.toString();
    if (annotation != null) {
      asString += ' annotated with: ${annotation.toString()}';
    }
    return asString;
  }
}
