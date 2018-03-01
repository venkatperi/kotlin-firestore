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

import com.google.firebase.firestore.DocumentReference
import com.vperi.android.firebase.firestore.LazyFactory
import com.vperi.promise.P
import com.vperi.store.entity.Entity
import com.vperi.store.entity.EntityRef
import java.util.*

class FirestoreEntityRef<out T : Entity>(
    internal val ref: DocumentReference,
    private val factory: LazyFactory<T>
) : EntityRef<T> {

  override val path: String by lazy { ref.path }

  override fun get(): P<T> = factory.value.createInstance(ref)

  override fun equals(other: Any?): Boolean =
      when {
        other == null -> false
        other !is EntityRef<*> -> false
        path == other.path -> true
        else -> false
      }

  override fun hashCode(): Int = Objects.hash(path)
}