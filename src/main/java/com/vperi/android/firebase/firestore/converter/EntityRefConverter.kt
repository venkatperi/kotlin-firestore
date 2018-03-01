package com.vperi.android.firebase.firestore.converter

import com.google.firebase.firestore.DocumentReference
import com.vperi.android.firebase.firestore.LazyFactory
import com.vperi.android.firebase.firestore.entity.FirestoreEntityRef
import com.vperi.store.entity.Entity
import com.vperi.store.entity.EntityRef

class EntityRefConverter<X : Entity>(
    val factory: LazyFactory<X>) :
    ValueConverter<EntityRef<X>, DocumentReference> {

  override fun convert(value: DocumentReference): EntityRef<X> =
      FirestoreEntityRef(value, factory)

  override fun convertBack(value: EntityRef<X>): DocumentReference =
      (value as FirestoreEntityRef<X>).ref
}