package com.appkompaniet.gylsbodafriluftsmuseum

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        if(savedInstanceState == null){
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, PlacesListFragment())
                    .commit()
        }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = mAuth?.currentUser

        if (currentUser == null){
            createAnonymousUser()
        }else{

        }

    }

    fun createAnonymousUser(){
        mAuth?.signInAnonymously()
                ?.addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        supportFragmentManager
                                .beginTransaction()
                                .replace(R.id.fragment_container, PlacesListFragment())
                                .commit()

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "You need internet connection for this application. Be sure to connect to Internet.", Toast.LENGTH_LONG).show()
                    }

                })

    }
}
