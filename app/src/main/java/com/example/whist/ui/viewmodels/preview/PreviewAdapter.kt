package com.example.whist.ui.viewmodels.preview

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.whist.R
import com.example.whist.data.domain.Player
import com.example.whist.databinding.PlayerItemBinding

class PreviewAdapter() : RecyclerView.Adapter<PreviewHolder>() {

    var players: List<Player> = emptyList()
        set(value) {
            field = value
            Log.d("Data PreviewAdapter", "CALL")
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewHolder {
        val withDataBinding: PlayerItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            PreviewHolder.LAYOUT,
            parent,
            false
        )
        return PreviewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: PreviewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.players = players[position]
        }
    }

    override fun getItemCount(): Int = players.size
}

class PreviewHolder(val viewDataBinding: PlayerItemBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.player_item
    }

}