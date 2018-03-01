package com.vperi.android.firebase.firestore.delegate

import com.vperi.android.firebase.firestore.entity.FirestoreEntity
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

abstract class ReadOnlyNullablePropertyDelegate<in T : FirestoreEntity, L, R> :
    BaseReadOnlyPropertyDelegate<T, L?, R?>() {

  operator fun provideDelegate(thisRef: T,
      prop: KProperty<*>): ReadOnlyProperty<T, L?> {
    onInit(thisRef, prop)
    return this
  }

  override operator fun getValue(thisRef: T, property: KProperty<*>): L? =
      with(thisRef) {
        @Suppress("UNCHECKED_CAST")
        var value: L? = properties[property.name] as L?
        if (value == null) {
          value = super.getValue(thisRef, property)
          if (value != null) properties[property.name] = value
        }
        value
      }
}