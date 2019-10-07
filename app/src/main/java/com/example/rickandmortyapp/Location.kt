package com.example.rickandmortyapp

import org.json.JSONArray

data class Location(val id : Int, val name: String, val type: String, val dimension: String, val residents: JSONArray)
