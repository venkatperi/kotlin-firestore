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
import com.vperi.android.firebase.firestore.converter.EntityRefConverter
import com.vperi.android.firebase.firestore.entity.FirestoreEntity
import com.vperi.android.firebase.firestore.factory.FirestoreEntityFactory
import com.vperi.entity.Entity
import com.vperi.entity.EntityRef

class FirestoreEntityRefDelegate<
    in T : FirestoreEntity, X : Entity>(
    factory: FirestoreEntityFactory<X>) :
    ReadWritePropertyDelegate<T, EntityRef<X>, DocumentReference>() {

  override val converter = EntityRefConverter(factory)

}


