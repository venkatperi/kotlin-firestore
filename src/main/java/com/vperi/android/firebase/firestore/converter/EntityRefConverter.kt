package com.vperi.android.firebase.firestore.converter

import com.google.firebase.firestore.DocumentReference
import com.vperi.android.firebase.firestore.entity.FirestoreEntityRef
import com.vperi.android.firebase.firestore.factory.FirestoreEntityFactory
import com.vperi.entity.Entity
import com.vperi.entity.EntityRef

class EntityRefConverter<X : Entity>(
    val factory: FirestoreEntityFactory<X>) :
    ValueConverter<EntityRef<X>, DocumentReference> {

  override fun convert(value: DocumentReference?): EntityRef<X>? =
      when (value) {
        null -> null
        else -> FirestoreEntityRef(value, factory)
      }

  override fun convertBack(value: EntityRef<X>?): DocumentReference? =
      (value as FirestoreEntityRef<X>?)?.ref
}