package com.example.whist.ui.viewmodels.room

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.whist.R
import com.example.whist.data.domain.TableGame
import com.example.whist.databinding.RoomItemBinding
import com.example.whist.ui.view.RoomFragment


class RoomAdapter(private val listener: OnItemClickListener) : //private val listener: OnItemClickListener
    RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {

    /**
     * Table Game that Adapter willl show
     */
    @LayoutRes
    val LAYOUT = R.layout.room_item

    var room: List<TableGame> = emptyList()
        set(value) {
            field = value
            Log.d("Data Changed", "CALL")
            notifyDataSetChanged()
        }

    inner class RoomViewHolder(val viewDataBinding: RoomItemBinding) : //, private val listener: RoomAdapter.OnItemClickListener
        RecyclerView.ViewHolder(viewDataBinding.root),View.OnClickListener { //, View.OnClickListener


        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }
    }

    override fun getItemCount(): Int = room.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val withDataBinding: RoomItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            LAYOUT,
            parent,
            false
        )
        return RoomViewHolder(withDataBinding) //,listener
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.room = room[position]
        }
    }
    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }
}
