package com.openclassrooms.realestatemanager.MasterDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.openclassrooms.realestatemanager.MasterDetail.Listing.DummyContent
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database_files.AppDatabase
import com.openclassrooms.realestatemanager.database_files.Listing
import com.openclassrooms.realestatemanager.database_files.ListingDao
import com.openclassrooms.realestatemanager.database_files.ListingRepository
import kotlinx.android.synthetic.main.activity_listing_detail.*
import kotlinx.coroutines.*

/**
 * A fragment representing a single Listing detail screen.
 * This fragment is either contained in a [ListingListActivity]
 * in two-pane mode (on tablets) or a [ListingDetailActivity]
 * on handsets.
 */
class ListingDetailFragment : Fragment() {

    /**
     * The dummy content this fragment is presenting.
     */
    private var item: DummyContent.DummyItem? = null
    private var listingItemToDisplay: Listing? = null;
    lateinit var repository: ListingRepository
    lateinit var listingDao: ListingDao
    lateinit var detailsTextView: TextView

    var listingId: Long? = null

    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listingDao = AppDatabase.getDatabase(activity!!.application).listingDao()
        repository = ListingRepository(listingDao)
        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.

                listingId = it.getLong(ARG_ITEM_ID)
                //initializeListingObject(it.getLong(ARG_ITEM_ID))
                item = DummyContent.ITEM_MAP[it.getString(ARG_ITEM_ID)]
                activity?.toolbar_layout?.title = item?.content
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.listing_detail, container, false)

        detailsTextView = rootView.findViewById(R.id.listing_detail)
        initializeListingObject(listingId)
        /*
        listingItemToDisplay.let {
            rootView.listing_detail.text = it?.listingDescription ?: "No listing desc."
        }
         */

        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }

    suspend fun getListingFromDatabase(id: Long): Listing? {

        return withContext(Dispatchers.IO) {

            var listing = listingDao.getListingById(id)
            listing
        }

    }


    fun initializeListingObject(id: Long?) {

        uiScope.launch {
            listingItemToDisplay = id?.let { getListingFromDatabase(it) }
            detailsTextView.text = listingItemToDisplay?.listingDescription ?: "Something is null"
        }


    }


}
