package huji.postpc.y2021.rachelsandwiches

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentSnapshot
import java.util.*

class EditOrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_order)

        val database = FirestoreData(this)
        val orderId = database.orderId.toString()

        val customerNameEditText = findViewById<EditText>(R.id.customerNameEditText)
        val picklesNumTextView = findViewById<TextView>(R.id.picklesNumTextView)
        val decreasePicklesNumButton = findViewById<Button>(R.id.decreasePicklesNumButton)
        val increasePicklesNumButton = findViewById<Button>(R.id.increasePicklesNumButton)
        val hummusButton = findViewById<Button>(R.id.hummusButton)
        val tahiniButton = findViewById<Button>(R.id.tahiniButton)
        val commentEditText = findViewById<EditText>(R.id.commentEditText)
        val saveOrderButton = findViewById<FloatingActionButton>(R.id.saveOrderButton)

        var name: String = ""
        var pickles: Int = 0;
        var hummus: Boolean = false
        var tahini: Boolean = false
        var comment: String = ""

        database.db.collection("orders").document(orderId).get()
            .addOnSuccessListener { result: DocumentSnapshot ->
                Log.d(ContentValues.TAG, "Document successfully downloaded")
                val downloadedOrder = result.toObject(Order::class.java)
                if (downloadedOrder != null) {
                    /** name */
                    name = downloadedOrder.customerName
                    customerNameEditText.setText(name)
                    /** pickles */
                    pickles = downloadedOrder.pickles
                    picklesNumTextView.text = pickles.toString()
                    decreasePicklesNumButton.isEnabled = pickles != 0
                    increasePicklesNumButton.isEnabled = pickles != 10
                    /** hummus */
                    if (downloadedOrder.hummus) {
                        hummus = true
                        hummusButton.text = "YES"
                    } else {
                        hummus = false
                        hummusButton.text = "NO"
                    }
                    /** tahini */
                    if (downloadedOrder.tahini) {
                        tahini = true
                        tahiniButton.text = "YES"
                    } else {
                        tahini = false
                        tahiniButton.text = "NO"
                    }
                    /** comment */
                    comment = downloadedOrder.comment
                    commentEditText.setText(comment)
                }
            }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error downloading document", e) }

        decreasePicklesNumButton.setOnClickListener {
            pickles--
            picklesNumTextView.text = pickles.toString()
            decreasePicklesNumButton.isEnabled = pickles != 0
            increasePicklesNumButton.isEnabled = pickles != 10
        }

        increasePicklesNumButton.setOnClickListener {
            pickles++
            picklesNumTextView.text = pickles.toString()
            decreasePicklesNumButton.isEnabled = pickles != 0
            increasePicklesNumButton.isEnabled = pickles != 10
        }

        hummusButton.setOnClickListener {
            if (hummus) {
                hummus = false
                hummusButton.text = "NO"
            } else {
                hummus = true
                hummusButton.text = "YES"
            }
        }

        tahiniButton.setOnClickListener {
            if (tahini) {
                tahini = false
                tahiniButton.text = "NO"
            } else {
                tahini = true
                tahiniButton.text = "YES"
            }
        }

        saveOrderButton.setOnClickListener {
            name = customerNameEditText.text.toString()
            comment = commentEditText.text.toString()
            database.uploadDocument(orderId, name, pickles, hummus, tahini, comment)
        }
    }
}