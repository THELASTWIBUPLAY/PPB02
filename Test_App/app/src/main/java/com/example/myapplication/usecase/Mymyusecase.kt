package com.example.myapplication.usecase

import com.example.myapplication.entity.Mymy
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class Mymyusecase {
    val db = Firebase.firestore

    suspend fun getMymy(): List<Mymy> {
        try {
            val data = db.collection("mymy")
                .get()
                .await()

            if (!data.isEmpty) {
                return data.documents.map {
                    Mymy(
                        id = it.id,
                        title = it.getString("title").toString(),
                        description = it.getString("description").toString()
                    )
                }
            }
            return arrayListOf()
        } catch (exc:Exception) {
            throw Exception (exc.message)
        }
    }
}