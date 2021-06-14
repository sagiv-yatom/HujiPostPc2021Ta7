package huji.postpc.y2021.rachelsandwiches

import android.content.ContentValues
import android.content.ContentValues.TAG
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
        val deleteOrderButton = findViewById<Button>(R.id.deleteOrderButton)
        val saveOrderButton = findViewById<FloatingActionButton>(R.id.saveOrderButton)

        var name: String = ""
        var pickles: Int = 0;
        var hummus: Boolean = false
        var tahini: Boolean = false
        var comment: String = ""

        if (savedInstanceState != null) {
            name = savedInstanceState.getString("customer name").toString()
            pickles = savedInstanceState.getInt("pickles")
            hummus = savedInstanceState.getBoolean("hummus")
            tahini = savedInstanceState.getBoolean("tahini")
            comment = savedInstanceState.getString("comment").toString()

            customerNameEditText.setText(name)
            picklesNumTextView.text = pickles.toString()

            if (hummus) {
                hummusButton.text = "YES"
            }
            else {
                hummusButton.text = "NO"
            }

            if (tahini) {
                tahiniButton.text = "YES"
            }
            else {
                tahiniButton.text = "NO"
            }

            commentEditText.setText(comment)
        }
        else {
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
        }

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

        deleteOrderButton.setOnClickListener {
            database.deleteOrder(orderId)
            val intentToOpenNewOrderActivity = Intent(this, NewOrderActivity::class.java)
            startActivity(intentToOpenNewOrderActivity)
            finish()
        }

        saveOrderButton.setOnClickListener {
            name = customerNameEditText.text.toString()
            comment = commentEditText.text.toString()
            database.uploadOrder(orderId, name, pickles, hummus, tahini, comment, "waiting")
        }

        // when the order's status is changed
        database.db.collection("orders").document(orderId).addSnapshotListener{ snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val downloadedOrder = snapshot.toObject(Order::class.java)
                if (downloadedOrder != null) {
                    when (downloadedOrder.status) {
                        "in-progress" -> {
                            startActivity(Intent(this, OrderInProgressActivity::class.java))
                            finish()
                        }
                        "ready" -> {
                            startActivity(Intent(this, OrderIsReadyActivity::class.java))
                            finish()
                        }
                    }
                }
            } else {
                Log.d(TAG, "Current data: null")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {

        val customerNameEditText = findViewById<EditText>(R.id.customerNameEditText)
        val picklesNumTextView = findViewById<TextView>(R.id.picklesNumTextView)
        val hummusButton = findViewById<Button>(R.id.hummusButton)
        val tahiniButton = findViewById<Button>(R.id.tahiniButton)
        val commentEditText = findViewById<EditText>(R.id.commentEditText)

        val name = customerNameEditText.text.toString()
        val pickles = Integer.valueOf(picklesNumTextView.text.toString())
        var hummus = false
        if (hummusButton.text.toString() == "YES") {
            hummus = true
        }
        var tahini = false
        if (tahiniButton.text.toString() == "YES") {
            tahini = true
        }
        val comment = commentEditText.text.toString()

        outState.run {
            putString("customer name", name)
            putInt("pickles", pickles)
            putBoolean("hummus", hummus)
            putBoolean("tahini", tahini)
            putString("comment", comment)
        }

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val name = savedInstanceState.getString("customer name")
        val pickles = savedInstanceState.getInt("pickles")
        val hummus = savedInstanceState.getBoolean("hummus")
        val tahini = savedInstanceState.getBoolean("tahini")
        val comment = savedInstanceState.getString("comment")

        val customerNameEditText = findViewById<EditText>(R.id.customerNameEditText)
        val picklesNumTextView = findViewById<TextView>(R.id.picklesNumTextView)
        val hummusButton = findViewById<Button>(R.id.hummusButton)
        val tahiniButton = findViewById<Button>(R.id.tahiniButton)
        val commentEditText = findViewById<EditText>(R.id.commentEditText)

        customerNameEditText.setText(name)
        picklesNumTextView.text = pickles.toString()

        if (hummus) {
            hummusButton.text = "YES"
        }
        else {
            hummusButton.text = "NO"
        }

        if (tahini) {
            tahiniButton.text = "YES"
        }
        else {
            tahiniButton.text = "NO"
        }

        commentEditText.setText(comment)

    }
}