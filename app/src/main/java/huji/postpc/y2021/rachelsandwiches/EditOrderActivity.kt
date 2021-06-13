package huji.postpc.y2021.rachelsandwiches

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentSnapshot

class EditOrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_order)

        val database = FirestoreData(this)

        val customerNameEditText = findViewById<EditText>(R.id.customerNameEditText)
        val picklesNumTextView = findViewById<TextView>(R.id.picklesNumTextView)
        val decreasePicklesNumButton = findViewById<Button>(R.id.decreasePicklesNumButton)
        val increasePicklesNumButton = findViewById<Button>(R.id.increasePicklesNumButton)
        val hummusButton = findViewById<Button>(R.id.hummusButton)
        val tahiniButton = findViewById<Button>(R.id.tahiniButton)
        val commentEditText = findViewById<EditText>(R.id.commentEditText)

        database.db.collection("orders").document(database.orderId.toString()).get()
            .addOnSuccessListener { result: DocumentSnapshot ->
                Log.d(ContentValues.TAG, "Document successfully downloaded")
                val downloadedOrder = result.toObject(Order::class.java)
                if (downloadedOrder != null) {
                    customerNameEditText.setText(downloadedOrder.customerName)
                    picklesNumTextView.text = downloadedOrder.pickles.toString()

                    if (downloadedOrder.hummus) hummusButton.text = "yes" else {
                        hummusButton.text = "no"
                    }
                    if (downloadedOrder.tahini) tahiniButton.text = "yes" else {
                        tahiniButton.text = "no"
                    }
                    commentEditText.setText(downloadedOrder.comment)
                }
            }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error downloading document", e) }
    }
}