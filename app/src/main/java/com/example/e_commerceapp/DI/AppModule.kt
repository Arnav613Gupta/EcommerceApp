package com.example.e_commerceapp.DI

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.example.e_commerceapp.Firebase.FireBaseCommon
import com.example.e_commerceapp.Utils.Constants.INTRODUCTIOn_SHARED_PREFRENCES
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuthDependency() = FirebaseAuth.getInstance()


    @Provides
    @Singleton
    fun provideFirebaseFirestoreDatabase()= Firebase.firestore

    @Provides
    fun provideIntroSharedPrefrences(application:Application)=application.getSharedPreferences(INTRODUCTIOn_SHARED_PREFRENCES,MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideFirebaseCommon(firebaseAuth: FirebaseAuth,firestore: FirebaseFirestore)=FireBaseCommon(firebaseAuth,firestore)


    @Provides
    @Singleton
    fun provideFirebaseStorage()=FirebaseStorage.getInstance().reference

}