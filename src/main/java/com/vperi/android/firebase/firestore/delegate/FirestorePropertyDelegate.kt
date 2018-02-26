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

import com.vperi.android.firebase.firestore.converter.PassthroughConverter
import com.vperi.android.firebase.firestore.entity.FirestoreEntity

open class FirestorePropertyDelegate<in T : FirestoreEntity, R> :
    ReadWritePropertyDelegate<T, R, R>() {

  override val converter = PassthroughConverter<R, R>()
}

