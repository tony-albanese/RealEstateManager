package com.openclassrooms.realestatemanager.listingmanagement

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import java.util.*

/*
This class is designed to create and EditViewModel so that parameters can be
passed to it.
 */
class ListingEditViewModelFactory(
        val application: Application,
        val owner: SavedStateRegistryOwner,
        val calendar: Calendar,
        val id: Long,
        defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    override fun <T : ViewModel> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
    ): T {
        return ListingEditViewModel(application, calendar, id) as T
    }

}