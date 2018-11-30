package com.example.sbaar.bewerbungsapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context:Context):SQLiteOpenHelper(context, DATABASE_NAME,null,DATABASE_VER) {

    companion object {
        private val DATABASE_VER = 1
        private  val DATABASE_NAME ="Bewerbung.db"

        private val TABLE_NAME ="studiengang"
        private val COL_ID ="Id"
        private val COL_DEGREE ="Abschluss"
        private val COL_SUBJECT_NAME ="Name"
        private val COL_FAKULTÄT ="Fakultät"
        private val COL_LINK ="Link"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_QUERY = ("CREATE TABLE  $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY $COL_DEGREE INTEGER $COL_SUBJECT_NAME TEXT $COL_FAKULTÄT INTEGER $COL_LINK TEXT)" )
        db!!.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db!!)
    }


    val allSubject:List<Subject>
        get(){
                val lstSubject = ArrayList<Subject>()
                val selectQuery = "SELECT * FROM $TABLE_NAME"
                val db:SQLiteDatabase = this.writableDatabase
            val cursor = db.rawQuery(selectQuery,null)
            if(cursor.moveToFirst())
            {
                do {
                    val subject = Subject()
                    subject._id = cursor.getInt(cursor.getColumnIndex(COL_ID))
                    subject.degree = cursor.getInt(cursor.getColumnIndex(COL_DEGREE))
                    subject.subject_name = cursor.getString(cursor.getColumnIndex(COL_SUBJECT_NAME))
                    subject.fakultät = cursor.getInt(cursor.getColumnIndex(COL_FAKULTÄT))
                    subject.link = cursor.getString(cursor.getColumnIndex(COL_LINK))

                    lstSubject.add(subject)

                }while (cursor.moveToNext())

            }
            db.close()
            return lstSubject
        }

    fun addSubject(subject:Subject)
    {
        val db:SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        values.put(COL_ID,subject._id)
        values.put(COL_DEGREE,subject.degree)
        values.put(COL_SUBJECT_NAME,subject.subject_name)
        values.put(COL_FAKULTÄT,subject.fakultät)
        values.put(COL_LINK,subject.link)
        db.insert(TABLE_NAME,null,values)
        db.close()

    }

}