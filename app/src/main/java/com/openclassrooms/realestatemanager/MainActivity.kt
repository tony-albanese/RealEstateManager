package com.openclassrooms.realestatemanager

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.openclassrooms.realestatemanager.database_files.AppDatabase
import kotlinx.android.synthetic.main.listings_activity_layout.*
import kotlinx.android.synthetic.main.listings_information_layout.*

class MainActivity : AppCompatActivity() {

    lateinit var fab: FloatingActionButton
    companion object {
        var database: AppDatabase? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listings_activity_layout)

        setSupportActionBar(toolbar)
        toolbar.title = title

        fab = findViewById(R.id.fab_test)

        database = Room.databaseBuilder(this,
                AppDatabase::class.java,
                "listing-db")
                .build()

        fab.setOnClickListener {
            if (listing_info_landscape_frame_layout != null) {
                Toast.makeText(this, "LARGE", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "SMALL", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_add_listing -> {
                val intent = Intent(this, EditListingActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
            else -> {
                return true

            }
        }
    }
}
