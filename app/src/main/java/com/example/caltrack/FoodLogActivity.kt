package com.example.caltrack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.caltrack.fragments.FoodLogOutputFragment
import com.example.caltrack.fragments.OnDataListener

class FoodLogActivity : AppCompatActivity(), OnDataListener {
    lateinit var textView: TextView
    lateinit var imageView: ImageView
    lateinit var foodName: String
    lateinit var photoUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_log)

        foodName = intent.extras?.getString("foodName", "").toString()
        photoUrl = intent.extras?.getString("photoUrl", "").toString()

        imageView = findViewById(R.id.food_image_view)
        textView = findViewById(R.id.txt)
        textView.text = foodName

        Glide.with(this)
            .load(photoUrl)
            .into(imageView)
    }

    override fun communicate(msg: String) {
        if (msg != "") {
            var obj: FoodLogOutputFragment =
                supportFragmentManager.findFragmentById(R.id.log_output) as FoodLogOutputFragment
            obj.displayReceivedData(msg.toFloat(), foodName)
        }
    }
}