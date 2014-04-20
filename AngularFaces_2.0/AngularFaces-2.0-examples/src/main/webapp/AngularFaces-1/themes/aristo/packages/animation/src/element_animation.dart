/*!
 * Animation
 *
 * Copyright (C) 2012, Kai Sellgren
 * Licensed under the MIT License.
 * http://www.opensource.org/licenses/mit-license.php
 */

part of animation;

/**
 * [ElementAnimation] is used to animate HTMLElement styles (such as left, width) and properties (such as scrollTop).
 */
class ElementAnimation extends Animation {
  static final RegExp valueUnitRegex = new RegExp(r'^(-?[0-9\.]+)([a-zA-Z%]+)$');

  /**
   * These are our target properties where we should end up in if all goes well.
   */
  Map<String, Object> toProperties = {};

  /**
   * A map of properties that represent the initial state of the element (beginning values).
   */
  final Map<String, Object> fromProperties = {};

  /**
   * This map stores the current properties our element has during the process of animation. These change all the time.
   */
  final Map<String, Object> currentProperties = {};

  /**
   * We need to keep track of what units are being used for properties (e.g. width could be px or em).
   */
  final Map<String, String> units = {};

  final Element element;

  bool _isInitialized = false;

  ElementAnimation(this.element) : super();

  set properties(value) => toProperties = value;

  /**
   * Calculates the current style and sets [fromProperties].
   *
   * This should be called just before the animation starts.
   */
  _initializeFromProperties() {
    if (_isInitialized) throw 'Unexpected scenario: element properties should not be initialized more than once.';

    CssStyleDeclaration style = element.getComputedStyle("");

    new Map.from(toProperties).forEach((key, value) {
      var cssValue = element.style.getPropertyValue(key);

      if (cssValue == '') cssValue = style.getPropertyValue(key);

      // Convert "auto" and "" to the actual value.
      if (cssValue == 'auto' || cssValue == '') {
        cssValue = _getActualValueForAuto(key);
      }

      // We could not convert "auto" in this particular case.
      if (cssValue == 'auto') {
        throw 'Cannot animate property "$key", because it had initial value of "auto", which is not supported for this type of property. Please specify an initial value before you start animating it.';
      }

      // Empty value.
      if (cssValue == '') {
        throw 'Cannot animate property "$key", because the initial value was an empty string "". Please specify an initial value.';
      }

      // Example properties needing a unit: width, height, top, etc.
      if (_doesPropertyNeedUnit(key)) {
        var match = new RegExp(r'^(-?[0-9\.]+)([a-zA-Z%]+)$').firstMatch(cssValue);

        // There's a unit.
        if (match != null) {
          var value = match.group(1);
          var unit = match.group(2);

          fromProperties[key] = double.parse(value);
          currentProperties[key] = double.parse(value);
          units[key] = unit;
        } else {
          var match = valueUnitRegex.firstMatch(cssValue);

          // A number without a unit.
          if (match != null) {
            var value = match.group(1);

            fromProperties[key] = double.parse(value);
            currentProperties[key] = double.parse(value);
            units[key] = _doesPropertyNeedUnit(key) ? 'px' : ''; // Default unit "px" if user did not specify anything.
          } else {
            fromProperties[key] = double.parse(cssValue);
            currentProperties[key] = double.parse(cssValue);
            units[key] = 'px'; // Default unit "px" if user did not specify anything.
          }
        }
      }

      // Example properties not needing a unit: opacity
      else {
        fromProperties[key] = double.parse(cssValue);
        currentProperties[key] = double.parse(cssValue);
        units[key] = "";
      }

      // If the user specified a to-value with a unit, let's use that as our unit instead.
      if (value is String) {
        var match = valueUnitRegex.firstMatch(value);

        if (match != null) {
          var value = match.group(1);
          var unit = match.group(2);

          toProperties[key] = double.parse(value);
          units[key] = unit;
        }
      }

      /*
      TODO: Make use of these formats.
      var hexColorRegExp = new RegExp('^#([0-9]+)\$');
      var rgbRegExp = new RegExp('^rgb\(([0-9]{1,3}),\s*([0-9]{1,3}),\s*([0-9]{1,3})\)\$');
      var rgbaRegExp = new RegExp('^rgba\(([0-9]{1,3}),\s*([0-9]{1,3}),\s*([0-9]{1,3}),\s*([0-9\.]+)\)\$');
      var textShadowRegExp = new RegExp('^1px 1px 1px rgba()\$');
       */
    });

    _isInitialized = true;
  }

  stop() {
    super.stop();

    currentProperties.clear();
    _isInitialized = false;

    _initializeFromProperties();

    fromProperties.forEach((String key, value) {
      element.style.setProperty(key, '${value}${units[key]}');
      currentProperties[key] = value;
    });
  }

  run() {
    // We have to remember that the animation can be created without running immediately.
    // If it's ran later, the initial styles may be different, so we defer loading of properties until run() is called.
    if (_isInitialized == false) _initializeFromProperties();

    if (_paused) {
      _paused = false;
      _pausedFor += _getNowMilliseconds() - _pausedAt;
    }

    // Set the start time if this is the first time.
    if (_startTime == null) _startTime = _getNowMilliseconds();

    window.requestAnimationFrame(_advance);

    super.run();
  }

  /**
   * Advances the animation by one step.
   */
  _advance(num highResTime) {
    if (_paused || _stopped) return;

    var currentTime = _getNowMilliseconds();

    // Reduce the time we have been paused for, to correct for the lost time.
    currentTime -= _pausedFor;

    // Calculate how much time we have left.
    var timeLeft = duration - (currentTime - _startTime);

    if (_onStepController.hasListener) {
      var percentage = 100 - (100 / (duration / timeLeft));

      // Clamp.
      if (percentage > 100) {
        percentage = 100;
      } else if (percentage < 0) {
        percentage = 0;
      }

      _onStepController.add({
        'animation': this,
        'percentage': percentage
      });
    }

    // Perform the animation.
    toProperties.forEach((String key, value) {
      var intermediateValue;

      // If there's still time left, calculate the exact figures.
      if (timeLeft > 0) {
        var baseValue = fromProperties[key];      // The base/original value.
        var change    = value - baseValue;        // How much the values differ.
        var time      = currentTime - _startTime; // How much time has passed.

        // Calculate the eased value.
        intermediateValue = super._performEasing(time, duration, change, baseValue);

        // Clamps the intermediate value to be within value's range. i.e. to ensure we never exceed limits.
        if (baseValue > value) {
          if (value > 0 && intermediateValue < value)
            intermediateValue = value;
          if (value < 0 && intermediateValue < value)
            intermediateValue = value;
        } else {
          if (value > 0 && intermediateValue > value)
            intermediateValue = value;
          if (value < 0 && intermediateValue > value)
            intermediateValue = value;
        }
      } else {
        // If no time left, jump to the final value.
        intermediateValue = value;
      }

      currentProperties[key] = intermediateValue;

      var result = _propertyNeedsPreciseAnimation(key) ? intermediateValue : intermediateValue.round();

      if (_isAnimatableElementProperty(key)) {
        _setPropertyValue(key, result);
      } else {
        element.style.setProperty(key, '$result${units[key]}');
      }
    });

    // If we still have time left, go on.
    if (timeLeft > 0) {
      window.requestAnimationFrame(_advance);
    } else {
      _onCompleteController.add(null);
    }
  }

  /**
   * Returns true if the given property requires a unit.
    *
    * Example: for width, returns true, but for opacity, returns false.
   */
  bool _doesPropertyNeedUnit(String propertyName) {
    var el = new DivElement();
    el.style.setProperty(propertyName, '0px');
    return el.style.getPropertyValue(propertyName) == '0px';
  }

  /**
   * We try to convert "auto" value to the actual value.
   */
  String _getActualValueForAuto(String propertyName) {
    convert() {
      switch (propertyName) {
        case 'width':
          return element.clientWidth;
        case 'height':
          return element.clientHeight;
        case 'top':
        case 'right':
        case 'bottom':
        case 'left':
          return 0;
        case 'scrollTop':
          return element.scrollTop;
        case 'scrollLeft':
          return element.scrollLeft;
        case 'opacity':
          return 1;
      }
    }

    var value = convert();
    if (value != null) return value.toString();

    return 'auto';
  }

  /**
   * Returns true if the property requires animation with decimal precision.
   *
   * Properties like 'opacity' needs to animate precisely, but e.g. width should not.
   */
  bool _propertyNeedsPreciseAnimation(String propertyName) {
    switch (propertyName) {
      case 'opacity':
        return true;
    }

    return false;
  }

  /**
   * Returns true if the given property is not a CSS property, but is an animatable element property.
   */
  bool _isAnimatableElementProperty(String propertyName) {
    const props = const ['scrollTop', 'scrollLeft'];

    return props.contains(propertyName);
  }

  /**
   * Sets a property value such as scrollTop.
   */
  void _setPropertyValue(String propertyName, value) {
    // TODO: Use mirrors when they land in dart2js?
    switch (propertyName) {
      case 'scrollTop':
        element.scrollTop = value;
        break;
      case 'scrollLeft':
        element.scrollLeft = value;
        break;
    }
  }
}