package com.openclassrooms.realestatemanager.listingmanagement

import android.app.Application
import android.content.DialogInterface
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.openclassrooms.realestatemanager.database_files.AppDatabase
import com.openclassrooms.realestatemanager.database_files.Listing
import com.openclassrooms.realestatemanager.database_files.ListingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext

/*
This class is designed to preserve the UI state when editing
a listing.
 */

class ListingEditViewModel(
        private val application: Application,
        val calendar: Calendar,
        val listingId: Long
) : ViewModel(), CoroutineScope {

    
    val repository: ListingRepository

    val currentListing: MutableLiveData<Listing>  //This is the member variable that will be exposed to the outside world.

    init {
        System.out.println(listingId)
        val listingDao = AppDatabase.getDatabase(application).listingDao()
        repository = ListingRepository(listingDao)
        currentListing = MutableLiveData(Listing())
        initializeListing()
    }

    fun updateListingPrice(newValue: Int) {
        currentListing.value?.listingPrice = newValue
    }

    fun saveListingToDatabase(builder: MaterialAlertDialogBuilder) {
        viewModelScope.launch {
            val returnedID = repository.insert(currentListing.value!!)

            if (!returnedID.equals(0)) {
                builder.show()
            } else {
                builder.setMessage("Something went wrong.")
                        .setPositiveButton("OK", createPositiveErrorButtonLisenter())
                        .show()
            }

        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private fun createPositiveErrorButtonLisenter(): DialogInterface.OnClickListener {
        return DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
        }
    }

    fun initializeListing() {
        if (!listingId.equals(0.toLong())) {
            viewModelScope.launch {
                val listing = repository.getListingById(listingId)
                currentListing.value = listing
            }
        }
    }
}