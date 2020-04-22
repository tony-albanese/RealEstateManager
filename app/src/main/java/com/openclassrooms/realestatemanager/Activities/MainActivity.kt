package com.openclassrooms.realestatemanager.Activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.openclassrooms.realestatemanager.Constants.LISTING_ID_KEY
import com.openclassrooms.realestatemanager.DisplayListings.ListingAdapter
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database_files.AppDatabase
import com.openclassrooms.realestatemanager.database_files.Listing
import com.openclassrooms.realestatemanager.database_files.ListingViewModel
import com.openclassrooms.realestatemanager.databinding.ListingsActivityLayoutBinding
import kotlinx.android.synthetic.main.listing_decription_editor_layout.*
import kotlinx.android.synthetic.main.listings_activity_layout.*
import kotlinx.android.synthetic.main.listings_information_layout.*
import java.util.*

class MainActivity : AppCompatActivity(), View.OnLongClickListener {

    lateinit var listingViewModel: ListingViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ListingAdapter
    lateinit var descriptionTextView: TextView

    var landscapeMode: Boolean = false

    companion object {
        var database: AppDatabase? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listingViewModel = ViewModelProvider(viewModelStore, ViewModelProvider.AndroidViewModelFactory(application)).get(ListingViewModel::class.java)
        val binding: ListingsActivityLayoutBinding = DataBindingUtil.setContentView(this, R.layout.listings_activity_layout)
        binding.lifecycleOwner = this
        binding.listingViewModel = listingViewModel

        landscapeMode = listing_info_landscape_frame_layout != null

        recyclerView = findViewById(R.id.rv_listings)
        descriptionTextView = findViewById(R.id.tv_description_body)

        adapter = ListingAdapter(Locale("EN", "US"), landscapeMode, itemViewOnClickListenerCallback)



        recyclerView.layoutManager = LinearLayoutManager(this)
        val itemDecor = DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(itemDecor)
        recyclerView.adapter = adapter

        setSupportActionBar(toolbar)
        toolbar.title = title

        database = Room.databaseBuilder(this,
                AppDatabase::class.java,
                "listing-db")
                .build()

        listingViewModel.publishedListings.observe(this, androidx.lifecycle.Observer {
            it?.let {
                adapter.setListings(it)
            }
        })

        descriptionTextView.setOnLongClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        //return super.onPrepareOptionsMenu(menu)
        menu?.getItem(1)?.setEnabled(landscapeMode)
        menu?.getItem(1)?.setVisible(landscapeMode)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_add_listing -> {
                val intent = Intent(this, EditListingActivity::class.java)
                intent.putExtra(LISTING_ID_KEY, 0.toLong())
                startActivity(intent)
                finish()
                return true
            }
            R.id.menu_item_edit_listing -> {
                val intent = Intent(this, EditListingActivity::class.java)
                intent.putExtra(LISTING_ID_KEY, listingViewModel.selectedListing.value?.id
                        ?: 0.toLong())
                startActivity(intent)
                finish()
                return true
            }
            else -> {
                return true

            }
        }
    }

    val itemViewOnClickListenerCallback: (Listing) -> Unit = {
        listingViewModel.setCurrentListing(it)
    }

    override fun onLongClick(p0: View?): Boolean {
        listing_description_editor_layout.visibility = View.VISIBLE
        listing_description_editor_layout.bringToFront()
        return true

    }
}