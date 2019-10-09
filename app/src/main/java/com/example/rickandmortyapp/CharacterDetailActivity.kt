package com.example.rickandmortyapp

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.character_detail_layout.*
import kotlinx.android.synthetic.main.character_main.*

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

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("displayFav", false)
                    startActivity(intent)
                }
                R.id.action_favorites -> {
                    val intent = Intent(this, CharacterListActivity::class.java)
                    intent.putExtra("displayFav", true)
                    startActivity(intent)
                }
                R.id.action_favorites_loc -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("displayFav", true)
                    startActivity(intent)
                }
            }
            return@setOnNavigationItemSelectedListener true
        }

    }

}