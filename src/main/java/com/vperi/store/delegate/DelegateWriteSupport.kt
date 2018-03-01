package com.vperi.store.delegate

import com.vperi.android.firebase.firestore.converter.ValueConverter
import kotlin.reflect.KProperty

interface DelegateWriteSupport<in T, L, R> {

  val converter: ValueConverter<L, R>

  fun save(thisRef: T, property: KProperty<*>, value: R)
}