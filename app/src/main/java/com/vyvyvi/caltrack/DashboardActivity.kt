package com.vyvyvi.caltrack

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.GridView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vyvyvi.caltrack.customviews.ProgressDrawing
import com.vyvyvi.caltrack.utils.MenuGridAdapter
import com.vyvyvi.caltrack.data.User
import com.vyvyvi.caltrack.data.TrackingLog
import com.vyvyvi.caltrack.utils.items
import com.vyvyvi.caltrack.utils.Utils.convertEmailToKey

class DashboardActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var heroTxt: TextView
    lateinit var heroTxt2: TextView
    lateinit var heroDesc: TextView

    lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        auth = FirebaseAuth.getInstance()
        sharedPreferences =
            getSharedPreferences(Configuration.sharedPreferences, Context.MODE_PRIVATE)



        dbRef = FirebaseDatabase.getInstance().getReference("Users")
        loadUser { userData->
            if (userData == null) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                return@loadUser
            }

            userData.uID?.let {
                loadTrackingLog(it) { logData ->
                    if (logData == null) {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        return@loadTrackingLog
                    }
                    loadStats(userData, logData)
                    loadButtons(userData, logData)
                }
            }
        }
    }

    fun loadUID(): String? {
        val currentUser = auth.currentUser
        if (currentUser == null || currentUser.email == null) {
            return null
        }

        var uID: String = sharedPreferences.getString(Configuration.userId, "").toString()
        if (uID == "") {
            val emailRef =
                FirebaseDatabase.getInstance().getReference("Emails").child(convertEmailToKey(
                    currentUser.email!!
                ))
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

    fun loadUser(callback: (User?) -> Unit) {
        var uID: String? = loadUID()

        if (uID == null || uID == "") {
            callback(null)
            return
        }

        dbRef.child(uID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                callback(user)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                callback(null)
            }
        })
    }

    fun loadStats(userData: User, logData: TrackingLog) {
        val customView = ProgressDrawing(
            this,
            logData.caloriesConsumed,
            logData.waterConsumed,
            userData.dailyCalories.toInt(),
            userData.dailyWater.toInt()
        )
        val rootView =
            findViewById<ViewGroup>(R.id.dashboard)
        rootView.addView(customView)

        heroTxt = findViewById(R.id.heroTxt)
        heroTxt2 = findViewById(R.id.heroTxt2)
        heroTxt.text =
            """${logData.caloriesConsumed.toInt()} of ${userData.dailyCalories.toInt()} Cal consumed"""
        heroTxt2.text =
            """${"%.2f".format(logData.waterConsumed)} of ${"%.2f".format(userData.dailyWater)} L consumed"""
        heroDesc = findViewById(R.id.heroDesc)
        heroDesc.text = """Your maintainence calories are ${userData.dailyCalories + 250}kcal. 
With a calorie deficit of 250kcal a day, you can lose 0.25kg / week and reach your target weight of ${userData.targetWeight}kg in ${userData.weeksNeeded} weeks."""
    }

    fun loadButtons(userData: User, logData: TrackingLog) {
        val grid = findViewById<GridView>(R.id.menuGrid)
        grid.adapter = MenuGridAdapter(applicationContext)
        grid.setOnItemClickListener{ adapterView, view, i, l ->
            val intent = Intent(applicationContext, items[i].activity)
            if (items[i].title == "Log Water") {
                intent.putExtra("name", userData.name)
                intent.putExtra("consumed", logData.waterConsumed)
                intent.putExtra("total", userData.dailyWater)
            }
            startActivity(intent)
        }
    }

    fun loadTrackingLog(uID: String, callback: (TrackingLog?) -> Unit) {
        val logRef =
            FirebaseDatabase.getInstance().getReference("Logs").child(uID)

        logRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists())
                {
                    val log = dataSnapshot.getValue(TrackingLog::class.java)
                    callback(log)
                } else {
                    val newLog = TrackingLog(uID)
                    logRef.setValue(newLog)
                        .addOnSuccessListener {
                            callback(newLog)
                        }
                        .addOnFailureListener {
                            callback(null)
                        }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                callback(null)
            }
        })
    }
}