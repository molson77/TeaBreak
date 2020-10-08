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

    /* property used to retrieve the list of TeaItems from the SQLite database */
    companion object {
        lateinit var dbOps: DatabaseOperations
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        dbOps = DatabaseOperations(this)

        viewTeas()
    }

    /* Inflates the Action Bar menu */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    /* Functionality for Action Bar menu */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                val intent = Intent(this, AddTeaActivity::class.java)
                startActivity(intent)
                true
            }
            else -> false
        }
    }

    /**
     * viewTeas:
     * @desc facilitates the integration of the RecyclerView and
     * the TeaAdapter to display the teas in the database.
     * Links the CardSpacingItemDecoration
     */

    private fun viewTeas() {
        val teaList = dbOps.getTeas(this)
        val adapter = TeaAdapter(this, teaList)
        val rv: RecyclerView = findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv.layoutManager = layoutManager
        rv.adapter = adapter
        rv.addItemDecoration(CardSpacingItemDecoration(20))
    }

}
