package com.vperi.android.firebase.firestore.delegate

import com.vperi.android.firebase.firestore.converter.PassthroughConverter
import com.vperi.android.firebase.firestore.entity.FirestoreEntity

open class FirestoreNullablePropertyDelegate<in T : FirestoreEntity, R : Any?> :
    ReadWriteNullablePropertyDelegate<T, R?, R?>() {

  override val converter = PassthroughConverter<R?, R?>()
}