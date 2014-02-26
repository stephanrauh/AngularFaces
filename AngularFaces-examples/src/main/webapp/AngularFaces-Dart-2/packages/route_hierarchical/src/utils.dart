library route.utils;

bool mapsEqual(Map a, Map b) {
  if (a.keys.length != b.keys.length) {
    return false;
  }
  for (var keyInA in a.keys) {
    if (!b.containsKey(keyInA) || a[keyInA] != b[keyInA]) {
      return false;
    }
  }
  return true;
}
