package com.example.rickandmortyapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.appcompat.widget.AppCompatTextView

class  ListAdapterCharacter(val context: Context, val list: ArrayList<Character>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:View = LayoutInflater.from(context).inflate(R.layout.character_layout,parent,false)

        val characterId = view.findViewById(R.id.character_id) as AppCompatTextView
        val characterName = view.findViewById(R.id.character_name) as AppCompatTextView
        val characterStatus = view.findViewById(R.id.character_status) as AppCompatTextView
        val characterSpecies = view.findViewById(R.id.character_species) as AppCompatTextView
        val characterType = view.findViewById(R.id.character_type) as AppCompatTextView
        val characterGender = view.findViewById(R.id.character_gender) as AppCompatTextView
        val characterImage = view.findViewById(R.id.character_img) as AppCompatTextView

        characterId.text = list[position].id.toString()
        characterName.text = list[position].name
        characterStatus.text = list[position].status
        characterSpecies.text = list[position].species
        characterType.text = list[position].type
        characterGender.text = list[position].gender
        characterImage.text = list[position].img

        return view
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }
}