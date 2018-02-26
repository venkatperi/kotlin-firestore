package com.vperi.android.firebase.firestore.entity

import com.vperi.entity.Entity
import com.vperi.entity.Query

open class QueryImpl<T : Entity> internal constructor(
    protected val query: Query<T>) : Query<T> by query