package com.vperi.android.arch.lifecycle

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry

open class SimpleLifecycleOwner(autoStart: Boolean = false) : LifecycleOwner {
  private val registry: LifecycleRegistry by lazy { LifecycleRegistry(this) }

  override fun getLifecycle(): Lifecycle = registry

  fun start() {
    registry.markState(Lifecycle.State.STARTED)
  }

  fun create() {
    registry.markState(Lifecycle.State.CREATED)
  }

  fun resume() {
    registry.markState(Lifecycle.State.RESUMED)
  }

  fun destroy() {
    registry.markState(Lifecycle.State.DESTROYED)
  }

  init {
    if (autoStart) {
      create()
      start()
    }
  }
}