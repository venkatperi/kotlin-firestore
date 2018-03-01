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

import android.annotation.SuppressLint
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.KodeinAware
import com.github.salomonbrys.kodein.instance
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.vperi.android.firebase.*
import com.vperi.android.firebase.firestore.delegate.FirestoreNullablePropertyDelegate
import com.vperi.android.firebase.firestore.delegate.FirestorePropertyDelegate
import com.vperi.android.firebase.firestore.factory.FirestoreEntityFactory
import com.vperi.kotlin.Event
import com.vperi.store.FirestoreEntityManager
import com.vperi.store.entity.Entity
import com.vperi.store.entity.EntityRef
import timber.log.Timber
import java.util.*
import java.util.concurrent.ConcurrentHashMap

abstract class FirestoreEntity(snap: DocumentSnapshot) :
    Entity, KodeinAware {

  final override val kodein: Kodein by lazy {
    FirestoreEntityManager.instance.kodein
  }

  private var snapshotHash: Int = snap.dataHashCode

  private val pending = HashMap<String, Any?>()

  private var _snapshot = snap
  var snapshot: DocumentSnapshot
    get() = _snapshot
    @SuppressLint("BinaryOperationInTimber")
    set(value) {
      val newHash = value.dataHashCode
      if (snapshotHash == newHash) {
        Timber.d("$path: no changes detected")
        return
      }
      Timber.d("Changed: $path($snapshotHash/$newHash): " +
          "${value.safeData}")
      _snapshot = value
      snapshotHash = newHash
      entityChanged()
    }

  val document: DocumentReference
    get() = snapshot.reference

  val path: String
    get() = document.path

  override fun <T : Entity> getReference(klass: Class<T>): EntityRef<T> =
      FirestoreEntityRef(document,
          lazy {
            kodein.instance<FirestoreEntityFactory<T>>(klass)
          })

  final override val exists
    get() = snapshot.exists()

  internal val properties = ConcurrentHashMap<String, Any?>()

  final override val onError = Event<Exception>()

  override val propertyChanged = Event<String>()

  override val entityChanged = Event<Void>()

  final override var id: String by FirestorePropertyDelegate()

  final override val updatedOn: Date? by FirestoreNullablePropertyDelegate()

  final override val createdOn: Date? by FirestoreNullablePropertyDelegate()

  fun refreshPropertyFromRemote(name: String) {
    properties.remove(name)
  }

  private fun sendPendingChanges() {
    document.setWithTimestamp(pending)
        .promise.fail { onError(it) }
    pending.clear()
  }

  fun remoteSetProperty(name: String, value: Any?) {
    if (snapshot.safeGet(name) != value) {
      Timber.d("remoteSet: $path.$name=$value")
      pending[name] = value
      sendPendingChanges()
    }
  }

  fun localSetProperty(name: String, value: Any?, refresh: Boolean = false) {
    if (value != null && properties[name] != value) {
      Timber.d("localSet($refresh): $path.$name=$value")
      properties[name] = value
      propertyChanged(name)
    }
  }

  init {
    onError += { Timber.e(it) }

    when {
      !exists -> pending["createdOn"] = FieldValue.serverTimestamp()
    }

    document.addSnapshotListener({ s, e ->
      when {
        e != null -> onError(e)
        s?.exists()!! -> snapshot = s
      }
    })
  }
}

