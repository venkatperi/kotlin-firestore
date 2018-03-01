package com.vperi.android.firebase.firestore.delegate

import com.vperi.android.firebase.firestore.entity.FirestoreEntity
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class ReadWritePropertyDelegate<in T : FirestoreEntity, L : Any, R : Any> :
    BaseReadWritePropertyDelegate<T, L, R>() {

  operator fun provideDelegate(thisRef: T, prop: KProperty<*>):
      ReadWriteProperty<T, L> {
    onInit(thisRef, prop)
    return this
  }
}
