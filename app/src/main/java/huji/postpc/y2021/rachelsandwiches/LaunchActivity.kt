package huji.postpc.y2021.rachelsandwiches

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot

class LaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = FirestoreData(this)
//        database.cleanSP()

        if (database.orderId == null) {
            startActivity(Intent(this, NewOrderActivity::class.java))
        }
        else {
            database.db.collection("orders").document(database.orderId.toString()).get()
                .addOnSuccessListener { result: DocumentSnapshot ->
                    Log.d(ContentValues.TAG, "Document successfully downloaded")
                    val downloadedOrder = result.toObject(Order::class.java)
                    if (downloadedOrder != null) {
                        if (downloadedOrder.status == "waiting") {
                            startActivity(Intent(this, EditOrderActivity::class.java))
                        }
                        else if (downloadedOrder.status == "in-progress") {
                            startActivity(Intent(this, OrderInProgressActivity::class.java))
                        }
                    }
                }
                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error downloading document", e) }
        }
    }
}