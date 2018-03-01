package com.vperi.android.arch.lifecycle

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import com.vperi.kotlin.Event

open class LifecycleHelper(
    val owner: LifecycleOwner? = null,
    autoStart: Boolean = false
) : LifecycleObserver {

  private val lifecycleOwner = owner ?: SimpleLifecycleOwner(autoStart)

  val onStart = Event<Void>()
  val onStop = Event<Void>()
  val onCreate = Event<Void>()
  val onDestroy = Event<Void>()
  val onPause = Event<Void>()
  val onResume = Event<Void>()

  @OnLifecycleEvent(Lifecycle.Event.ON_START)
  fun start() {
    onStart(sticky = true)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  fun stop() {
    onStop()
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  fun create() {
    onCreate(sticky = true)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
  fun pause() {
    onPause()
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
  fun resume() {
    onResume(sticky = true)
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
//    onCreate.onListenerAdded!! += {
//      if (lifecycleOwner.lifecycle.currentState <= Lifecycle.State.CREATED)
//        (it!!)(null)
//    }
//
//    onStart.onListenerAdded!! += {
//      if (lifecycleOwner.lifecycle.currentState <= Lifecycle.State.STARTED)
//        (it!!)(null)
//    }

    lifecycleOwner.lifecycle.addObserver(this)
    val state = lifecycleOwner.lifecycle.currentState
    if (state < Lifecycle.State.DESTROYED) {
      when {
        state >= Lifecycle.State.CREATED -> create()
      }
      when {
        state >= Lifecycle.State.STARTED -> start()
      }
    }

  }
}