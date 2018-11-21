package com.example.sbaar.bewerbungsapp

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SubjectList(private val context: Activity, internal var Subject: List<Subject>) : ArrayAdapter<Subject>(context, R.layout.subject_child) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater = context.layoutInflater
        val recycleViewItem = inflater.inflate(R.layout.subject_child,null,true)

        val textViewName = recycleViewItem.findViewById(R.id.subject_name_Id) as TextView

        val subjects = Subject[position]
        textViewName.text = subjects.subject_name

        return recycleViewItem
    }



}