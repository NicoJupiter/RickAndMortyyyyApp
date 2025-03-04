package com.example.rickandmortyapp

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
import android.widget.Toast
import kotlinx.android.synthetic.main.character_main.bottom_navigation




class CharacterListActivity : AppCompatActivity() {

    private val listCharacter = ArrayList<Character>()
    var filename = "character_fav.txt"
    private val adapter = ListAdapterCharacter(this, listCharacter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.character_main)
        val isDesplayFav : Boolean = intent.getBooleanExtra("displayFav", false)
        VolleySingleton.getInstance(this).requestQueue

        if(isDesplayFav)
        {
            if (LocalFileManager.fileExist(this , filename))
            {
                val listUrl: List<String> = LocalFileManager.getFileContent(this, filename)
                displayFavoriteCharacter(listUrl)
            } else {
                Toast.makeText(this , "No character fav to show" , Toast.LENGTH_LONG).show()
                displayResidentFromLocation(0)
            }
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

   /* private val displayImg = object : View.OnLayoutChangeListener {
        override fun onLayoutChange(v: View, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {

            if(listCharacter.size > 0)
            {
                var x = 0
                while (x < listCharacter.size)
                {

                     if(v.character_list.getChildAt(0).character_img.tag == "notLoaded")
                       {
                           loadImgCharacter(listCharacter[0].urlImg, v.character_list.getChildAt(0).character_img)
                           v.character_list.getChildAt(0).character_img.tag = "loaded"
                       }
                    x++
                    character_list.removeOnLayoutChangeListener(this)
                }
            }
        }
    }*/

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
                val jsonObject = JSONObject(stringresp)
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

                character_list.adapter = adapter
             //   character_list.addOnLayoutChangeListener(displayImg)

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
                Toast.makeText(this , "Add character to fav" , Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            try {
               LocalFileManager.deleteAndSave(this, filename, selectedCharacter.id.toString())
                Toast.makeText(this , "Remove character to fav" , Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

  /*  private fun loadImgCharacter(imgUrl : String, characterImg: ImageView)
    {
        val imageRequest = ImageRequest(
            imgUrl,
            Response.Listener { response ->
                characterImg.setImageBitmap(response)
            },
            characterImg.layoutParams.width, // Image width
            characterImg.layoutParams.height, // Image height
            ScaleType.MATRIX, // Image scale type
            Bitmap.Config.ARGB_8888, //Image decode configuration
            Response.ErrorListener { error ->
                println("failed img load")
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(imageRequest)
    }*/
}

