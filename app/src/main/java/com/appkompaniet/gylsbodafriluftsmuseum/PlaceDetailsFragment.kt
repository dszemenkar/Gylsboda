package com.appkompaniet.gylsbodafriluftsmuseum

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_place_details.*


 class PlaceDetailsFragment:Fragment() {

     companion object {
         val TAG = "PlaceDetailsFragment"
     }

     var placeId: String = ""
     var likes: Int = 0

     override fun onCreateView(inflater:LayoutInflater?, container:ViewGroup?,
savedInstanceState:Bundle?):View? {
 // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_place_details, container, false)
}

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProviders.of(activity).get(PlaceViewModel::class.java)

        //val place = viewModel.place

        val pId = arguments.getString("placeId")
        viewModel.loadPlace(pId).observe(this, Observer {
            val place: Place = it ?: Place("", "", "", 0, "")
            placeId = place.id
            likes = place.likes

            nameText.text = place.name
            descriptionText.text = place.description
            likeText.text = likes.toString() + " gillar detta."

        })

/**

        likebutton.setOnClickListener {
            val pName = nameText.text.toString()
            val pDesc = descriptionText.text.toString()
            likes = likes + 1
            val updatedPlace = Place(pName,pDesc,likes)
            //viewModel.(updatedPlace)
        }*/

    }
}// Required empty public constructor
