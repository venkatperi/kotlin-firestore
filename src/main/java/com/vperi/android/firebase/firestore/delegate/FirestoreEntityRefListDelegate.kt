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

import com.google.firebase.firestore.DocumentReference
import com.lightningkite.kotlin.observable.list.ObservableListWrapper
import com.vperi.android.firebase.firestore.entity.FirestoreEntity
import com.vperi.android.firebase.firestore.entity.FirestoreEntityRef
import com.vperi.android.firebase.firestore.factory.FirestoreEntityFactory
import com.vperi.android.firebase.safeGet
import com.vperi.android.firebase.setWithTimestamp
import com.vperi.entity.Entity
import com.vperi.entity.EntityRef
import timber.log.Timber
import kotlin.reflect.KProperty

class FirestoreEntityRefListDelegate<
    in T : FirestoreEntity,
    X : Entity>(private val factory: FirestoreEntityFactory<X>) {

  private var list: ObservableListWrapper<EntityRef<X>>? = null

  operator fun getValue(thisRef: T, property: KProperty<*>):
      MutableList<EntityRef<X>> {

    with(thisRef) {
      if (list == null) {
        val prop = snapshot.safeGet(property.name) as Iterable<*>?
        val friends = prop?.map {
          FirestoreEntityRef(it as DocumentReference, factory) as EntityRef<X>
        }

        list = ObservableListWrapper((friends ?: listOf()).toMutableList())

        list!!.onUpdate += {
          Timber.d("updating EntityRef list")
          thisRef.document.setWithTimestamp(property.name, list!!.map {
            (it as FirestoreEntityRef<X>).ref
          })
        }
      }
    }
    return list!!
  }
}