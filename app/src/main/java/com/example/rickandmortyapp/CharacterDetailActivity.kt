package com.example.rickandmortyapp

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.character_detail_layout.*

class CharacterDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.character_detail_layout)
        val character = intent.getSerializableExtra("character_extra") as Character

        character_detail_name.text = character.name
        character_detail_status.text = character.status
        character_detail_species.text = character.species
        character_detail_type.text = character.type
        character_detail_gender.text = character.gender

        Picasso.get().load(character.urlImg).into(character_detail_img)


    }

}