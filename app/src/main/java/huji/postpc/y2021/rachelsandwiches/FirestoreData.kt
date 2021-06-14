package huji.postpc.y2021.rachelsandwiches

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreData(context: Context) {

    val db = Firebase.firestore
    var sp: SharedPreferences = context.getSharedPreferences("local_db_item", Context.MODE_PRIVATE)
    var orderId: String? = sp.getString("id", null)

    fun cleanSP() {
        sp.edit().clear().apply()
    }

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
        db.collection("orders").document(id).set(newOrder, SetOptions.merge())
            .addOnSuccessListener { Log.d(TAG, "New order successfully uploaded") }
            .addOnFailureListener { e -> Log.w(TAG, "Error uploading new order", e) }

        val editor = sp.edit()
        editor.putString("id", id)
        editor.apply()
    }

    fun deleteDocument(id: String) {
        db.collection("orders").document(id).delete()
                .addOnSuccessListener { Log.d(TAG, "Order successfully deleted") }
                .addOnFailureListener { e -> Log.w(TAG, "Error deleting order", e) }

        sp.edit().clear().apply()
    }
}