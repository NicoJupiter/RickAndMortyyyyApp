package com.example.rickandmortyapp

import android.content.Context
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.Arrays.asList




class LocalFileManager {
    companion object {

        fun writeFile(context: Context, filename: String ,selectedItem: String) {
            val outputStream = context.openFileOutput(filename, Context.MODE_APPEND)
            val idCharacterFav : String = selectedItem  + ","
            outputStream.write(idCharacterFav.toByteArray())
            outputStream.close()
        }

        fun deleteAndSave(context: Context, filename: String ,selectedItem: String) {
            val fileInputStream : FileInputStream = context.openFileInput(filename)
            val inputStreamReader = InputStreamReader(fileInputStream)

            val bufferedReader = BufferedReader(inputStreamReader)

            var result: List<String> = bufferedReader.readLine().split(",").map { it.trim() }
            println(result)
            val mutableList : MutableList<String> = result.toMutableList()
            if(mutableList.remove(selectedItem))
            {
                println("removed")
                println(mutableList)
                println(mutableList.size)
            } else {
                println("failed to remove")
            }
            bufferedReader.close()
            context.deleteFile(filename)

            val outputStream = context.openFileOutput(filename, Context.MODE_APPEND)
            for (id in mutableList) {
                val idConcat = id + ","
                outputStream.write(idConcat.toByteArray())
            }

            outputStream.close()
        }
    }
}
