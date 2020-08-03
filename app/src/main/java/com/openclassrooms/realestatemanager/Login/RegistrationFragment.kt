package com.openclassrooms.realestatemanager.Login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.layout_registration.*

class RegistrationFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        view.findViewById<TextView>(R.id.tv_register).setOnClickListener {
            sendRegistrationRequest()
        }
    }

    fun createRegistrationRequest(): RegistrationRequest? {
        val email: EmailString.Email? = EmailString(et_email?.text.toString())

        return email?.let {
            val registrationRequest = RegistrationRequest(
                    et_first_name?.text.toString(),
                    et_last_name?.text.toString(),
                    it,
                    et_password?.text.toString()
            )
            return registrationRequest
        }
    }

    fun sendRegistrationRequest() {

    }

}