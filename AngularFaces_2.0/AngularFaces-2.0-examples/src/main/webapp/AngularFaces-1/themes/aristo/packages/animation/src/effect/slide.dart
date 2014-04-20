/*!
 * Animation
 *
 * Copyright (C) 2012, Kai Sellgren
 * Licensed under the MIT License.
 * http://www.opensource.org/licenses/mit-license.php
 */

part of effect;

Animation slideOut(Element element, {String direction: 'right', duration: 1000, Easing easing}) {
  var wrapper = _createWrapper(element);
  wrapper.style
    ..overflow = 'hidden'
    ..width = '${element.clientWidth}px'
    ..height = '${element.clientHeight}px'
    ..position = 'relative';

  return animate(element, properties: {'left': element.clientWidth});
}

Animation slideIn(Element element, {String direction: 'right', duration: 1000, Easing easing}) {
  var wrapper = _createWrapper(element);
  wrapper.style
    ..overflow = 'hidden'
    ..width = '${element.clientWidth}px'
    ..height = '${element.clientHeight}px'
    ..position = 'relative';

  element.style.left = '${element.clientWidth}px';

  return animate(element, properties: {'left': 0});
}

Animation fadeOut(Element element, {duration: 500, Easing easing}) {
  return animate(element, properties: {'opacity': 0}, duration: duration, easing: easing);
}

Animation fadeIn(Element element, {duration: 500, Easing easing}) {
  element.style.opacity = '0';
  return animate(element, properties: {'opacity': 1}, duration: duration, easing: easing);
}