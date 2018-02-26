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

package com.vperi.entity

typealias CollectionChanges<T, U> = Iterable<CollectionChange<T, U>>

sealed class CollectionChange<out K, out V>(
    val id: K,
    val item: V,
    val index: Int,
    val upstream: Boolean) {

  class Add<out K, out V>(id: K, item: V,
      index: Int, upstream: Boolean = false) :
      CollectionChange<K, V>(id, item, index, upstream)

  class Modify<out K, out V>(id: K, item: V,
      index: Int, upstream: Boolean = false) :
      CollectionChange<K, V>(id, item, index, upstream)

  class Remove<out K, out V>(id: K, item: V,
      index: Int, upstream: Boolean = false) :
      CollectionChange<K, V>(id, item, index, upstream)

  class Move<out K, out V>(id: K, item: V,
      index: Int, val oldIndex: Int,
      upstream: Boolean = false) :
      CollectionChange<K, V>(id, item, index, upstream)
}