package com.openclassrooms.realestatemanager.listingmanagement

import android.app.Application
import android.content.DialogInterface
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.openclassrooms.realestatemanager.Constants.LISTING_SAVE_FILE
import com.openclassrooms.realestatemanager.Utilities.DateUtilities
import com.openclassrooms.realestatemanager.database_files.AppDatabase
import com.openclassrooms.realestatemanager.database_files.Listing
import com.openclassrooms.realestatemanager.database_files.ListingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*
import kotlin.coroutines.CoroutineContext

/*
This class is designed to preserve the UI state when editing
a listing.
 */

class ListingEditViewModel(
        private val application: Application,
        val calendar: Calendar,
        val listingId: Long,
        val gson: Gson,
        val objectInputStream: ObjectInputStream,
        val objectOutputStream: ObjectOutputStream
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
        } else {
            //TODO: Set the initial dates for listing and selling.
            //Check if there is an unsaved listing saved on file. If not,
            //Set the default dates. Otherwise, restore the listing from the file.
            currentListing.value?.listingSaleDate = DateUtilities.getDateString(calendar)
            currentListing.value?.listingDate = DateUtilities.getDateString(calendar)
        }
    }

    fun saveListingToFile() {
        val listingString = gson.toJson(this.currentListing.value)
        objectOutputStream.writeObject(listingString)
        objectOutputStream.close()
    }

    fun loadListingFromFile() {
        val listingJson = objectInputStream.readObject() as String
        val listingType = object : TypeToken<Listing>() {}.type
        val listing = gson.fromJson<Listing>(listingJson, listingType)
        currentListing.value = listing
    }

    fun deleteListingFile() {
        val successfuleDeletion = application.applicationContext.deleteFile(LISTING_SAVE_FILE)
        if (!successfuleDeletion) {
            Log.e("DATABASE", "Error deleting file.")
        }
    }
}