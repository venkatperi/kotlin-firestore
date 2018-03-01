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

package com.vperi.store.entity

import com.vperi.kotlin.Event
import java.util.*

/**
 * Base from which Firestore entities derive
 *
 */
interface Entity {

  /**
   * @property String The unique id of this entity
   */
  var id: String

  /**
   * @property Boolean true if this entity exists in the Firestore collection
   */
  val exists: Boolean

  /**
   * @property createdOn date when this record was created
   */
  val createdOn: Date?

  /**
   * @property updatedOn date when this record was modified
   */
  val updatedOn: Date?

  fun <T : Entity> getReference(klass: Class<T>): EntityRef<T>

  val propertyChanged: Event<String>
  val entityChanged: Event<Void>
  val onError: Event<Exception>
}

