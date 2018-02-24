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
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.vperi.android.firebase.dataHashCode
import com.vperi.android.firebase.firestore.delegate.FirestorePropertyDelegate
import com.vperi.android.firebase.safeData
import com.vperi.entity.Entity
import com.vperi.kotlin.Event
import timber.log.Timber
import java.util.Date
import java.util.concurrent.ConcurrentHashMap

abstract class FirestoreEntity(snap: DocumentSnapshot) : Entity {

  private var snapshotHash: Int = snap.dataHashCode

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

  override val exists
    get() = snapshot.exists()

  internal val properties = ConcurrentHashMap<String, Any?>()

  final override val onError = Event<Exception>()

  override val propertyChanged = Event<String>()

  override val entityChanged = Event<Void>()

  final override var id: String by FirestorePropertyDelegate()

  final override var updatedOn: Date? by FirestorePropertyDelegate()

  init {
    onError += { Timber.e(it) }

    document.addSnapshotListener({ s, e ->
      when {
        e != null -> onError(e)
        s?.exists()!! -> snapshot = s
      }
    })
  }
}

