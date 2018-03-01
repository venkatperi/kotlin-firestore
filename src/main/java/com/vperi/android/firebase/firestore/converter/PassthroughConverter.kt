package com.vperi.android.firebase.firestore.converter

@Suppress("UNCHECKED_CAST")
class PassthroughConverter<L, R> : ValueConverter<L, R> {
  override fun convertBack(value: L): R {
    return value as R
  }

  override fun convert(value: R): L {
    return value as L
  }
}