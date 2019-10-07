package com.example.rickandmortyapp

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
import java.net.HttpURLConnection
import java.net.URL


class CharacterListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.character_main)

        val id: Int = intent.getIntExtra("locationId", 0)
        val url = "https://rickandmortyapi.com/api/location/${id}"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                var stringresp = response.toString()
                val jsonObject : JSONObject = JSONObject(stringresp)
                val residents : JSONArray = JSONArray(jsonObject.getString("residents"))
                var x = 0
                while (x < residents.length())
                {
                    AsyncTaskHandleJson().execute(residents[x].toString())
                    x++
                }
               // getResidents(residents)
            },
            Response.ErrorListener {print("failed")}
        )

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)

    }

    inner class AsyncTaskHandleJson : AsyncTask<String, String, String> {
        constructor() : super()

        override fun doInBackground(vararg url: String?): String {
            var text: String
            val connection = URL(url[0]).openConnection() as HttpURLConnection
            try {
                connection.connect()
                text = connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
            }
            finally {
                connection.disconnect()
            }
            return text
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            displayResident(result)
        }

    }

    private fun getResidents(residents : JSONArray?)
    {
        if(residents !== null)
        {
            val listCharacter = ArrayList<Character>()
            var x = 0
            while (x < residents.length()) {
                val url = residents[x].toString()
                val stringRequest = StringRequest(
                    Request.Method.GET, url,
                    Response.Listener<String> { response ->
                        var stringresp = response.toString()
                        val jsonObject : JSONObject = JSONObject(stringresp)
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
                        println(listCharacter[0].name)
                    },
                    Response.ErrorListener {print("failed")}
                )

                VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)
                x++
            }
            val adapter = ListAdapterCharacter(this, listCharacter)
            character_list.adapter = adapter

        }
    }

    private fun displayResident(residents : String?)
    {
        val jsonObject : JSONObject = JSONObject(residents)
        val listCharacter = ArrayList<Character>()
        var x = 0
        println(jsonObject)
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
        /*while (x < jsonArray.length())
        {
            var jsonObject = jsonArray.getJSONObject(x)
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
            x++
        }*/
        val adapter = ListAdapterCharacter(this, listCharacter)
        character_list.adapter = adapter
    }
}

