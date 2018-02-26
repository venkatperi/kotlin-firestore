package com.vperi.android.arch.lifecycle

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import com.vperi.kotlin.Event

open class LifecycleHelper(
    owner: LifecycleOwner?,
    autoStart: Boolean = false) : LifecycleObserver {

  val onStart = Event<Void>()
  val onStop = Event<Void>()
  val onCreate = Event<Void>()
  val onDestroy = Event<Void>()
  val onPause = Event<Void>()
  val onResume = Event<Void>()

  @OnLifecycleEvent(Lifecycle.Event.ON_START)
  fun start() {
    onStart()
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  fun stop() {
    onStop()
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  fun create() {
    onCreate()
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
  fun pause() {
    onPause()
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
  fun resume() {
    onResume()
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  internal fun ownerDestroy(source: LifecycleOwner) {
    source.lifecycle.removeObserver(this)
    destroy()
  }

  fun destroy() {
    onDestroy()
  }

  init {
    when {
      owner != null -> owner.lifecycle.addObserver(this)
      autoStart -> start()
    }
  }
}