package com.example.caltrack.fragments

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
import com.example.caltrack.Configuration
import com.example.caltrack.DashboardActivity
import com.example.caltrack.R
import com.example.caltrack.api.ApiService
import com.example.caltrack.api.FoodNutritionDetails
import com.example.caltrack.api.NutritionQueryRequest
import com.example.caltrack.data.FoodLogItem
import com.example.caltrack.data.InternalStorageHelper
import com.example.caltrack.data.TrackingLog
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


        val context = requireContext().applicationContext
        sharedPreferences =
            context.getSharedPreferences(Configuration.sharedPreferences, Context.MODE_PRIVATE)

        val name = sharedPreferences.getString(Configuration.name, "")

        submitBtn = v.findViewById(R.id.submit_button)
        submitBtn.setOnClickListener {
            val current = LocalDateTime.now()
            val hour = current.hour
            val minute = current.minute

            val logData = InternalStorageHelper(context).readFile(
                name + "_log.json",
                TrackingLog()
            ) as TrackingLog

            logData.food_logs.add(
                FoodLogItem(
                    getNutritionResult.food_name,
                    getNutritionResult.nf_calories,
                    quantity,
                    getNutritionResult.nf_total_fat,
                    getNutritionResult.nf_total_carbohydrate,
                    getNutritionResult.nf_protein,
                    getNutritionResult.nf_dietary_fiber
                )
            )
            logData.caloriesConsumed += getNutritionResult.nf_calories

            InternalStorageHelper(context).writeFile(
                name + "_log.json",
                logData
            )
            Toast.makeText(
                context,
                "logging ${quantity}g ${getNutritionResult.food_name} - ${getNutritionResult.nf_calories} kcals at $hour:$minute",
                Toast.LENGTH_SHORT
            ).show()
            Log.d("api-test", logData.toString())
            val intent = Intent(context, DashboardActivity::class.java)
            startActivity(intent)
        }

        return v
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