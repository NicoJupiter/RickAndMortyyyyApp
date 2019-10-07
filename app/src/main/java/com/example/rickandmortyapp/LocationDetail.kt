package com.example.rickandmortyapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class LocationDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val queue = VolleySingleton.getInstance(this).requestQueue

        val id: Int = intent.getIntExtra("locationId", 0)
        val url = "https://rickandmortyapi.com/api/location/${id}"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                var stringresp = response.toString()
                println(stringresp)

            },
            Response.ErrorListener {print("failed")}
        )

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)

    }
}

