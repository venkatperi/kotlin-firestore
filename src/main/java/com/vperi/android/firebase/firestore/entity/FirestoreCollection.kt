/*
 * Copyright 2018 Venkat Peri. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vperi.android.firebase.firestore.entity

import android.os.Build
import android.support.annotation.RequiresApi
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentReference
import com.vperi.android.firebase.firestore.factory.FirestoreEntityFactory
import com.vperi.android.firebase.promise
import com.vperi.entity.CollectionChange
import com.vperi.entity.CollectionChanges
import com.vperi.entity.Entity
import com.vperi.entity.EntityCollection
import com.vperi.kotlin.Event
import com.vperi.promise.P
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.then
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.N)
open class FirestoreCollection<T : Entity>(
    private val db: DbRef,
    private val name: String,
    private val factory: FirestoreEntityFactory<T>) :
    EntityCollection<T> {

  val path: String  by lazy { "${db.path}/$name" }

  private val map = HashMap<String, P<T>>()

  private val collection by lazy { db.collection(name) }

  private fun document(id: String): DocumentReference =
      collection.document(id)

  override val collectionChanged = Event<CollectionChanges<String, P<T>>>()

  final override val onError = Event<Exception>()

  override val size: Int get() = map.size
  override fun containsKey(key: String): Boolean = map.containsKey(key)
  override fun containsValue(value: P<T>): Boolean = map.containsValue(value)
  override fun isEmpty(): Boolean = map.isEmpty()
  override val entries get() = map.entries
  override val keys: MutableSet<String> get() = map.keys
  override val values: MutableCollection<P<T>> get() = map.values
  override fun clear() = map.clear()

  override operator fun get(key: String): P<T>? {
    Timber.d("get: $path/$key")
    return map.getOrPut(key, {
      Timber.d("$path/$key not found. Fetching.")
      factory.createInstance(collection, key)
    })
  }

  @RequiresApi(Build.VERSION_CODES.N)
  override fun put(key: String, value: P<T>): P<T>? {
    val change = actuallyPut(key, value)
    collectionChanged(listOf(change))
    return change.old
  }

  @RequiresApi(Build.VERSION_CODES.N)
  override fun putAll(from: Map<out String, P<T>>) =
      collectionChanged(from.map { actuallyPut(it.key, it.value) })

  override fun remove(key: String): P<T>? {
    Timber.d("remove: $path/$key")
    val value = map.remove(key)!!
    document(key).delete().promise.fail {
      onError(it)
    }
    emitChange(key, value, null, CollectionChange.Type.REMOVED)
    return value
  }

  @RequiresApi(Build.VERSION_CODES.N)
  override fun getAll(): P<Iterable<T>> {
    Timber.d("getAll: $path")
    return collection.get().promise
        .then { it.map(factory::createInstance) }
        .then {
          it.forEach { put(it.id!!, Promise.of(it)) }
          it
        }.fail { onError(it) }
  }

  private fun actuallyPut(key: String, value: P<T>):
      CollectionChange<String, P<T>> {
    val type = when (key in map) {
      true -> CollectionChange.Type.REPLACED
      else -> CollectionChange.Type.ADDED
    }
    return CollectionChange(key, value, map.put(key, value), type)
  }

  private fun emitChange(key: String, value: P<T>? = null,
      old: P<T>? = null, type: CollectionChange.Type) =
      collectionChanged(listOf(
          CollectionChange(key, value, old, type)))

  init {
    onError += { Timber.e(it) }

    collection.addSnapshotListener({ items, e ->
      when {
        e != null -> onError(e)

        !items.isEmpty -> {
          items.documentChanges.forEach {
            Timber.d(
                "got upstream msg: ${it.type.name} for $path/${it.document.id}")
            when (it.type) {
              DocumentChange.Type.REMOVED -> remove(it.document.id)

              DocumentChange.Type.ADDED -> when {
                it.document.id in map -> Timber.d(
                    "Ignoring ADDED. Already in collection: ${it.document.id}")
                else -> put(it.document.id,
                    Promise.of(factory.createInstance(it.document)))
              }

              DocumentChange.Type.MODIFIED ->
                emitChange(key = it.document.id,
                    type = CollectionChange.Type.MODIFIED)
            }
          }
        }
      }
    })
  }
}