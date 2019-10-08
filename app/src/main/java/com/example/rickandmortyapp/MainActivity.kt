package com.example.rickandmortyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.bottom_navigation
import kotlinx.android.synthetic.main.character_main.*
import org.json.JSONArray
import org.json.JSONObject
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.os.bundleOf


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        VolleySingleton.getInstance(this).requestQueue
        val url = "https://rickandmortyapi.com/api/location"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                var stringresp = response.toString()
                val jsonObject : JSONObject = JSONObject(stringresp)
                val locations : JSONArray = JSONArray(jsonObject.getString("results"))
                handleJson(locations)
            },
            Response.ErrorListener {print("failed")}
        )

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)

        locations_list.setOnItemClickListener { parent, view, position, id ->
            val selectedLocation = parent.getItemAtPosition(position) as Location
            val intent = Intent(this, CharacterListActivity::class.java)
            intent.putExtra("location", selectedLocation)
            startActivity(intent)
        }

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> {
                    println("home")
                }
                R.id.action_favorites -> {
                    val intent = Intent(this, CharacterListActivity::class.java)
                    intent.putExtra("displayFav", true)
                    startActivity(intent)
                }
                R.id.action_add -> {
                    println("add")
                }
            }
            return@setOnNavigationItemSelectedListener true
        }

    }

    private fun handleJson(locations : JSONArray?)
    {
        val list = ArrayList<Location>()
        if(locations !== null)
        {
            var x = 0
            while (x < locations.length()) {
                val jsonObject = locations.getJSONObject(x)
                list.add(
                    Location(
                    jsonObject.getInt("id"),
                        jsonObject.getString("name"),
                        jsonObject.getString("type"),
                        jsonObject.getString("dimension")
                        )
                )
                x ++
            }

            val adapter = ListAdapterLocation(this, list)
            locations_list.adapter = adapter
        }
    }

}
