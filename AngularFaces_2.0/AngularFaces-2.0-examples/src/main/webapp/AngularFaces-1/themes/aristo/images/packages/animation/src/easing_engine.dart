/*!
 * Animation
 *
 * Copyright (C) 2012, Kai Sellgren
 * Licensed under the MIT License.
 * http://www.opensource.org/licenses/mit-license.php
 */

part of animation;

class EasingEngine {
  /**
   * Performs a linear.
   */
  static num linear(num time, num duration, num change, num baseValue) {
    return change * time / duration + baseValue;
  }

  // QUADRATIC

  /**
   * Performs a quadratic easy-in.
   */
  static num easeInQuad(num time, num duration, num change, num baseValue) {
    time = time / duration;

    return change * time * time + baseValue;
  }

  /**
   * Performs a quadratic easy-out.
   */
  static num easeOutQuad(num time, num duration, num change, num baseValue) {
    time = time / duration;

    return -change * time * (time - 2) + baseValue;
  }

  /**
   * Performs a quadratic easy-in-out.
   */
  static num easeInOutQuad(num time, num duration, num change, num baseValue) {
    time = time / (duration / 2);

    if (time < 1)
      return change / 2 * time * time + baseValue;

    time--;

    return -change / 2 * (time * (time - 2) - 1) + baseValue;
  }

  // CUBIC

  /**
   * Performs a cubic easy-in.
   */
  static num easeInCubic(num time, num duration, num change, num baseValue) {
    time = time / duration;

    return change * time * time * time + baseValue;
  }

  /**
   * Performs a cubic easy-out.
   */
  static num easeOutCubic(num time, num duration, num change, num baseValue) {
    time = time / duration;

    time--;

    return change * (time * time * time + 1) + baseValue;
  }

  /**
   * Performs a cubic easy-in-out.
   */
  static num easeInOutCubic(num time, num duration, num change, num baseValue) {
    time = time / (duration / 2);

    if (time < 1)
      return change / 2 * time * time * time + baseValue;

    time -= 2;

    return change / 2 * (time * time * time + 2) + baseValue;
  }

  // QUARTIC

  /**
   * Performs a quartic easy-in.
   */
  static num easeInQuartic(num time, num duration, num change, num baseValue) {
    time = time / duration;

    return change * time * time * time * time + baseValue;
  }

  /**
   * Performs a quartic easy-out.
   */
  static num easeOutQuartic(num time, num duration, num change, num baseValue) {
    time = time / duration;

    time--;

    return -change * (time * time * time * time - 1) + baseValue;
  }

  /**
   * Performs a quartic easy-in-out.
   */
  static num easeInOutQuartic(num time, num duration, num change, num baseValue) {
    time = time / (duration / 2);

    if (time < 1)
      return change / 2 * time * time * time * time + baseValue;

    time -= 2;

    return -change / 2 * (time * time * time * time - 2) + baseValue;
  }

  // QUINTIC

  /**
   * Performs a quintic easy-in.
   */
  static num easeInQuintic(num time, num duration, num change, num baseValue) {
    time = time / duration;

    return change * time * time * time * time * time + baseValue;
  }

  /**
   * Performs a quintic easy-out.
   */
  static num easeOutQuintic(num time, num duration, num change, num baseValue) {
    time = time / duration;

    time--;

    return change * (time * time * time * time * time + 1) + baseValue;
  }

  /**
   * Performs a quintic easy-in-out.
   */
  static num easeInOutQuintic(num time, num duration, num change, num baseValue) {
    time = time / (duration / 2);

    if (time < 1)
      return change / 2 * time * time * time * time * time + baseValue;

    time -= 2;

    return change / 2 * (time * time * time * time * time + 2) + baseValue;
  }

  // SINUSOIDAL

  /**
   * Performs a sine easy-in.
   */
  static num easeInSine(num time, num duration, num change, num baseValue) {
    return -change * cos(time / duration * (PI / 2)) + change + baseValue;
  }

  /**
   * Performs a sine easy-out.
   */
  static num easeOutSine(num time, num duration, num change, num baseValue) {
    return change * sin(time / duration * (PI / 2)) + baseValue;
  }

  /**
   * Performs a sine easy-in-out.
   */
  static num easeInOutSine(num time, num duration, num change, num baseValue) {
    return -change / 2 * (cos(time / duration * PI) - 1) + baseValue;
  }

  // EXPONENTIAL

  /**
   * Performs an exponential easy-in.
   */
  static num easeInExponential(num time, num duration, num change, num baseValue) {
    return change * pow(2, 10 * (time / duration - 1)) + baseValue;
  }

  /**
   * Performs an exponential easy-out.
   */
  static num easeOutExponential(num time, num duration, num change, num baseValue) {
    return change * (-pow(2, -10 * time / duration) + 1) + baseValue;
  }

  /**
   * Performs an exponential easy-in-out.
   */
  static num easeInOutExponential(num time, num duration, num change, num baseValue) {
    time = time / (duration / 2);

    if (time < 1)
      return change / 2 * pow(2, 10 * (time - 1)) + baseValue;

    time--;

    return change / 2 * (-pow(2, -10 * time) + 2) + baseValue;
  }

  // CIRCULAR

  /**
   * Performs a circular easy-in.
   */
  static num easeInCircular(num time, num duration, num change, num baseValue) {
    time = time / duration;

    return -change * (sqrt(1 - time * time) - 1) + baseValue;
  }

  /**
   * Performs a circular easy-out.
   */
  static num easeOutCircular(num time, num duration, num change, num baseValue) {
    time = time / duration;

    time--;

    return change * sqrt(1 - time * time) + baseValue;
  }

  /**
   * Performs a circular easy-in-out.
   */
  static num easeInOutCircular(num time, num duration, num change, num baseValue) {
    time = time / (duration / 2);

    if (time < 1)
      return -change / 2 * sqrt(1 - time * time) + baseValue;

    time -= 2;

    return change / 2 * (sqrt(1 - time * time) + 1) + baseValue;
  }
}