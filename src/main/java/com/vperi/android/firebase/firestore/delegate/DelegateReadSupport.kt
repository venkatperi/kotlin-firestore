package com.vperi.android.firebase.firestore.delegate

import com.vperi.android.firebase.firestore.converter.ValueConverter
import kotlin.reflect.KProperty

interface DelegateReadSupport<in T, L, R> {

  val converter: ValueConverter<L, R>

  fun load(thisRef: T, property: KProperty<*>): R
}