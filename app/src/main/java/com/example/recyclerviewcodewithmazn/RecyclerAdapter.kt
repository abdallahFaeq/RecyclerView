package com.example.recyclerviewcodewithmazn

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(counteries:MutableList<String>) :RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>(){
    var countriesList = counteries
    inner class RecyclerHolder(itemView:View) :RecyclerView.ViewHolder(itemView){
        lateinit var title:TextView
        init {
            title = itemView.findViewById(R.id.title)

            itemView.setOnClickListener({
                val position = adapterPosition
                Toast.makeText(itemView.context,"you click here ${countriesList[position]}",Toast.LENGTH_SHORT).show()
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
        return RecyclerHolder(LayoutInflater.from(parent.context).inflate(R.layout.counteries_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        holder.title.text = countriesList[position]
    }

    override fun getItemCount(): Int {
        return countriesList.size
    }
}