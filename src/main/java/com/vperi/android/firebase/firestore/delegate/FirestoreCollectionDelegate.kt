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

package com.vperi.android.firebase.firestore.delegate

import com.vperi.android.firebase.firestore.entity.DbRef
import com.vperi.android.firebase.firestore.entity.FirestoreCollection
import com.vperi.android.firebase.firestore.entity.FirestoreRoot
import com.vperi.android.firebase.firestore.factory.FirestoreEntityFactory
import com.vperi.entity.Entity
import com.vperi.entity.EntityCollection
import timber.log.Timber
import kotlin.reflect.KProperty

class FirestoreCollectionDelegate<
    in T : FirestoreRoot,
    V : Entity>
(private val factory: FirestoreEntityFactory<V>) {

  private var value: EntityCollection<V>? = null

  operator fun getValue(thisRef: T, prop: KProperty<*>):
      EntityCollection<V> {
    Timber.d("getValue: $thisRef")
    if (value == null) {
      value = FirestoreCollection(
          DbRef.Root(thisRef.fireStore!!),
          prop.name,
          factory)
    }
    return value!!
  }
}