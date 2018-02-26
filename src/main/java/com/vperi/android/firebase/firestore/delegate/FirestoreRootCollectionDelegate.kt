package com.vperi.android.firebase.firestore.delegate

import com.google.firebase.firestore.CollectionReference
import com.vperi.android.firebase.firestore.converter.CollectionConverter
import com.vperi.android.firebase.firestore.entity.FirestoreRoot
import com.vperi.android.firebase.firestore.factory.FirestoreEntityFactory
import com.vperi.entity.Entity
import com.vperi.entity.EntityCollection
import kotlin.reflect.KProperty

class FirestoreRootCollectionDelegate<
    in T : FirestoreRoot,
    X : Entity>(factory: FirestoreEntityFactory<X>) :
    BaseReadOnlyDelegate<T, EntityCollection<X>, CollectionReference>() {

  override val converter = CollectionConverter(factory)

  override fun load(thisRef: T, property: KProperty<*>): CollectionReference =
      thisRef.fireStore!!.collection(property.name)
}