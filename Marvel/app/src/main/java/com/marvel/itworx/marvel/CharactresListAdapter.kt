package com.marvel.itworx.marvel

import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.marvel.itworx.marvel.data.Character

class CharactersListAdapter(private val charactersList: ArrayList<Character>) :
        RecyclerView.Adapter<CharactersListAdapter.CharacterViewHolder>() {

    var characterListRecyclerViewAdapterListener: CharacterListRecyclerViewAdapterListener? = null

    class CharacterViewHolder(mainView: View) : RecyclerView.ViewHolder(mainView) {
        val characterThumbnail: ImageView = mainView.findViewById(R.id.characterThumbnailImageView)
        val characterNameTextView: TextView = mainView.findViewById(R.id.characterNameTextView)
        val characterDescriptionTextView: TextView = mainView.findViewById(R.id.characterDescriptionTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val mainView = LayoutInflater.from(parent.context).inflate(R.layout.character_item, parent, false) as View
        return CharacterViewHolder(mainView)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = charactersList[position]
        holder.characterNameTextView.text = character.characterName
        holder.characterDescriptionTextView.text = character.characterDescription
        var characterImagePath = "${character.characterThumbnailPath}/portrait_small.${character.characterThumbnailExtension}"
        if (characterImagePath != null) {
            Glide.with(holder.characterThumbnail.context)
                    .load(characterImagePath)
                    .into(holder.characterThumbnail)
        } else {
            holder.characterThumbnail.setImageDrawable(holder.characterThumbnail.context.getDrawable(R.mipmap.ic_placeholder))
        }
        holder.itemView.setOnClickListener {
            if (characterListRecyclerViewAdapterListener != null) {
                characterListRecyclerViewAdapterListener!!.onItemClick(character.characterID)
            }
        }
    }

    override fun getItemCount() = charactersList.size
}


interface CharacterListRecyclerViewAdapterListener {
    fun onItemClick(characterID: Int)
}