package com.vperi.android.firebase.firestore.delegate

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

abstract class BaseReadOnlyDelegate<in T, L, R> :
    ReadOnlyProperty<T, L>,
    DelegateReadSupport<T, L, R> {

  open fun beforeProvideDelegate(thisRef: T, prop: KProperty<*>) {
  }

  override operator fun getValue(thisRef: T, property: KProperty<*>): L =
      converter.convert(load(thisRef, property))
}

