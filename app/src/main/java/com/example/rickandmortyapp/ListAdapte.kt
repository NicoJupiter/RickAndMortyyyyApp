package com.example.rickandmortyapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.appcompat.widget.AppCompatTextView

class  ListAdapte(val context: Context, val list: ArrayList<Location>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {


        val view:View = LayoutInflater.from(context).inflate(R.layout.row_layout,parent,false)

        val locationId = view.findViewById(R.id.location_id) as AppCompatTextView
        val locationName = view.findViewById(R.id.location_name) as AppCompatTextView
        val locationType = view.findViewById(R.id.location_type) as AppCompatTextView
        val locationDimension = view.findViewById(R.id.location_dimension) as AppCompatTextView

        locationId.text = list[position].id.toString()
        locationName.text = list[position].name
        locationType.text = list[position].type
        locationDimension.text = list[position].dimension

        return view
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
     return list.size
    }

}