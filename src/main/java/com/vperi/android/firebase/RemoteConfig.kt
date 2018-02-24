package com.vperi.android.firebase

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.vperi.android.firebase.Firebase.remoteConfig

@Suppress("unused")
class RemoteConfig(defaults: Map<String, Any?> = HashMap()) {
  var developerMode = true
  var cacheExpiration: Long = 3600
  var fetched: Boolean = false

  init {
    val settings = FirebaseRemoteConfigSettings
        .Builder()
        .setDeveloperModeEnabled(developerMode)
        .build()

    with(remoteConfig) {
      setConfigSettings(settings)
//      setDefaults(R.xml.remote_config_defaults)
      setDefaults(defaults)
      if (info.configSettings.isDeveloperModeEnabled) {
        cacheExpiration = 0
      }

      fetch(cacheExpiration).promise
          .success {
            activateFetched()
            fetched = true
            Log.d(TAG, "Fetched remote config")
          }
          .fail {
            Log.e(TAG, it.toString())
          }
    }
  }

  inline operator fun <reified T : Any> get(key: String): T? =
      remoteConfig[key]

  companion object {
    private val TAG = RemoteConfig::class.java.simpleName
  }
}
