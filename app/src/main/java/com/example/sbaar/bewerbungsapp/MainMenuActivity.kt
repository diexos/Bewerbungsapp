package com.example.sbaar.bewerbungsapp



import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import android.webkit.WebView
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.sbaar.bewerbungsapp.R.id.subject_list
import com.example.sbaar.bewerbungsapp.R.string.website_url
import kotlinx.android.synthetic.main.activity_subject.*
import kotlinx.android.synthetic.main.activity_subject.view.*
import kotlinx.android.synthetic.main.subject_child.view.*
import org.json.JSONException
import org.json.JSONObject


class MainMenuActivity : AppCompatActivity() {
    var links = ""
    var subjects: MutableList<Subject> = ArrayList()
    var displayList: MutableList<Subject> = ArrayList()
    val manager = supportFragmentManager
    val url_root = "http://192.168.178.56/bewerbungsdb/v1/?op="
    val url_get = url_root + "getSubject"
    lateinit var db:DBHelper
    private val TAG = "PermissionDemo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject)


        db = DBHelper(this)
        LoadData()
        subject_list.layoutManager = LinearLayoutManager(this)
        subject_list.adapter =  SubjectAdapter(displayList, this)


    }




    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val searchItem = menu.findItem(R.id.menu_search)
        if (searchItem != null) {
            val searchView = searchItem.actionView as SearchView
            val editext = searchView.findViewById<EditText>(android.support.v7.appcompat.R.id.search_src_text)
            editext.hint = "Suche"

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {

                    displayList.clear()
                    if (newText.isNotEmpty()) {
                        val search = newText.toLowerCase()
                        subjects.forEach {
                            if (it.subject_name.toLowerCase().contains(search)) {
                                displayList.add(it)
                            }
                        }
                    } else {
                        displayList.addAll(subjects)
                    }
                    subject_list.adapter?.notifyDataSetChanged()
                    return true
                }

            })
        }

        return super.onCreateOptionsMenu(menu)
    }




    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_contact -> ShowContact()
            R.id.menu_status ->{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Bewerbungsstatus")
                builder.setMessage("Der Status deiner Bewerbung ist _______ .")
                builder.setPositiveButton("Ja"){dialog, which ->
                    Toast.makeText(applicationContext,"bestätigt.",Toast.LENGTH_SHORT).show()
                }
                builder.setNegativeButton("Nein"){dialog,which ->
                    Toast.makeText(applicationContext,"abgelehnt.",Toast.LENGTH_SHORT).show()
                }
                builder.setNeutralButton("Abbrechen"){_,_ ->
                    Toast.makeText(applicationContext,"abgebrochen.",Toast.LENGTH_SHORT).show()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    class SubjectAdapter(items: List<Subject>, ctx: MainMenuActivity) : RecyclerView.Adapter<SubjectAdapter.ViewHolder>() {

        private var list = items
        private var context = ctx

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val subj = list[position]
            holder.name.text = subj.subject_name
            context.links = subj.link
            holder.itemView.setOnClickListener {
                Toast.makeText(context, "${holder.name.text}", Toast.LENGTH_LONG).show()
                context.ShowApply()

            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.subject_child, parent, false))
        }


        class ViewHolder(v: View) : RecyclerView.ViewHolder(v){
            val name = v.subject_name_Id
            var link = ""

        }
    }





    private fun LoadData() {
        val stringRequest = StringRequest(Request.Method.GET,
                url_get,
                Response.Listener<String> { s ->
                    try {
                        val obj = JSONObject(s)
                        if (!obj.getBoolean("error")) {
                            val array = obj.getJSONArray("subject")
                            for (i in 0 until array.length() ) {
                                val objectSubject = array.getJSONObject(i)
                                val subj = Subject(
                                        objectSubject.getInt("ID"),
                                        objectSubject.getInt("Abschluss"),
                                        objectSubject.getString("Name"),
                                        objectSubject.getInt("Fakultät"),
                                        objectSubject.getString("Link")
                                )
                                displayList.add(subj)
                                subjects.add(subj)
                            }
                        } else {
                            Toast.makeText(this, obj.getString("message"), Toast.LENGTH_LONG).show()
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()

                    }
                }, Response.ErrorListener { volleyError -> Toast.makeText(this, volleyError.message, Toast.LENGTH_LONG).show() })
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add<String>(stringRequest)
    }

    fun ShowApply() {
        val transaction = manager.beginTransaction()
        val fragment = ApplyFragment()
        transaction.replace(R.id.fragment_holder, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }

    fun ShowContact() {
        val transaction = manager.beginTransaction()
        val fragment = ContactFragment()
        transaction.replace(R.id.fragment_holder, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


   fun ShowDataCheck() {
        val transaction = manager.beginTransaction()
        val fragment = DataCheckFragment()
        transaction.replace(R.id.fragment_holder, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun getLink() :String{


        return links

    }


}



