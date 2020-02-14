package com.digiapt.objectdection.firebase_data.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.digiapt.objectdection.firebase_data.model.ObjectModel
import com.digiapt.objectdection.firebase_data.repo.MainRepo
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ListenerRegistration
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import org.json.JSONObject
import java.lang.Exception

class MainViewModel : ViewModel() {

    private var objectModel:MutableLiveData<ObjectModel> = MutableLiveData()
    private var listener:ListenerRegistration? = null

    fun getObjectDetails(objectDetails:String): LiveData<ObjectModel> {
        listener = MainRepo.getObjectDetails(objectDetails).addSnapshotListener(EventListener { t, firebaseFirestoreException ->

            if (firebaseFirestoreException == null) {
                if (t!!.exists()) {
                    val jsonObject = JSONObject(t?.data)
                    val model: ObjectModel = GsonBuilder().serializeNulls().create()
                        .fromJson(jsonObject.toString(), ObjectModel::class.java)
                    objectModel.value = model
                }else {
                    Log.d("test_123444", "sadsd:   not  not ")
                    objectModel.value = null
                }

            }

        })
        return objectModel
    }

    fun removeListner() {
        try {
            listener?.remove()
        }catch (e:Exception) {

        }
    }
}