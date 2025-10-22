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

    suspend fun  createMymy(mymy: Mymy): Mymy {
        val data = hashMapOf(
            "title" to mymy.title,
            "description" to mymy.description
        )

        try {
            val docRef = db.collection("mymy")
                .add(data)
                .await()

            return mymy.copy(id = docRef.id)
        } catch (exc: Exception) {
            throw Exception(exc.message)
        }
    }

    suspend fun deleteMymy(id: String) {
        try {
            db.collection("mymy")
                .document(id)
                .delete()
                .await()
        } catch (exc: Exception) {
            throw Exception(exc.message)
        }
    }

    suspend fun getMymy(id: String): Mymy? {
        val data = db.collection("mymy")
            .document(id)
            .get()
            .await()

        if (data.exists()) {
            return Mymy(
                id = data.id,
                title = data.getString("title").toString(),
                description = data.getString("description").toString()
            )
        }
        return null
    }

    suspend fun updateMymy(mymy: Mymy) {
        val payload = hashMapOf(
            "title" to mymy.title,
            "description" to mymy.description
        )

        try {
            db.collection("mymy")
                .document(mymy.id)
                .set(payload)
                .await()
        } catch (exc: Exception) {
            throw Exception(exc.message)
        }
    }
}