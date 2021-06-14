package huji.postpc.y2021.rachelsandwiches

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*


class NewOrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_order)

        val database = FirestoreData(this)

        val customerNameEditText = findViewById<EditText>(R.id.customerNameEditText)
        val picklesNumTextView = findViewById<TextView>(R.id.picklesNumTextView)
        val decreasePicklesNumButton = findViewById<Button>(R.id.decreasePicklesNumButton)
        val increasePicklesNumButton = findViewById<Button>(R.id.increasePicklesNumButton)
        val hummusButton = findViewById<Button>(R.id.hummusButton)
        val tahiniButton = findViewById<Button>(R.id.tahiniButton)
        val commentEditText = findViewById<EditText>(R.id.commentEditText)
        val sendNewOrderButton = findViewById<FloatingActionButton>(R.id.sendNewOrderButton)

        var name: String = ""
        var pickles: Int = 0;
        var hummus: Boolean = false
        var tahini: Boolean = false
        var comment: String = ""

        picklesNumTextView.text = pickles.toString()
        decreasePicklesNumButton.isEnabled = false
        hummusButton.text = "NO"
        tahiniButton.text = "NO"

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

        sendNewOrderButton.setOnClickListener {
            val newId = UUID.randomUUID().toString()
            name = customerNameEditText.text.toString()
            comment = commentEditText.text.toString()
            database.uploadDocument(newId, name, pickles, hummus, tahini, comment)

            val intentToOpenEditOrderActivity = Intent(this, EditOrderActivity::class.java)
            startActivity(intentToOpenEditOrderActivity)
            finish()

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