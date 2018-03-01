package com.vperi.store.entity

import com.vperi.promise.P

@Suppress("unused")
interface Query<T : Entity> {
  fun whereEqualTo(field: String, value: Any): Query<T>

  fun whereLessThan(field: String, value: Any): Query<T>

  fun whereLessThanOrEqualTo(field: String, value: Any): Query<T>

  fun whereGreaterThan(field: String, value: Any): Query<T>

  fun whereGreaterThanOrEqualTo(field: String, value: Any): Query<T>

  fun orderBy(field: String, ascending: Boolean = true): Query<T>

  fun limit(count: Long): Query<T>

  fun startAt(vararg fieldValues: Any): Query<T>

  fun startAfter(vararg fieldValues: Any): Query<T>

  fun endBefore(vararg fieldValues: Any): Query<T>

  fun endAt(vararg fieldValues: Any): Query<T>

  fun get(): P<Iterable<T>>

}
