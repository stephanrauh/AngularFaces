/*!
 * Animation
 *
 * Copyright (C) 2012, Kai Sellgren
 * Licensed under the MIT License.
 * http://www.opensource.org/licenses/mit-license.php
 */

part of animation;

class Easing {
  static const LINEAR = const Easing._(0);
  static const QUADRATIC_EASY_IN = const Easing._(1);
  static const QUADRATIC_EASY_OUT = const Easing._(2);
  static const QUADRATIC_EASY_IN_OUT = const Easing._(3);
  static const CUBIC_EASY_IN = const Easing._(4);
  static const CUBIC_EASY_OUT = const Easing._(5);
  static const CUBIC_EASY_IN_OUT = const Easing._(6);
  static const QUARTIC_EASY_IN = const Easing._(7);
  static const QUARTIC_EASY_OUT = const Easing._(8);
  static const QUARTIC_EASY_IN_OUT = const Easing._(9);
  static const QUINTIC_EASY_IN = const Easing._(10);
  static const QUINTIC_EASY_OUT = const Easing._(11);
  static const QUINTIC_EASY_IN_OUT = const Easing._(12);
  static const SINUSOIDAL_EASY_IN = const Easing._(13);
  static const SINUSOIDAL_EASY_OUT = const Easing._(14);
  static const SINUSOIDAL_EASY_IN_OUT = const Easing._(15);
  static const EXPONENTIAL_EASY_IN = const Easing._(16);
  static const EXPONENTIAL_EASY_OUT = const Easing._(17);
  static const EXPONENTIAL_EASY_IN_OUT = const Easing._(18);
  static const CIRCULAR_EASY_IN = const Easing._(19);
  static const CIRCULAR_EASY_OUT = const Easing._(20);
  static const CIRCULAR_EASY_IN_OUT = const Easing._(21);

  final int value;

  const Easing._(this.value);
}