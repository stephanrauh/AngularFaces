/*!
 * Animation
 *
 * Copyright (C) 2012, Kai Sellgren
 * Licensed under the MIT License.
 * http://www.opensource.org/licenses/mit-license.php
 */

part of animation;

typedef void StepCallback(Animation anim, double percentage);

abstract class Animation {
  int _duration = 500;

  /**
   * Returns the duration of this animation in milliseconds.
   */
  int get duration => _duration;

  /**
   * Sets the duration for this animation in milliseconds
   */
  set duration(int value) {
    _duration = value;
  }

  int _startTime;
  int _pausedAt;
  int _pausedFor = 0;
  bool _paused = false;
  bool _stopped = false;
  Easing easing = Easing.QUARTIC_EASY_IN_OUT;

  StreamController _onStepController = new StreamController.broadcast();
  StreamController _onCompleteController = new StreamController.broadcast();
  Stream get onStep => _onStepController.stream;
  Stream get onComplete => _onCompleteController.stream;

  Animation() {}

  /**
   * Pauses the animation.
   */
  pause() {
    _paused = true;
    _pausedAt = _getNowMilliseconds();
  }

  /**
   * Pauses the animation for the given [duration], and then resumes.
   *
   * [duration] is in milliseconds.
   */
  pauseFor(int duration) {
    pause();
    new Timer(new Duration(milliseconds: duration), run);
  }

  /**
   * Stops the animation and resets it to the beginning state.
   */
  stop() {
    _paused = false;
    _pausedAt = 0;
    _pausedFor = 0;
    _startTime = null;
    _stopped = true;
  }

  /**
   * Completes the animation by setting it to the final state.
   */
  finish() {
    throw new UnsupportedError('');
  }

  /**
   * Resets the animation to the initial state, but does not stop the animation.
   */
  reset() {
    throw new UnsupportedError('');
  }

  /**
   * Fowards the animation by the given [duration].
   *
   * [duration] is in milliseconds.
   *
   * If the current state forwarded by duration exceeds the total duration of the animation,
   * the animation will simply finish and be set to the final state.
   */
  forward(int duration) {
    throw new UnsupportedError('');
  }

  /**
   * Delays the animation by [duration].
   *
   * This means that the animation takes longer.
   *
   * [duration] is in milliseconds.
   */
  delay(int duration) {
    throw new UnsupportedError('');

    this.duration += duration;
    _startTime -= duration * 2;
  }

  /**
   * Runs the animation. If the animation was paused before, it will be resumed.
   */
  run() {
    _paused = false;
    _stopped = false;
  }

  /**
   * Resumes a paused animation.
   */
  resume() => run();

  /**
   * Performs easing for the given values using the chosen easing type.
   */
  _performEasing(time, duration, change, baseValue) {
    switch (easing) {
      case Easing.LINEAR:
        return EasingEngine.linear(time, duration, change, baseValue);
      case Easing.QUADRATIC_EASY_IN:
        return EasingEngine.easeInQuad(time, duration, change, baseValue);
      case Easing.QUADRATIC_EASY_OUT:
        return EasingEngine.easeOutQuad(time, duration, change, baseValue);
      case Easing.QUADRATIC_EASY_IN_OUT:
        return EasingEngine.easeInOutQuad(time, duration, change, baseValue);
      case Easing.CUBIC_EASY_IN:
        return EasingEngine.easeInCubic(time, duration, change, baseValue);
      case Easing.CUBIC_EASY_OUT:
        return EasingEngine.easeOutCubic(time, duration, change, baseValue);
      case Easing.CUBIC_EASY_IN_OUT:
        return EasingEngine.easeInOutCubic(time, duration, change, baseValue);
      case Easing.QUARTIC_EASY_IN:
        return EasingEngine.easeInQuartic(time, duration, change, baseValue);
      case Easing.QUARTIC_EASY_OUT:
        return EasingEngine.easeOutQuartic(time, duration, change, baseValue);
      case Easing.QUARTIC_EASY_IN_OUT:
        return EasingEngine.easeInOutQuartic(time, duration, change, baseValue);
      case Easing.QUINTIC_EASY_IN:
        return EasingEngine.easeInQuintic(time, duration, change, baseValue);
      case Easing.QUINTIC_EASY_OUT:
        return EasingEngine.easeOutQuintic(time, duration, change, baseValue);
      case Easing.QUINTIC_EASY_IN_OUT:
        return EasingEngine.easeInOutQuintic(time, duration, change, baseValue);
      case Easing.SINUSOIDAL_EASY_IN:
        return EasingEngine.easeInSine(time, duration, change, baseValue);
      case Easing.SINUSOIDAL_EASY_OUT:
        return EasingEngine.easeOutSine(time, duration, change, baseValue);
      case Easing.SINUSOIDAL_EASY_IN_OUT:
        return EasingEngine.easeInOutSine(time, duration, change, baseValue);
      case Easing.EXPONENTIAL_EASY_IN:
        return EasingEngine.easeInExponential(time, duration, change, baseValue);
      case Easing.EXPONENTIAL_EASY_OUT:
        return EasingEngine.easeOutExponential(time, duration, change, baseValue);
      case Easing.EXPONENTIAL_EASY_IN_OUT:
        return EasingEngine.easeInOutExponential(time, duration, change, baseValue);
      case Easing.CIRCULAR_EASY_IN:
        return EasingEngine.easeInCircular(time, duration, change, baseValue);
      case Easing.CIRCULAR_EASY_OUT:
        return EasingEngine.easeOutCircular(time, duration, change, baseValue);
      case Easing.CIRCULAR_EASY_IN_OUT:
        return EasingEngine.easeInOutCircular(time, duration, change, baseValue);
    }

    throw new Exception('Could not perform easing. Did you choose a proper easing type?');
  }
}
