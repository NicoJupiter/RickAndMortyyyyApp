package com.example.rickandmortyapp

import java.io.Serializable

data class Character(val id: Int, val name: String, val status: String, val species: String, val type: String, val gender: String, val urlImg: String) : Serializable