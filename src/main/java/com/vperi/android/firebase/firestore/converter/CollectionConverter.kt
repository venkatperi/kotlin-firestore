package com.vperi.android.firebase.firestore.converter

import com.google.firebase.firestore.CollectionReference
import com.vperi.android.firebase.firestore.LazyFactory
import com.vperi.android.firebase.firestore.entity.FirestoreCollection
import com.vperi.store.entity.Entity
import com.vperi.store.entity.EntityCollection

class CollectionConverter<X : Entity>(
    val factory: LazyFactory<X>) :
    ValueConverter<EntityCollection<X>, CollectionReference> {

  override fun convertBack(value: EntityCollection<X>): CollectionReference {
    throw IllegalStateException()
  }

  override fun convert(value: CollectionReference): EntityCollection<X> =
      FirestoreCollection(value, factory)
}

