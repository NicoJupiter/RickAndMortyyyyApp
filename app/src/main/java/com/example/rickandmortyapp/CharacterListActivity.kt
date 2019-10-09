package com.example.rickandmortyapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.character_main.*
import org.json.JSONArray
import org.json.JSONObject




class CharacterListActivity : AppCompatActivity() {

    private val listCharacter = ArrayList<Character>()
    var filename = "character_fav_test2.txt"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.character_main)

        val isDesplayFav : Boolean = intent.getBooleanExtra("displayFav", false)

        if(isDesplayFav)
        {
           val listUrl: List<String> = LocalFileManager.getFileContent(this, filename)
            displayFavoriteCharacter(listUrl)
        } else {
            val location = intent.getSerializableExtra("location") as Location
            displayResidentFromLocation(location.id)
        }


        character_list.setOnItemClickListener { parent, view, position, id ->

            val selectedCharacter = parent.getItemAtPosition(position) as Character
            val intent = Intent(this, CharacterDetailActivity::class.java)
            intent.putExtra("character_extra", selectedCharacter)
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

    private fun displayFavoriteCharacter(urls : List<String>) {

      val url =  "https://rickandmortyapi.com/api/character/"+ urls.joinToString()
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                val stringresp = response.toString()
                val characters  = JSONArray(stringresp)
                var x = 0
                while (x < characters.length())
                {
                  val jsonObject = characters.getJSONObject(x)
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
                }
              val adapter = ListAdapterCharacter(this, listCharacter)
                character_list.adapter = adapter
            },
            Response.ErrorListener {print("failed")}
        )
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)
    }

    private fun displayResidentFromLocation(id : Int) {

        val url = "https://rickandmortyapi.com/api/location/${id}"


        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                val stringresp = response.toString()
                val jsonObject : JSONObject = JSONObject(stringresp)
                val residents  = JSONArray(jsonObject.getString("residents"))

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

    }

    private fun getResidents(url : String?)
    {
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                val stringresp = response.toString()
                val jsonObject = JSONObject(stringresp)
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


    fun getCharacterbyType(view: View)
    {
        println("click label")
    }
    fun onToggleClickedCharacter(view: View) {
        val on = (view as ToggleButton).isChecked
        val parentRow = view.getParent() as View
        val listView = parentRow.parent as ListView
        val position = listView.getPositionForView(parentRow)
        val selectedCharacter = listView.getItemAtPosition(position) as Character
        if (on) {
            try {
                LocalFileManager.writeFile(this, filename, selectedCharacter.id.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            try {
               LocalFileManager.deleteAndSave(this, filename, selectedCharacter.id.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

