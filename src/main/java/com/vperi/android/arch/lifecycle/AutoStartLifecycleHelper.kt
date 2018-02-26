package com.vperi.android.arch.lifecycle

import android.arch.lifecycle.LifecycleOwner

class AutoStartLifecycleHelper(owner: LifecycleOwner?) :
    LifecycleHelper(owner, true)