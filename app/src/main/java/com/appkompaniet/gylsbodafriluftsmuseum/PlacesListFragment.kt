package com.appkompaniet.gylsbodafriluftsmuseum

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_places_list.*


class PlacesListFragment:Fragment() {

public override fun onCreateView(inflater:LayoutInflater?, container:ViewGroup?,
savedInstanceState:Bundle?):View? {
 // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_places_list, container, false)
}

     override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
         super.onViewCreated(view, savedInstanceState)

         placesList.adapter = PlacesAdapter()
         placesList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

         val viewModel = ViewModelProviders.of(activity).get(PlaceViewModel::class.java)

         viewModel.allPlaces.observe(this, Observer {
             (placesList.adapter as PlacesAdapter).places = it ?: emptyList()
             placesList.adapter.notifyDataSetChanged()
         })

     }

    class PlacesAdapter : RecyclerView.Adapter<PlaceViewHolder>() {

        var places: List<Place> = emptyList()

        override fun onBindViewHolder(holder: PlaceViewHolder?, position: Int) {
            val place = places[position]
            holder?.placeId = place.id
            holder?.placeName?.text = place.name

        }

        override fun getItemCount(): Int = places.size

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PlaceViewHolder {
            val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.place_item, parent, false)
            return PlaceViewHolder(itemView)
        }

    }

    class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val placeName: TextView = itemView.findViewById(R.id.placeName)
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val placeDetailsFragment = PlaceDetailsFragment()
            val arguments = Bundle()
            arguments.putString("placeId", placeId)
            placeDetailsFragment.arguments = arguments

            (view?.context as FragmentActivity)
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, placeDetailsFragment)
                    .addToBackStack(null)
                    .commit()
        }

        var placeId: String = ""
    }


}// Required empty public constructor
