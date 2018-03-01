package com.vperi.android.firebase.firestore.entity

import com.google.firebase.firestore.CollectionReference
import com.vperi.android.firebase.firestore.LazyFactory
import com.vperi.promise.P
import com.vperi.store.entity.Entity

open class BaseFirestoreCollection<T : Entity> internal constructor(
    protected val collection: CollectionReference,
    protected val factory: LazyFactory<T>,
    protected val values: ArrayList<P<T>>) : MutableList<P<T>> by values,
    QueryImpl<T>(FirestoreQuery(collection, factory))