package com.vperi.android.firebase.firestore.entity

import com.vperi.store.entity.Entity
import com.vperi.store.entity.Query

open class QueryImpl<T : Entity> internal constructor(
    protected val query: Query<T>) : Query<T> by query