/*!
 * Animation
 *
 * Copyright (C) 2012, Kai Sellgren
 * Licensed under the MIT License.
 * http://www.opensource.org/licenses/mit-license.php
 */

part of animation;

/**
 * Performs an animation on the given [element] with the given [properties].
 *
 * You may specify a [duration] in milliseconds.
 *
 * By default, linear interpolation is used, but you may specify another [easingType] method to use.
 */
Animation animate(Element element, {int duration, Easing easing, Map<String, Object> properties, bool paused}) {
  assert(element != null);
  final animation = new ElementAnimation(element);

  if (properties != null) {
    animation.toProperties = properties;
  }

  if (easing != null) {
    animation.easing = easing;
  }

  if (duration != null) {
    animation.duration = duration;
  }

  if (paused != true) {
    animation.run();
  }

  return animation;
}

int _getNowMilliseconds() => new DateTime.now().millisecondsSinceEpoch;
