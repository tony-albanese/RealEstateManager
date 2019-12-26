package com.openclassrooms.realestatemanager

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.openclassrooms.realestatemanager.database_files.Listing
import com.openclassrooms.realestatemanager.database_files.ListingViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var listingViewModel: ListingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.app_toolbar))

        val recyclerView = findViewById<RecyclerView>(R.id.message_recycler_view)
        val adapter = ListingAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        val store = viewModelStore

        listingViewModel = ViewModelProvider(store, ViewModelProvider.AndroidViewModelFactory(application)).get(ListingViewModel::class.java)

        listingViewModel.listings.observe(this, Observer {
            it?.let {
                adapter.setListings(it)
            }
        })

        val button = findViewById<MaterialButton>(R.id.btn_upload)
        button.setOnClickListener {
            val textBody = et_message_input.text.toString()
            var listing = Listing(textBody)
            listingViewModel.insert(listing)
            et_message_input.text?.clear()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> {
                listingViewModel.clearDatabase()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
