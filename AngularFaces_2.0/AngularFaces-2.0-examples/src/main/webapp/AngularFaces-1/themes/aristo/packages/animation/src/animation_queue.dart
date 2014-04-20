/*!
 * Animation
 *
 * Copyright (C) 2012, Kai Sellgren
 * Licensed under the MIT License.
 * http://www.opensource.org/licenses/mit-license.php
 */

part of animation;

class AnimationQueue {
  int _position = 0;
  int interval = 0;
  int repeat = 0;
  int _repeatedSoFar = 0;

  bool _isListeningToComplete = false;

  /**
   * Returns the current "position" -- the nth animation that is taking place.
   */
  int get position => _position;

  List<Animation> _queue = [];

  /**
   * Returns the number of animations remaining in the queue.
   *
   * This includes the currently running animation.
   */
  int get remaining => _queue.length - _position;

  /**
   * Adds the given animation to the queue.
   */
  add(Animation animation) {
    _queue.add(animation);
  }

  /**
   * Adds every animation in the collection to the queue.
   */
  addAll(List animations) {
    animations.forEach((animation) => _queue.add(animation));
  }

  /**
   * Clears the queue.
   *
   * If there's an animation occurring, it will complete.
   */
  clear() {
    _queue.clear();
    _position = 0;
  }

  stop() {
    throw new UnsupportedError('');
  }

  pause() {
    throw new UnsupportedError('');
  }

  finish() {
    throw new UnsupportedError('');
  }

  /**
   * Removes the given animation from the queue.
   */
  remove(Animation animation) {
    var position = -1;

    // Determine the position of [animation] within the queue.
    for (var i = 0, length = _queue.length; i < length; i++) {
      if (_queue[i] == animation) {
        position = i;
        break;
      }
    }

    if (position >= 0)
      _queue.removeAt(position);
  }

  /**
   * Removes the animation at the given index.
   */
  removeAt(int index) {
    _queue.removeAt(index);
  }

  /**
   * Directly jumps to the nth animation in the queue.
   *
   * If there is currently an animation running, it will complete before this.
   */
  jump(int position) {
    _position = position;
  }

  /**
   * Runs the queue of animations.
   */
  run() {
    if (_position >= _queue.length) {
      // TODO: Fire an event.

      _repeatedSoFar++;

      if (_repeatedSoFar < repeat || repeat == -1) {
        jump(0);
        run();
      }
    } else {
      Animation anim = _queue[_position];
      anim.stop();

      // TODO: There must be a better way to do this.
      if (_isListeningToComplete == false) {        
        _isListeningToComplete = (_position == (_queue.length - 1));
        anim.onComplete.listen((data) {
          _position++;

          _runAfterInterval();
        });
      }

      anim.run();
    }
  }

  /**
   * A helper method which runs the next animation after the interval.
   */
  _runAfterInterval() {
    if (interval > 0) {
      new Timer(new Duration(milliseconds:interval), run);
    } else {
      run();
    }
  }
}
