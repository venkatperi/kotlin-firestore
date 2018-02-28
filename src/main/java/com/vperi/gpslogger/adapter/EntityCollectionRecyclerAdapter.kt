package com.vperi.gpslogger.adapter

import android.support.v7.widget.RecyclerView
import com.vperi.entity.CollectionChange
import com.vperi.entity.CollectionChanges
import com.vperi.entity.Entity
import com.vperi.entity.EntityCollection
import com.vperi.promise.P

abstract class EntityCollectionRecyclerAdapter<
    in T : Entity,
    VH : EntityViewHolder<T>>(
    private val collection: EntityCollection<T>) :
    RecyclerView.Adapter<VH>() {

  override fun getItemCount(): Int =
      collection.size

  override fun onBindViewHolder(holder: VH, position: Int) =
      holder.bind(collection[position])

  private fun onChanged(changes: CollectionChanges<String, P<T>>?) {
    changes?.forEach {
      when (it) {
        is CollectionChange.Add -> notifyItemInserted(it.index)
        is CollectionChange.Remove -> notifyItemRemoved(it.index)
        is CollectionChange.Modify -> notifyItemChanged(it.index)
        is CollectionChange.Move -> notifyItemMoved(it.oldIndex, it.index)
      }
    }
  }

  init {
    collection.onChanged += ::onChanged
//    collection.onChanged -= ::onChanged
  }
}
