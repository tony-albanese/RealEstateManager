package com.openclassrooms.realestatemanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.database_files.ListingViewModel
import com.openclassrooms.realestatemanager.recycler_selection.ListingAdapter
import com.openclassrooms.realestatemanager.recycler_selection.Lookup

class PhotoActivity : AppCompatActivity() {

    lateinit var listingViewModel: ListingViewModel
    var tracker: SelectionTracker<Long>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        val recyclerView = findViewById<RecyclerView>(R.id.rv_listings)
        val adapter = ListingAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        listingViewModel = ViewModelProvider(viewModelStore, ViewModelProvider.AndroidViewModelFactory(application)).get(ListingViewModel::class.java)

        listingViewModel.listings.observe(this, Observer {
            it?.let {
                adapter.setListings(it)
            }
        })

        tracker = SelectionTracker.Builder<Long>(
                "selection-id",
                recyclerView,
                StableIdKeyProvider(recyclerView),
                Lookup(recyclerView),
                StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
                SelectionPredicates.createSelectAnything()
        ).build()

        adapter.setTracker(tracker!!)
    }
}
