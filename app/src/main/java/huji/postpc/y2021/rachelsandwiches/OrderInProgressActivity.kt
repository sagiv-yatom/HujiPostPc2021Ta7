package huji.postpc.y2021.rachelsandwiches

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

class OrderInProgressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_in_progress)

        val database = FirestoreData(this)
        val orderId = database.orderId.toString()

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        // when the order's status is changed
        database.db.collection("orders").document(orderId).addSnapshotListener{ snapshot, e ->
            if (e != null) {
                Log.w(ContentValues.TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val downloadedOrder = snapshot.toObject(Order::class.java)
                if (downloadedOrder != null) {
                    when (downloadedOrder.status) {
                        "ready" -> {
                            startActivity(Intent(this, OrderIsReadyActivity::class.java))
                            finish()
                        }
                    }
                }
            } else {
                Log.d(ContentValues.TAG, "Current data: null")
            }
        }
    }
}