package huji.postpc.y2021.rachelsandwiches

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class OrderIsReadyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_is_ready)

        val database = FirestoreData(this)
        val orderId = database.orderId.toString()

        val gotItButton = findViewById<Button>(R.id.gotItButton)

        gotItButton.setOnClickListener {
            database.setOrderAsDone(orderId)
            val intentToOpenNewOrderActivity = Intent(this, NewOrderActivity::class.java)
            startActivity(intentToOpenNewOrderActivity)
            finish()
        }
    }
}