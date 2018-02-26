package com.vperi.android.firebase.firestore.converter

interface ValueConverter<L, R> {
  fun convert(value: R): L
  fun convertBack(value: L): R
}


