package com.vperi.android.firebase.firestore.delegate

import com.vperi.android.firebase.firestore.entity.FirestoreEntity
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

abstract class ReadOnlyPropertyDelegate<in T : FirestoreEntity, out L, R> :
    BaseReadOnlyPropertyDelegate<T, L, R>() {

  operator fun provideDelegate(thisRef: T,
      prop: KProperty<*>): ReadOnlyProperty<T, L> {
    beforeProvideDelegate(thisRef, prop)
    return this
  }
}