package com.vperi.android.firebase.firestore.entity

import com.vperi.android.firebase.firestore.LazyFactory
import com.vperi.android.firebase.promise
import com.vperi.promise.P
import com.vperi.store.entity.Entity
import com.vperi.store.entity.Query
import nl.komponents.kovenant.then

class FirestoreQuery<T : Entity>(
    private var query: com.google.firebase.firestore.Query,
    private val factory: LazyFactory<T>
) : Query<T> {

  override fun whereEqualTo(field: String, value: Any): Query<T> {
    query = query.whereEqualTo(field, value)
    return this
  }

  override fun whereLessThan(field: String, value: Any): Query<T> {
    query = query.whereLessThan(field, value)
    return this
  }

  override fun whereLessThanOrEqualTo(field: String, value: Any): Query<T> {
    query = query.whereLessThanOrEqualTo(field, value)
    return this
  }

  override fun whereGreaterThan(field: String, value: Any): Query<T> {
    query = query.whereGreaterThan(field, value)
    return this
  }

  override fun whereGreaterThanOrEqualTo(field: String, value: Any): Query<T> {
    query = query.whereGreaterThanOrEqualTo(field, value)
    return this
  }

  override fun orderBy(field: String, ascending: Boolean): Query<T> {
    val dir = when {
      ascending -> com.google.firebase.firestore.Query.Direction.ASCENDING
      else -> com.google.firebase.firestore.Query.Direction.DESCENDING
    }
    query = query.orderBy(field, dir)
    return this
  }

  override fun limit(count: Long): Query<T> {
    query = query.limit(count)
    return this
  }

  override fun startAt(vararg fieldValues: Any): Query<T> {
    query = query.startAt(fieldValues)
    return this
  }

  override fun startAfter(vararg fieldValues: Any): Query<T> {
    query = query.startAfter(fieldValues)
    return this
  }

  override fun endBefore(vararg fieldValues: Any): Query<T> {
    query = query.endBefore(fieldValues)
    return this
  }

  override fun endAt(vararg fieldValues: Any): Query<T> {
    query = query.endAt(fieldValues)
    return this
  }

  override fun get(): P<Iterable<T>> {
    return query.get().promise
        .then {
          it.map {
            factory.value.createInstance(it)
          }
        }
  }
}