package com.ssafy.drumscometrue.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.drumscometrue.R
import com.ssafy.drumscometrue.kpop.Kpop

class kPopAdapter(val context: Context, val kpopList: ArrayList<Kpop>, val itemClick: (Kpop) -> Unit):
    RecyclerView.Adapter<kPopAdapter.Holder>() {

    inner class Holder(itemView: View?, itemClick: (Kpop) -> Unit): RecyclerView.ViewHolder(itemView!!) {
        private val songText = itemView?.findViewById<TextView>(R.id.tv_rv_song)
        private val singerText = itemView?.findViewById<TextView>(R.id.tv_rv_singer)
        private val songImage =	itemView?.findViewById<ImageView>(R.id.img_rv_photo)
        private val level = itemView?.findViewById<TextView>(R.id.tv_rv_level)

        fun bind(kpop: Kpop, context: Context) {
            if(kpop.image != ""){
                val resourceId = context.resources.getIdentifier(kpop.image, "drawable", context.packageName)
                songImage?.setImageResource(resourceId)
            }else{
                songImage?.setImageResource(R.mipmap.ic_launcher)
            }

            songText?.text = kpop.song
            singerText?.text = kpop.singer
            level?.text = kpop.level

            itemView?.setOnClickListener{itemClick(kpop)}

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.kpop_list_item, parent, false)
        return Holder(view, itemClick)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(kpopList[position], context)
    }

    override fun getItemCount(): Int {
        return kpopList.size
    }

}