package com.vperi.android.firebase.firestore.delegate

import com.google.firebase.firestore.CollectionReference
import com.vperi.android.firebase.firestore.LazyFactory
import com.vperi.android.firebase.firestore.converter.CollectionConverter
import com.vperi.android.firebase.firestore.entity.FirestoreRoot
import com.vperi.store.delegate.BaseReadOnlyDelegate
import com.vperi.store.entity.Entity
import com.vperi.store.entity.EntityCollection
import kotlin.reflect.KProperty

class FirestoreRootCollectionDelegate<
    in T : FirestoreRoot,
    X : Entity>(factory: LazyFactory<X>) :
    BaseReadOnlyDelegate<T, EntityCollection<X>, CollectionReference>() {

  override val converter = CollectionConverter(factory)

  override fun load(thisRef: T, property: KProperty<*>): CollectionReference =
      thisRef.fireStore!!.collection(property.name)
}