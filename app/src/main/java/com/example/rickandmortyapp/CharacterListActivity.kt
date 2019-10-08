package com.example.rickandmortyapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.character_main.*
import org.json.JSONArray
import org.json.JSONObject
import android.widget.BaseAdapter
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.AsyncTask
import com.android.volley.toolbox.JsonObjectRequest
import java.net.HttpURLConnection
import java.net.URL


class CharacterListActivity : AppCompatActivity() {

    private val listCharacter = ArrayList<Character>()
    private var residents = JSONArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.character_main)

        val id: Int = intent.getIntExtra("locationId", 0)
        val url = "https://rickandmortyapi.com/api/location/${id}"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                val stringresp = response.toString()
                val jsonObject : JSONObject = JSONObject(stringresp)
                residents  = JSONArray(jsonObject.getString("residents"))

                var x = 0
                while(x < residents.length())
                {
                    getResidents(residents[x].toString())
                    x++
                }
            },
            Response.ErrorListener {print("failed")}
        )
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)

        character_list.setOnItemClickListener { parent, view, position, id ->

            val selectedCharacter = parent.getItemAtPosition(position) as Character
            val intent = Intent(this, CharacterDetailActivity::class.java)
            intent.putExtra("character_extra", selectedCharacter)
            startActivity(intent)
        }

    }

    private fun getResidents(url : String?)
    {
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                val stringresp = response.toString()
                val jsonObject : JSONObject = JSONObject(stringresp)
                println(jsonObject.getString("name"))
                listCharacter.add(
                    Character(
                        jsonObject.getInt("id"),
                        jsonObject.getString("name"),
                        jsonObject.getString("status"),
                        jsonObject.getString("species"),
                        jsonObject.getString("type"),
                        jsonObject.getString("gender"),
                        jsonObject.getString("image")
                    )
                )
                val adapter = ListAdapterCharacter(this, listCharacter)
                character_list.adapter = adapter
            },
            Response.ErrorListener {print("failed")}
        )
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)
    }
}

