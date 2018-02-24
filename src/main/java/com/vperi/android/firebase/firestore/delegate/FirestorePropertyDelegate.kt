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

import com.vperi.android.firebase.firestore.entity.FirestoreEntity
import com.vperi.android.firebase.promise
import com.vperi.android.firebase.safeGet
import com.vperi.android.firebase.setWithTimestamp
import timber.log.Timber
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class FirestorePropertyDelegate<in T : FirestoreEntity, R : Any?> :
    ReadWriteProperty<T, R> {

  operator fun provideDelegate(
      thisRef: T,
      prop: KProperty<*>): ReadWriteProperty<T, R> {

    with(thisRef) {
      entityChanged += {
        prop.name.let {
          val value = snapshot.safeGet(it)
          if (properties[it] != value) {
            try {
              Timber.d("updatingLocal: $it -> $value (${properties[it]}")
              properties[it] = value
              propertyChanged(it)
            } catch (e: Exception) {
              Timber.e(e)
            }
          }
        }
      }
    }
    return this
  }

  @Suppress("UNCHECKED_CAST")
  override operator fun getValue(thisRef: T, property: KProperty<*>): R =
      with(thisRef) {
        properties.getOrPut(property.name, {
          snapshot.safeGet(property.name)
        }) as R
      }

  override operator fun setValue(thisRef: T, property: KProperty<*>, value: R) {
    with(thisRef) {
      if (snapshot.safeGet(property.name) != value) {
        Timber.d("settingRemote: ${thisRef.path}.${property.name}=$value")
        document
            .setWithTimestamp(property.name, value)
            .promise.fail { thisRef.onError(it) }
      }

      if (properties[property.name] != value) {
        Timber.d("settingLocal: ${thisRef.path}.${property.name}=$value")
        properties[property.name] = value
        propertyChanged(property.name)
      }
    }
  }
}

