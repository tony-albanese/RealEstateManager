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
        val listingDao = AppDatabase.getDatabase(application).listingDao()
        repository = ListingRepository(listingDao)

        //TODO: Get the current listing from the repository and cast it in a MutableLiveData object.
        currentListing = MutableLiveData(Listing())
        currentListing.value?.listingDate = "10/11/2422"
        currentListing.value?.listingSaleDate = "10/22/2022"
        currentListing.value?.listingPrice = 300000
        currentListing.value?.numberBathrooms = 1.5
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

}