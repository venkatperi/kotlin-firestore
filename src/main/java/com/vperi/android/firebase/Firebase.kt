package com.vperi.android.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

@Suppress("unused")
/**
 * Created by venkat on 2/15/18.
 */

object Firebase {
  val instanceId: FirebaseInstanceId
    get() = FirebaseInstanceId.getInstance()

  val fcm: FirebaseMessaging
    get() = FirebaseMessaging.getInstance()

  val store: FirebaseFirestore
    get() = FirebaseFirestore.getInstance()

  val auth: FirebaseAuth
    get() = FirebaseAuth.getInstance()

  val remoteConfig: FirebaseRemoteConfig
    get() = FirebaseRemoteConfig.getInstance()

  val currentUser: FirebaseUser?
    get() = auth.currentUser

  val isAuthenticated: Boolean
    get() = currentUser != null
}