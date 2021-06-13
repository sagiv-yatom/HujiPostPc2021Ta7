package huji.postpc.y2021.rachelsandwiches

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreData {

    val db = Firebase.firestore

    fun uploadDocument(
        id: String,
        customerName: String,
        pickles: Int,
        hummus: Boolean,
        tahini: Boolean,
        comment: String
    ) {

        val newOrder = Order(
            id, customerName, pickles, hummus, tahini, comment, "waiting"
        )
        db.collection("orders").document(id).set(newOrder)
            .addOnSuccessListener { Log.d(TAG, "New order successfully uploaded") }
            .addOnFailureListener { e -> Log.w(TAG, "Error uploading new order", e) }
//        val editor = sp.edit()
//        editor.putString("id", id)
//        editor.apply()
    }
}