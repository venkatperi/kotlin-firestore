package com.vperi.android.firebase.firestore.entity

import com.google.firebase.firestore.CollectionReference
import com.vperi.android.firebase.firestore.factory.FirestoreEntityFactory
import com.vperi.entity.Entity
import com.vperi.promise.P

open class BaseFirestoreCollection<T : Entity> internal constructor(
    protected val collection: CollectionReference,
    protected val factory: FirestoreEntityFactory<T>,
    protected val values: ArrayList<P<T>>) : MutableList<P<T>> by values,
    QueryImpl<T>(FirestoreQuery(collection, factory))