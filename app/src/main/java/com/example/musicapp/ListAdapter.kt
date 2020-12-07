package com.example.musicapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*

class ListAdapter(private val exampleList: ArrayList<String>, private val listener: OnItemClickListener
) : RecyclerView.Adapter<ListAdapter.ListViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.list_item,
                parent, false
        )
        return ListViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = exampleList[position]
        holder.songname.text= currentItem
    }
    override fun getItemCount() = exampleList.size

   inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {
        var songname: TextView = itemView.song_list

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?)
        {
            val position = adapterPosition
            if(position!=RecyclerView.NO_POSITION)
            {
                listener.onItemClick(position)
            }
        }
    }

        interface OnItemClickListener {
            fun onItemClick(position: Int)
          }

}
