package com.example.todoapp

import com.google.firebase.database.FirebaseDatabase

class StartApp: android.app.Application()
{
    override fun onCreate() {
        super.onCreate();
        /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}