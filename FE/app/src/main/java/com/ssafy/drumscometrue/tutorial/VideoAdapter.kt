package com.ssafy.drumscometrue.tutorial

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

import com.ssafy.drumscometrue.R

class VideoAdapter(private val videoItems: List<VideoItem>) :
    RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val youTubePlayerView: YouTubePlayerView = itemView.findViewById(R.id.youtube_video)
        val tutorDescription: TextView = itemView.findViewById(R.id.tutor_description)
        val tutorSubtitle: TextView = itemView.findViewById(R.id.tutor_subtitle)
        val tutorTitle: TextView = itemView.findViewById(R.id.tutor_title)
        val tutorImage: ImageView = itemView.findViewById(R.id.tutor_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_video_page, parent, false)
        return VideoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val videoItem = videoItems[position]
        holder.tutorImage.setImageResource(videoItem.image)
        holder.tutorDescription.text = videoItem.description
        holder.tutorSubtitle.text = videoItem.subtitle
        holder.tutorTitle.text = videoItem.title

        holder.youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.cueVideo(videoItem.videoId, 0f)
            }
        })
    }

    override fun getItemCount(): Int {
        return videoItems.size
    }


}