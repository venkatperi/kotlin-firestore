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

import android.arch.lifecycle.LifecycleOwner
import com.google.firebase.firestore.*
import com.vperi.android.arch.lifecycle.AutoStartLifecycleHelper
import com.vperi.android.firebase.firestore.factory.FirestoreEntityFactory
import com.vperi.android.firebase.promise
import com.vperi.entity.CollectionChange
import com.vperi.entity.CollectionChanges
import com.vperi.entity.Entity
import com.vperi.entity.EntityCollection
import com.vperi.kotlin.Event
import com.vperi.promise.P
import nl.komponents.kovenant.Promise
import timber.log.Timber

open class FirestoreCollection<T : Entity>(
//    db: CollectionContainer,
//    name: String,
    collection: CollectionReference,
    factory: FirestoreEntityFactory<T>,
    owner: LifecycleOwner? = null) :
    BaseFirestoreCollection<T>(collection, factory, ArrayList<P<T>>()),
    EntityCollection<T> {

  private val lifecycle = AutoStartLifecycleHelper(owner)

  private val map = HashMap<String, P<T>>()

  //  val path: String  by lazy { "${db.path}/$name" }
  val path: String by lazy { collection.path }

//  private val collection by lazy { db.collection(name) }

  private fun document(id: String): DocumentReference =
      collection.document(id)

  override val onChanged = Event<CollectionChanges<String, P<T>>>()

  final override val onError = Event<Exception>()

  override fun create(): P<T>? =
      factory.createInstance(collection).apply {
        success { map[it.id] = this }
      }

  override fun findById(key: String): P<T>? {
    Timber.d("findById: $path/$key")
    return map.getOrPut(key, {
      factory.createInstance(collection, key)
    })
  }

  override fun deleteById(key: String): P<T>? {
    Timber.d("deleteById: $path/$key")
    val value = map.remove(key)!!
    document(key).delete().promise.fail {
      onError(it)
    }
    return value
  }

  override fun get(): P<Iterable<T>> {
    return query.get()
  }

  private fun onDocumentAdded(change: DocumentChange):
      List<CollectionChange<String, P<T>>> {
    val doc = change.document
    val item = map.getOrPut(doc.id, { createInstance(doc) })
    values.add(change.newIndex, item)
    return listOf(CollectionChange.Add(doc.id, item, change.newIndex, true))
  }

  private fun onDocumentRemoved(change: DocumentChange):
      List<CollectionChange<String, P<T>>> {
    val doc = change.document
    val item = map[doc.id]!!
    values.removeAt(change.newIndex)
    map.remove(doc.id)
    return listOf(CollectionChange.Remove(doc.id, item, change.newIndex, true))
  }

  private fun onDocumentModified(change: DocumentChange):
      List<CollectionChange<String, P<T>>> {
    val doc = change.document
    val item = map[doc.id]!!
    val index = change.newIndex
    val oldIndex = change.oldIndex
    val changes = arrayListOf<CollectionChange<String, P<T>>>()
    if (index != oldIndex) {
      values.removeAt(oldIndex)
      values.add(index, item)
      changes.add(CollectionChange.Move(doc.id, item, index, oldIndex, true))
    }
    changes.add(CollectionChange.Modify(doc.id, item, index, true))
    return changes
  }

  private val changeHandlers = mapOf(
      DocumentChange.Type.ADDED to ::onDocumentAdded,
      DocumentChange.Type.REMOVED to ::onDocumentRemoved,
      DocumentChange.Type.MODIFIED to ::onDocumentModified
  )

  private fun createInstance(doc: DocumentSnapshot): P<T> =
      Promise.of(factory.createInstance(doc))

  private fun snapshotListener(
      items: QuerySnapshot,
      e: FirebaseFirestoreException?) {
    when {
      e != null -> onError(e)
      !items.isEmpty -> {
        val changes = items.documentChanges.map {
          changeHandlers[it.type]!!.invoke(it)
        }.flatten()
        onChanged(changes)
      }
    }
  }

  init {
    lifecycle.onStart += { collection.addSnapshotListener(::snapshotListener) }
    onError += { Timber.e(it) }
  }
}