@file:Suppress("unused")

package com.vperi.android.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.vperi.kotlin.LazyWithReceiver
import com.vperi.promise.D
import com.vperi.promise.P

val <T : Any?> Task<T>.promise: P<T> by LazyWithReceiver<Task<T>, P<T>> {
  D<T>().apply {
    addOnCompleteListener {
      when {
        it.isSuccessful -> resolve(it.result)
        else -> reject(it.exception!!)
      }
    }
  }.promise
}

inline operator fun <reified T : Any?> FirebaseRemoteConfig.get(
    key: String): T? =
    when (T::class) {
      String::class -> getString(key) as T?
      Boolean::class -> getBoolean(key) as T?
      Double::class -> getDouble(key) as T?
      Long::class -> getLong(key) as T?
      ByteArray::class -> getByteArray(key) as T?
      else -> throw UnsupportedOperationException("Not yet implemented")
    }

fun DocumentSnapshot.safeGet(key: String): Any? =
    safeData?.get(key)

fun DocumentSnapshot.safeContains(key: String): Boolean =
    safeData?.contains(key) ?: false

val DocumentSnapshot.safeData: Map<String, Any>?
  get() = when {
    exists() -> data
    else -> null
  }

val DocumentSnapshot.dataHashCode: Int
  get() = safeData?.hashCode() ?: -1

fun DocumentReference.setWithTimestamp(key: String, value: Any?): Task<Void> {
  val data = hashMapOf(key to value)
  return setWithTimestamp(data)
}

fun DocumentReference.setWithTimestamp(data: HashMap<String, Any?>): Task<Void> {
  data["updatedOn"] = FieldValue.serverTimestamp()
  return this.set(data, SetOptions.merge())
}

