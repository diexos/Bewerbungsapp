package com.example.sbaar.bewerbungsapp



import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_subject.*
import kotlinx.android.synthetic.main.subject_child.view.*
import kotlinx.android.synthetic.main.apply.*




class MainMenuActivity : AppCompatActivity() {

    var subjects: MutableList<String> = ArrayList()
    var displayList: MutableList<String> = ArrayList()
    val manager = supportFragmentManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject)
        LoadData()
        subject_list.layoutManager = LinearLayoutManager(this)
        subject_list.adapter = SubjectAdapter(displayList, this)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val searchItem = menu.findItem(R.id.menu_search)
        if (searchItem != null) {
            val searchView = searchItem.actionView as SearchView
            val editext = searchView.findViewById<EditText>(android.support.v7.appcompat.R.id.search_src_text)
            editext.hint = "Search here..."

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {

                    displayList.clear()
                    if (newText.isNotEmpty()) {
                        val search = newText.toLowerCase()
                        subjects.forEach {
                            if (it.toLowerCase().contains(search)) {
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
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    class SubjectAdapter(items: List<String>, ctx: MainMenuActivity) : RecyclerView.Adapter<SubjectAdapter.ViewHolder>() {

        private var list = items
        private var context = ctx

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.name.text = list[position]
            holder.itemView.setOnClickListener {
                Toast.makeText(context, "${holder.name.text}", Toast.LENGTH_LONG).show()
                context.ShowApply()

            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.subject_child, parent, false))
        }


        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val name = v.subject_name
        }
    }

    private fun LoadData() {
        subjects.add("Angewandte Mathematik")
        subjects.add("Angewandte Informatik")
        subjects.add("Chemie")
        displayList.addAll(subjects)
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



}
