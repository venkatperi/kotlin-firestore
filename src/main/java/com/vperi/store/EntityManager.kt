package com.vperi.store

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.conf.ConfigurableKodein
import com.vperi.android.firebase.firestore.factory.FirestoreEntityFactory
import com.vperi.store.entity.Entity

typealias BuilderConfig = Kodein.Builder.() -> Unit

class FirestoreEntityManager private constructor() {

  private var freezeConfig = false
  private val factories = ArrayList<Pair<FirestoreEntityFactory<*>, Class<*>>>()
  private val configs = ArrayList<BuilderConfig>()
//  val kodein by lazy { ConfigurableKodein() }

  val kodein by lazy {
    Kodein {
      //      factories.forEach { bind(it.second) from singleton { it.first } }
      configs.forEach {
        it()
      }
    }
  }

  fun configure(block: BuilderConfig) {
    configs.add(block)
  }

  fun <T : Entity> registerFactory(klass: Class<T>, factory: FirestoreEntityFactory<T>) {
    factories.add(factory to klass)
//    if (freezeConfig)
//      throw IllegalStateException("Config is frozen")

//    kodein.addConfig {
    //      bind<FirestoreEntityFactory<T>>(klass) with singleton { factory }
//    }
  }

  companion object {
    val instance = FirestoreEntityManager()
  }
}

