package com.example.caltrack

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import com.example.caltrack.data.InternalStorageHelper
import com.example.caltrack.data.User

fun calculateCalories(W: Float, H: Float, A: Float): Float {
    return ((13.397 * W) + (4.799 * H) - (5.677 * A) + 88.362 - 275).toFloat()
}

fun calculateWeeksNeeded(
    currentWeight: Float,
    targetWeight: Float
): Float {
    return ((currentWeight - targetWeight) / 0.25).toFloat()
}

class SettingActivity : AppCompatActivity() {
    lateinit var logout: Button
    lateinit var username: TextView
    lateinit var email: TextView
    lateinit var sharedPreferences: SharedPreferences
    lateinit var submitBtn: Button

    lateinit var ageInput: EditText
    lateinit var weightInput: EditText
    lateinit var heightInput: EditText
    lateinit var targetWeightInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        sharedPreferences =
            getSharedPreferences(Configuration.sharedPreferences, Context.MODE_PRIVATE)

        logout = findViewById(R.id.logoutBtn)
        submitBtn = findViewById(R.id.submit_button)

        username = findViewById(R.id.uname)
        email = findViewById(R.id.email)

        ageInput = findViewById(R.id.age)
        weightInput =
            findViewById(R.id.current_weight)
        heightInput =
            findViewById(R.id.current_height)
        targetWeightInput =
            findViewById(R.id.target_weight)

        val userData = InternalStorageHelper(this).readFile(
            sharedPreferences.getString(Configuration.name, "") + ".json",
            User()
        ) as User

        username.text =
            username.text.toString() + sharedPreferences.getString(Configuration.name, "")
        email.text = email.text.toString() + sharedPreferences.getString(Configuration.email, "")

        if (userData.name != "") {
            ageInput.setText(userData.age.toString())
            heightInput.setText(userData.height.toString())
            weightInput.setText(userData.currentWeight.toString())
            targetWeightInput.setText(userData.targetWeight.toString())
        }



        logout.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.putBoolean(Configuration.loggedIn, false)
            editor.apply()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        submitBtn.setOnClickListener {
            var activitySelection: String? = null
            var genderSelection: String? = null

            val activityGroup = findViewById<RadioGroup>(R.id.activity_group)
            if (activityGroup != null) {
                val checkedRadioButtonId = activityGroup.getCheckedRadioButtonId()
                if (checkedRadioButtonId != -1) {
                    val radioButton = activityGroup.findViewById<RadioButton>(checkedRadioButtonId)
                    activitySelection = radioButton?.text.toString()
                }
            }

            val genderGroup = findViewById<RadioGroup>(R.id.gender_group)
            if (genderGroup != null) {
                val checkedRadioButtonId = genderGroup.getCheckedRadioButtonId()
                if (checkedRadioButtonId != -1) {
                    val radioButton = genderGroup.findViewById<RadioButton>(checkedRadioButtonId)
                    genderSelection = radioButton?.text.toString()
                }
            }

            if (ageInput.text.isEmpty()
                || weightInput.text.isEmpty()
                || heightInput.text.isEmpty()
                || targetWeightInput.text.isEmpty()
                || genderSelection == null
                || genderSelection == ""
                || activitySelection == null
                || activitySelection == ""
            ) {
                Toast.makeText(this, "Can not leave blank field", Toast.LENGTH_SHORT).show()
            }
            val dailyCalories = calculateCalories(
                weightInput.text.toString().toFloat(),
                heightInput.text.toString().toFloat(),
                ageInput.text.toString().toFloat()
            )
            val weeks = calculateWeeksNeeded(
                weightInput.text.toString().toFloat(),
                targetWeightInput.text.toString().toFloat()
            )
            val u = User(
                sharedPreferences.getString(Configuration.name, "") as String,
                ageInput.text.toString().toInt(),
                genderSelection.toString(),
                heightInput.text.toString().toFloat(),
                weightInput.text.toString().toFloat(),
                targetWeightInput.text.toString().toFloat(),
                activitySelection.toString(),
                dailyCalories,
                8f,
                weeks
            )

            InternalStorageHelper(this).writeFile(
                sharedPreferences.getString(Configuration.name, "") + ".json",
                u
            )
            Toast.makeText(
                this,
                "In order to reach your target weight, you can eat only " + dailyCalories + "kcal everyday.",
                Toast.LENGTH_LONG
            ).show()
            Toast.makeText(
                this,
                "It might take you " + weeks + " weeks to reach target.",
                Toast.LENGTH_LONG
            ).show()
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }
    }
}