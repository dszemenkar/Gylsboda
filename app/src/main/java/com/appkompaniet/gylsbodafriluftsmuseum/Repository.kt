package com.appkompaniet.gylsbodafriluftsmuseum

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Created by davidszemenkar on 2017-10-17.
 */

data class FirebasePlace(val name: String = "", val description: String = "", val likes: Int = 0, val image: String = "", val description_en: String = "", val description_de: String = "", val long: Double = 0.0, val lat: Double = 0.0)

data class Place(val id: String, val name: String, val description: String, val likes: Int, val image: String, val description_en: String = "", val description_de: String = "", val long: Double = 0.0, val lat: Double = 0.0)


class PlaceRepository{

    companion object {
        val TAG = "PlaceRepository"
    }

    private val database = FirebaseDatabase.getInstance()

    private val placesRef = database.getReference("Places")

    private val valueEventListener = object : ValueEventListener{
        override fun onCancelled(databaseError: DatabaseError?) {

        }

        override fun onDataChange(snapshot: DataSnapshot?) {
            val latestPlaces = snapshot?.children?.map {
                val firebasePlace = it.getValue(FirebasePlace::class.java)
                return@map  Place(it.key, firebasePlace?.name ?: "", firebasePlace?.description ?: "", firebasePlace?.likes ?: 0, firebasePlace?.image ?: "", firebasePlace?.description_en ?: "", firebasePlace?.description_de ?: "", firebasePlace?.long ?: 0.0, firebasePlace?.lat ?: 0.0)
            } ?: emptyList()
            allPlaces.postValue(latestPlaces)
        }
    }


    var allPlaces: MediatorLiveData<List<Place>> = MediatorLiveData()

    fun startListening() {
        placesRef.addValueEventListener(valueEventListener)
    }

    fun stopListening() {
        placesRef.removeEventListener(valueEventListener)
    }

    fun loadPlace(id: String): LiveData<Place> {
        val mediatorLiveData = MediatorLiveData<Place>()
        if (id != ""){
            placesRef.child(id).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError?) {

                }

                override fun onDataChange(snapshot: DataSnapshot?) {
                    val firebasePlace = snapshot?.getValue(FirebasePlace::class.java)
                    val place = Place(snapshot?.key ?: "", firebasePlace?.name ?: "", firebasePlace?.description ?: "", firebasePlace?.likes ?: 0, firebasePlace?.image ?: "", firebasePlace?.description_en ?: "", firebasePlace?.description_de ?: "", firebasePlace?.long ?: 0.0, firebasePlace?.lat ?: 0.0)
                    mediatorLiveData.postValue(place)
                }
            })
        } else{
            mediatorLiveData.postValue(Place("","","", 0, "", "", "", 0.0, 0.0))
        }
        return mediatorLiveData
    }
    fun updatePlace(place: Place) {
        val firebasePlace = FirebasePlace(place.name, place.description, place.likes, place.image, place.description_en, place.description_de, place.long, place.lat)
        when(place.id){
            "" -> return
            else -> placesRef.child(place.id).setValue(firebasePlace)
        }
    }
}