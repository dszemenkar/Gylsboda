package com.appkompaniet.gylsbodafriluftsmuseum

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel


class PlaceViewModel : ViewModel(){
    companion object {
        val TAG = "PlacesViewModel"
    }
    private val placeRepository = PlaceRepository()

    val allPlaces = placeRepository.allPlaces

    init {
        placeRepository.startListening()
    }

    override fun onCleared() {
        super.onCleared()
        placeRepository.stopListening()

    }

    fun loadPlace(id: String): LiveData<Place>{
        return placeRepository.loadPlace(id)
    }

    fun updatePlace(place: Place){
        placeRepository.updatePlace(place)
    }


    /**init {
        places = LinkedList<Place>()

        for (i in 0 .. 20){
            places += Place("Transformatorn", "Denna byggnad uppfördes", 0)
        }
    }*/
    /**fun updatePlace(place: Place){
        this.place = place
        Log.d(TAG, "Updated place with ${place.likes}")
    }*/
}