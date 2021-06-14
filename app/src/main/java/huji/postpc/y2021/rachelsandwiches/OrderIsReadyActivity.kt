package huji.postpc.y2021.rachelsandwiches

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

class OrderIsReadyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_is_ready)

        val database = FirestoreData(this)
        val orderId = database.orderId.toString()

        val goItButton = findViewById<Button>(R.id.goItButton)

        goItButton.setOnClickListener {
            database.deleteDocument(orderId)
            val intentToOpenNewOrderActivity = Intent(this, NewOrderActivity::class.java)
            startActivity(intentToOpenNewOrderActivity)
            finish()
        }
    }
}