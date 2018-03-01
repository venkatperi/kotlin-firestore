package com.vperi.android.firebase.firestore

import com.vperi.android.firebase.firestore.factory.FirestoreEntityFactory

    /**
     * Created by venkat on 2/28/18.
     */

typealias LazyFactory<X> = Lazy<FirestoreEntityFactory<X>>