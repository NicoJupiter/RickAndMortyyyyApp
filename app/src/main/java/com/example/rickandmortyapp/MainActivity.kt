package com.example.rickandmortyapp

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val queue = Volley.newRequestQueue(this)
        val url = "https://rickandmortyapi.com/api/location"


        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                var stringresp = response.toString()
                val jsonObject : JSONObject = JSONObject(stringresp)
                val locations : JSONArray = JSONArray(jsonObject.getString("results"))
                handleJson(locations)
            },
            Response.ErrorListener {print("failed")})
        // Add the request to the RequestQueue.
        queue.add(stringRequest)
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
                        jsonObject.getString("dimension"))
                )
                x ++
            }
            val adapter = ListAdapte(this, list)
            locations_list.adapter = adapter
        }
    }

}
