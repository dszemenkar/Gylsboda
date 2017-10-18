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

data class FirebasePlace(val name: String = "", val description: String = "", val likes: Int = 0, val image: String = "")

data class Place(val id: String, val name: String, val description: String, val likes: Int, val image: String)


class PlaceRepository{

    companion object {
        val TAG = "PlaceRepository"
    }

    private val database = FirebaseDatabase.getInstance()
    private val placesRef = database.getReference("Places")

    private val valueEventListener = object : ValueEventListener{
        override fun onCancelled(databaseError: DatabaseError?) {
            Log.e(TAG, "Error in Firebase communication", databaseError?.toException())
        }

        override fun onDataChange(snapshot: DataSnapshot?) {
            val latestPlaces = snapshot?.children?.map {
                val firebasePlace = it.getValue(FirebasePlace::class.java)
                return@map  Place(it.key, firebasePlace?.name ?: "", firebasePlace?.description ?: "", firebasePlace?.likes ?: 0, firebasePlace?.image ?: "")
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
                    //inget
                }

                override fun onDataChange(snapshot: DataSnapshot?) {
                    val firebasePlace = snapshot?.getValue(FirebasePlace::class.java)
                    val place = Place(snapshot?.key ?: "", firebasePlace?.name ?: "", firebasePlace?.description ?: "", firebasePlace?.likes ?: 0, firebasePlace?.image ?: "")
                    mediatorLiveData.postValue(place)
                }
            })
        } else{
            // Hittade ingen plats
            mediatorLiveData.postValue(Place("","","", 0, ""))
        }
        return mediatorLiveData
    }
    fun updatePlace(place: Place) {
        val firebasePlace = FirebasePlace(place.name, place.description, place.likes, place.image)
        when(place.id){
            "" -> return
            else -> placesRef.child(place.id).setValue(firebasePlace)
        }
    }
}