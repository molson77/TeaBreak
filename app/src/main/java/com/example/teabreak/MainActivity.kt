package com.example.teabreak

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var dbOps: DatabaseOperations
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // creates DatabaseOperations object for database manipulation
        dbOps = DatabaseOperations(this)

        // onClickListener for Adding Teas
        fab.setOnClickListener {
            val intent = Intent(this, AddTeaActivity::class.java)
            startActivity(intent)
        }

        viewTeas()
    }


    // viewTeas: Void - facilitates the integration of the RecyclerView and the TeaAdapter
    //                  to display the teas in the database.

    private fun viewTeas() {
        val teaList = dbOps.getTeas(this)
        val adapter = TeaAdapter(this, teaList)
        val rv: RecyclerView = findViewById(R.id.recyclerView)

        // linking the layoutManager to the recyclerView
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv.layoutManager = layoutManager
        // setting the custom adapter for the cardView tea items in the recyclerView
        rv.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }


}
