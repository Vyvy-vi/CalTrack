package com.vyvyvi.caltrack.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vyvyvi.caltrack.Configuration
import com.vyvyvi.caltrack.DashboardActivity
import com.vyvyvi.caltrack.LoginActivity
import com.vyvyvi.caltrack.R
import com.vyvyvi.caltrack.api.ApiService
import com.vyvyvi.caltrack.api.FoodNutritionDetails
import com.vyvyvi.caltrack.api.NutritionQueryRequest
import com.vyvyvi.caltrack.data.FoodLogItem
import com.vyvyvi.caltrack.data.InternalStorageHelper
import com.vyvyvi.caltrack.data.TrackingLog
import com.vyvyvi.caltrack.utils.Utils.convertEmailToKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class FoodLogOutputFragment : Fragment() {

    lateinit var proteinText: TextView
    lateinit var carbsText: TextView
    lateinit var fatText: TextView
    lateinit var fiberText: TextView
    lateinit var calorieCnt: TextView
    lateinit var submitBtn: Button
    lateinit var auth: FirebaseAuth
    var quantity: Float = 0f

    lateinit var sharedPreferences: SharedPreferences

    lateinit var getNutritionResult: FoodNutritionDetails

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v: View = inflater.inflate(R.layout.fragment_food_log_output, container, false)

        proteinText = v.findViewById(R.id.proteinTxt)
        carbsText = v.findViewById(R.id.carbsTxt)
        fatText = v.findViewById(R.id.fatTxt)
        fiberText = v.findViewById(R.id.fiberTxt)
        calorieCnt = v.findViewById(R.id.calories_count)
        auth = FirebaseAuth.getInstance()


        val context = requireContext().applicationContext
        sharedPreferences =
            context.getSharedPreferences(Configuration.sharedPreferences, Context.MODE_PRIVATE)

        submitBtn = v.findViewById(R.id.submit_button)
        submitBtn.setOnClickListener {
            val current = LocalDateTime.now()
            val hour = current.hour
            val minute = current.minute

            val uID = loadUID()

            if (uID == null) {
                val i = Intent(context, LoginActivity::class.java)
                startActivity(i)
                return@setOnClickListener
            }

            val foodItem = FoodLogItem(
                getNutritionResult.food_name,
                getNutritionResult.nf_calories,
                quantity,
                getNutritionResult.nf_total_fat,
                getNutritionResult.nf_total_carbohydrate,
                getNutritionResult.nf_protein,
                getNutritionResult.nf_dietary_fiber
            )

            updateTrackingLog(uID, getNutritionResult.nf_calories, foodItem)

            Toast.makeText(
                context,
                "logging ${quantity}g ${getNutritionResult.food_name} - ${getNutritionResult.nf_calories} kcals at $hour:$minute",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(context, DashboardActivity::class.java)
            startActivity(intent)
        }

        return v
    }

    fun loadUID(): String? {
        val currentUser = auth.currentUser
        if (currentUser == null || currentUser.email == null) {
            return null
        }

        var uID: String = sharedPreferences.getString(Configuration.userId, "").toString()
        if (uID == "") {
            val emailRef =
                FirebaseDatabase.getInstance().getReference("Emails").child(
                    convertEmailToKey(
                        currentUser.email!!
                    )
                )
            emailRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val itemId = dataSnapshot.getValue(String::class.java)
                    if (itemId != null) {
                        uID = itemId
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
        return uID
    }

    fun updateTrackingLog(userId: String, calories: Float, foodLogItem: FoodLogItem) {
        val logRef = FirebaseDatabase.getInstance().getReference("Logs").child(userId)

        logRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val existingLog = dataSnapshot.getValue(TrackingLog::class.java)
                if (existingLog != null) {
                    val updatedLog = existingLog
                    updatedLog.caloriesConsumed += calories
                    updatedLog.food_logs.add(foodLogItem)
                    logRef.setValue(updatedLog)
                } else {
                    val newLog = TrackingLog(
                        userId,
                        caloriesConsumed = calories,
                        food_logs = mutableListOf(foodLogItem)
                    )
                    logRef.setValue(newLog)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun displayReceivedData(quantity: Float, food: String) {
        this.quantity = quantity
        CoroutineScope(Dispatchers.IO).launch {
            fetchNutritionResults("$quantity grams of $food")
            activity?.runOnUiThread {
                proteinText.text = getNutritionResult.nf_protein.toString() + "g"
                fatText.text = getNutritionResult.nf_total_fat.toString() + "g"
                carbsText.text = getNutritionResult.nf_total_carbohydrate.toString() + "g"
                fiberText.text = getNutritionResult.nf_dietary_fiber.toString() + "g"
                calorieCnt.text = getNutritionResult.nf_calories.toString() + " kcal"
            }
        }
    }

    private suspend fun fetchNutritionResults(query: String) {
        try {
            val response = ApiService._interface.getNutrition(
                NutritionQueryRequest(query)
            )
            val responseData = response.body()
            Log.d("tst", responseData.toString())
            if (response.isSuccessful && responseData != null) {
                getNutritionResult = responseData.foods[0]
            } else {
                // Handle unsuccessful response (e.g., show an error message)
                val errorBody = response.errorBody()
                val errorString = errorBody?.string() ?: "Unknown error"
                Log.e(
                    "FoodLogOutputFragment",
                    "Error fetching food nutrition results: $errorString"
                )
            }
        } catch (e: Exception) {
            Log.e("FoodLogOutputFragment", "Error fetching food nutrition results: ${e.message}")
            return
        }
    }
}