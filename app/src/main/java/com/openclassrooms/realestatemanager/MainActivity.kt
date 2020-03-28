package com.openclassrooms.realestatemanager

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.openclassrooms.realestatemanager.database_files.AppDatabase
import kotlinx.android.synthetic.main.listings_activity_layout.*

class MainActivity : AppCompatActivity() {

    companion object {
        var database: AppDatabase? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listings_activity_layout)

        setSupportActionBar(toolbar)
        toolbar.title = title

        database = Room.databaseBuilder(this,
                AppDatabase::class.java,
                "listing-db")
                .build()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }
}
