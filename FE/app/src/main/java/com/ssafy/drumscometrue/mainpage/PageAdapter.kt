package com.ssafy.drumscometrue.mainpage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.drumscometrue.R

class PageAdapter(
    private val pages: List<PageData>
) : RecyclerView.Adapter<PageAdapter.PageViewHolder>() {

    // 클릭 이벤트를 처리할 리스너를 정의합니다.
    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.page_card_item, parent, false)
        return PageViewHolder(itemView)
    }

    override fun getItemCount(): Int = pages.size

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.bind(pages[position])

        // 카드를 클릭했을 때 리스너 호출
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(position)
        }
    }

    class PageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val pageImageView: ImageView = itemView.findViewById(R.id.page_image)
        private val pageTitleView: TextView = itemView.findViewById(R.id.page_title)
//        private val pageDescriptionView: TextView = itemView.findViewById(R.id.page_description)

        fun bind(page: PageData) {
            pageImageView.setImageResource(page.img)
            pageTitleView.text = page.title
//            pageDescriptionView.text = page.description
        }
    }
}