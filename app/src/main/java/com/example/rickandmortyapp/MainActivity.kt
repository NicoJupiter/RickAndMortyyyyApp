package com.example.rickandmortyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.bottom_navigation
import org.json.JSONArray
import org.json.JSONObject
import android.view.View
import android.widget.ListView
import android.widget.ToggleButton


class MainActivity : AppCompatActivity() {

    var filename = "location_fav_test.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        VolleySingleton.getInstance(this).requestQueue

        val isDesplayFav : Boolean = intent.getBooleanExtra("displayFav", false)

        if(isDesplayFav)
        {
            val listUrl: List<String> = LocalFileManager.getFileContent(this, filename)
           displayFavLocation(listUrl)
        } else {
            displayAllLocation()
        }

        locations_list.setOnItemClickListener { parent, view, position, id ->
            val selectedLocation = parent.getItemAtPosition(position) as Location
            val intent = Intent(this, CharacterListActivity::class.java)
            intent.putExtra("location", selectedLocation)
            startActivity(intent)
        }

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
                R.id.action_add -> {
                    println("add")
                }
            }
            return@setOnNavigationItemSelectedListener true
        }

    }

    fun displayFavLocation(urls : List<String>)
    {
        val url =  "https://rickandmortyapi.com/api/location/"+ urls.joinToString()
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                val stringresp = response.toString()
                val locations  = JSONArray(stringresp)
               getLocation(locations)
            },
            Response.ErrorListener {print("failed")}
        )
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)
    }

    fun displayAllLocation()
    {
        val url = "https://rickandmortyapi.com/api/location"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                var stringresp = response.toString()
                val jsonObject = JSONObject(stringresp)
                val locations = JSONArray(jsonObject.getString("results"))
                getLocation(locations)
            },
            Response.ErrorListener {print("failed")}
        )

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)
    }

    fun getLocationbyType(view: View)
    {
        println("click on location")
        val parentRow = view.getParent() as View
        val listView = parentRow.parent as ListView
        val position = listView.getPositionForView(parentRow)
        val selectedLocation = listView.getItemAtPosition(position) as Location

        val stringRequest = StringRequest(
            Request.Method.GET, "https://rickandmortyapi.com/api/location?type="+selectedLocation.type,
            Response.Listener<String> { response ->
                var stringresp = response.toString()
                val jsonObject = JSONObject(stringresp)
                val locations = JSONArray(jsonObject.getString("results"))
                getLocation(locations)
            },
            Response.ErrorListener {print("failed")}
        )

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)

    }

    private fun getLocation(locations : JSONArray?)
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

    fun onToggleClickedLocation(view: View) {
        val on = (view as ToggleButton).isChecked
        val parentRow = view.getParent() as View
        val listView = parentRow.parent as ListView
        val position = listView.getPositionForView(parentRow)
        val selectedLocation = listView.getItemAtPosition(position) as Location
        if (on) {
            try {
                LocalFileManager.writeFile(this, filename, selectedLocation.id.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            try {
                LocalFileManager.deleteAndSave(this, filename, selectedLocation.id.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
