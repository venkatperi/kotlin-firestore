package com.vperi.android.firebase.firestore.delegate

import com.vperi.android.firebase.firestore.entity.FirestoreEntity
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class BaseReadWritePropertyDelegate<in T : FirestoreEntity, L, R> :
    ReadWriteProperty<T, L?>,
    DelegateWriteSupport<T, L, R>,
    BaseReadOnlyPropertyDelegate<T, L, R>() {

  override fun save(thisRef: T, property: KProperty<*>, value: R?) {
    thisRef.remoteSetProperty(property.name, value)
  }

  override operator fun setValue(thisRef: T, property: KProperty<*>, value: L?) {
    save(thisRef, property, converter.convertBack(value))
    thisRef.localSetProperty(property.name, value)
  }
}