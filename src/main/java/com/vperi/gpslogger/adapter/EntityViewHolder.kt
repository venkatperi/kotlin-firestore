package com.vperi.gpslogger.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.vperi.entity.Entity
import com.vperi.promise.P

abstract class EntityViewHolder<in T : Entity>(view: View) :
    RecyclerView.ViewHolder(view) {
  abstract fun bind(item: P<T>)
}