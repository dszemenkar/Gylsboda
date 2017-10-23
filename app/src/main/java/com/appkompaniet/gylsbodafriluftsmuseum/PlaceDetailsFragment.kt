package com.appkompaniet.gylsbodafriluftsmuseum

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_place_details.*
import java.util.*


class PlaceDetailsFragment:Fragment() {

     companion object {
         val TAG = "PlaceDetailsFragment"
     }

     var placeId: String = ""
     var likes: Int = 0
     var pImage: String = ""

     override fun onCreateView(inflater:LayoutInflater?, container:ViewGroup?,
savedInstanceState:Bundle?):View? {
 // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_place_details, container, false)
}

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProviders.of(activity).get(PlaceViewModel::class.java)

        val pId = arguments.getString("placeId")
        viewModel.loadPlace(pId).observe(this, Observer {
            val place: Place = it ?: Place("", "", "", 0, "", "", "", 0.0, 0.0)
            placeId = place.id
            likes = place.likes
            pImage = place.image

            nameText.text = place.name
            val lang = Locale.getDefault().getDisplayLanguage()
            if(lang == "English"){
                descriptionText.text = place.description_en
            }
            else if(lang == "German"){
                descriptionText.text = place.description_de
            }
            else{
                descriptionText.text = place.description
            }

            likeText.text = getString(R.string.likes_text, likes.toString())
            Glide.with(imageView).load(pImage).into(imageView)
        })


        likebutton.setOnClickListener {
            val pName = nameText.text.toString()
            val pDesc = descriptionText.text.toString()
            likes = likes + 1
            val updatedPlace = Place(pId, pName, pDesc, likes, pImage)
            likeText.text = getString(R.string.likes_text, likes.toString())
            viewModel.updatePlace(updatedPlace)
        }

    }
}// Required empty public constructor
