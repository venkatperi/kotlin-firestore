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

import com.vperi.kotlin.Event
import com.vperi.promise.P

typealias CollectionChanges<T, U> = Iterable<CollectionChange<T, U>>

interface EntityCollection<T> : MutableMap<String, P<T>> {

  fun getAll(): P<Iterable<T>>

  val collectionChanged: Event<CollectionChanges<String, P<T>>>

  val onError: Event<Exception>
}

