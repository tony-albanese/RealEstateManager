package com.openclassrooms.realestatemanager.listingmanagement

import android.app.Application
import android.util.Log
import android.widget.SeekBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.openclassrooms.realestatemanager.Constants.LISTING_SAVE_FILE
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utilities.CustomDialogBuilder
import com.openclassrooms.realestatemanager.Utilities.DateUtilities
import com.openclassrooms.realestatemanager.Utilities.ListingDataTypeConverters
import com.openclassrooms.realestatemanager.database_files.AppDatabase
import com.openclassrooms.realestatemanager.database_files.Listing
import com.openclassrooms.realestatemanager.database_files.ListingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*
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
    var saveToFile: Boolean = true

    var isNewListing: Boolean = true

    val currentListing: MutableLiveData<Listing>  //This is the member variable that will be exposed to the outside world.

    /*
    These variables are the LiveData for the number of rooms in the listing.
     */
    private val _numberOfRooms = MutableLiveData<Int>(0)
    val numberOfRoom: LiveData<Int>
        get() = _numberOfRooms

    private val _numberOfBedrooms = MutableLiveData<Int>(0)
    val numberOfBedrooms: LiveData<Int>
        get() = _numberOfBedrooms

    private val _numberOfBathrooms = MutableLiveData<Double>(0.0)
    val numberOfBathrooms: LiveData<Double>
        get() = _numberOfBathrooms

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

    fun saveListingToDatabase(builder: CustomDialogBuilder) {
        viewModelScope.launch {
            val returnedID = repository.insert(currentListing.value!!)
            when (returnedID) {
                -1.toLong() -> {
                    Log.i("DATABASE", "Conflict. Ignored")
                    Log.i("DATABASE", returnedID.toString())
                    saveToFile = true
                    builder.buildErrorDialog()
                            .show()

                }
                in 0..Long.MAX_VALUE -> {
                    saveToFile = false
                    deleteListingFile()
                    builder.buildSuccessDialogBuilder()
                            .show()

                    //TODO: Check Value of Listing Location
                    when (currentListing) {
                        null -> {
                        }
                        else -> {
                        }
                    }
                }
                else -> {
                    saveToFile = true
                    builder.buildErrorDialog()
                            .show()
                }

            }
        }
    }

    fun updateListing(builder: CustomDialogBuilder) {
        viewModelScope.launch {
            val returnedValue = repository.updateListing(currentListing.value!!)
            if (returnedValue == 1) {
                saveToFile = false
                Log.i("DATABASE", "Int: " + returnedValue.toString())
                builder.buildSuccessDialogBuilder()
                        .setMessage("Listing Updated")
                        .show()
            } else {
                saveToFile = true
                Log.i("DATABASE", "Int: " + returnedValue.toString())
                builder.buildErrorDialog()
                        .show()
            }
        }

    }
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main


    fun initializeListing() {
        if (!listingId.equals(0.toLong())) {
            viewModelScope.launch {
                val listing = repository.getListing(listingId)
                currentListing.value = listing
                isNewListing = false
            }
        } else {
            val listing = loadListingFromFile()
            if (listing != null) {
                currentListing.value = listing
            } else {
                currentListing.value?.listingSaleDate = DateUtilities.getDateString(calendar)
                currentListing.value?.listingDate = DateUtilities.getDateString(calendar)
            }
        }
    }

    fun saveListingToFile() {
        Log.i("FILE-IO", "saveListingToFile() called")
        try {
            val fileOutputStream = application.applicationContext.openFileOutput(LISTING_SAVE_FILE, Application.MODE_PRIVATE)
            val objectOutputStream = ObjectOutputStream(fileOutputStream)
            val gson = Gson()
            val listingString = gson.toJson(this.currentListing.value)
            objectOutputStream.writeObject(listingString)
            objectOutputStream.flush()
            fileOutputStream.close()
            objectOutputStream.close()
        } catch (e: FileNotFoundException) {
            Log.i("FILE-IO", "File not found saving.")
        } catch (e: IOException) {
            Log.i("FILE-IO", "Error saving file.")
        }

    }

    fun loadListingFromFile(): Listing? {
        Log.i("FILE-IO", "loadListingFromFile() called.")
        try {
            val fileInputStream = application.applicationContext.openFileInput(LISTING_SAVE_FILE)
            val objectInputStream = ObjectInputStream(fileInputStream)
            val listingJson = objectInputStream.readObject() as String
            val listingType = object : TypeToken<Listing>() {}.type
            objectInputStream.close()
            fileInputStream.close()
            return Gson().fromJson<Listing>(listingJson, listingType)
        } catch (e: FileNotFoundException) {
            Log.i("FILE-IO", "File not found.")
            return null
        } catch (e: EOFException) {
            e.printStackTrace()
            Log.i("FILE-IO", e.message ?: "Something wrong with EOF.")
            return null
        }
    }

    fun deleteListingFile() {
        Log.i("FILE-IO", "deleteListingFile() called")
        val successfuleDeletion = application.applicationContext.deleteFile(LISTING_SAVE_FILE)
        if (!successfuleDeletion) {
            Log.i("FILE-IO", "Error deleting file.")
        }
    }

    fun onProgressChange(seekBar: SeekBar?, progress: Int) {

        when (seekBar?.id) {
            R.id.seekbar_total_rooms -> {
                currentListing.value?.numberOfRooms = progress
                _numberOfRooms.value = currentListing.value?.numberOfRooms
            }
            R.id.seekbar_bedrooms -> {
                currentListing.value?.numberOfBedrooms = progress
                _numberOfBedrooms.value = progress
            }
            R.id.seekbar_bathrooms -> {
                val numberOfBathrooms = ListingDataTypeConverters.progressToNumberOfBathrooms(progress)
                currentListing.value?.numberBathrooms = numberOfBathrooms
                _numberOfBathrooms.value = numberOfBathrooms
            }
        }

    }
}