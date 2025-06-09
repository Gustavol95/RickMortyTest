package com.glopezsanchez.rickmortytest.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.placeholder
import com.glopezsanchez.rickmortytest.R
import com.glopezsanchez.rickmortytest.databinding.ListItemCharacterBinding
import com.glopezsanchez.rickmortytest.domain.model.Character

class CharacterAdapter(private val onClick: (Character) -> Unit) :
    PagingDataAdapter<Character, CharacterAdapter.CharacterViewHolder>(CharacterDiffCallback) {

    class CharacterViewHolder(itemView: View, val onClick: (Character) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private var currentCharacter: Character? = null
        private val binding = ListItemCharacterBinding.bind(itemView)

        init {
            itemView.setOnClickListener {
                currentCharacter?.let {
                    onClick(it)
                }
            }
        }

        fun bind(character: Character) {
            currentCharacter = character
            binding.characterName.text = character.name
            binding.characterImage.load(character.picture) {
                placeholder(R.drawable.ic_loading)
            }
            binding.characterStatus.text = character.status
            binding.locationText.text = character.location
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_character, parent, false)
        return CharacterViewHolder(view, onClick)
    }


    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = getItem(position)
        if (character != null)
            holder.bind(character)

    }
}

object CharacterDiffCallback : DiffUtil.ItemCallback<Character>() {
    override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem.id == newItem.id
    }
}