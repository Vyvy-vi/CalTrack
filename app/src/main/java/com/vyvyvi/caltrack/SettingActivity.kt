package com.vyvyvi.caltrack

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.vyvyvi.caltrack.data.InternalStorageHelper
import com.vyvyvi.caltrack.data.User
import com.vyvyvi.caltrack.utils.Utils.convertEmailToKey
import java.util.UUID

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
    lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    lateinit var logout: Button
    lateinit var email: TextView
    lateinit var sharedPreferences: SharedPreferences
    lateinit var submitBtn: Button

    lateinit var unameInput: EditText
    lateinit var ageInput: EditText
    lateinit var weightInput: EditText
    lateinit var heightInput: EditText
    lateinit var targetWeightInput: EditText

    lateinit var avatar: ShapeableImageView
    lateinit var getImage: ActivityResultLauncher<String>
    private var imageUri: Uri? = null
    private var avatarId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        sharedPreferences =
            getSharedPreferences(Configuration.sharedPreferences, Context.MODE_PRIVATE)
        auth = FirebaseAuth.getInstance()

        logout = findViewById(R.id.logoutBtn)
        submitBtn = findViewById(R.id.submit_button)
        email = findViewById(R.id.email)
        avatar = findViewById(R.id.avatar)

        getImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                if (it != null) {
                    imageUri = it
                    avatarId = uploadImage()
                }
                avatar.setImageURI(imageUri)
            })

        unameInput = findViewById(R.id.uname)
        ageInput = findViewById(R.id.age)
        weightInput =
            findViewById(R.id.current_weight)
        heightInput =
            findViewById(R.id.current_height)
        targetWeightInput =
            findViewById(R.id.target_weight)

        email.text = sharedPreferences.getString(Configuration.email, "")

        logout.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.putString(Configuration.userId, "")
            editor.apply()

            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        dbRef = FirebaseDatabase.getInstance().getReference("Users")
        var uID: String = sharedPreferences.getString(Configuration.userId, "").toString()
        if (uID == "") {
            sharedPreferences.getString(Configuration.email, "")?.let {
                val emailRef = FirebaseDatabase.getInstance().getReference("Emails")
                    .child(convertEmailToKey(it))
                emailRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val itemId = dataSnapshot.getValue(String::class.java)
                        if (itemId != null) {
                            uID = itemId
                            seedData(uID)
                        } else {
                            uID = dbRef.push().key!!
                            emailRef.setValue(uID)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            }
        } else
            seedData(uID)

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

            val user = User(
                uID,
                unameInput.text.toString(),
                ageInput.text.toString().toInt(),
                genderSelection.toString(),
                heightInput.text.toString().toFloat(),
                weightInput.text.toString().toFloat(),
                targetWeightInput.text.toString().toFloat(),
                activitySelection.toString(),
                dailyCalories,
                8f,
                weeks,
                avatarId
            )

            val editor = sharedPreferences.edit()
            editor.putString(Configuration.name, user.name)
            editor.putString(Configuration.userId, uID)
            editor.apply()
            dbRef.child(uID).setValue(user).addOnCompleteListener {
                Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_LONG).show()

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
            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error : ${err.message}", Toast.LENGTH_LONG).show()
            }
        }

        avatar.setOnClickListener {
            getImage.launch("image/*")
        }
    }

    fun seedData(uID: String) {
        dbRef.child(uID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userData = dataSnapshot.getValue(User::class.java)

                if (userData != null) {
                    unameInput.setText(sharedPreferences.getString(Configuration.name, ""))
                    ageInput.setText(userData.age.toString())
                    heightInput.setText(userData.height.toString())
                    weightInput.setText(userData.currentWeight.toString())
                    targetWeightInput.setText(userData.targetWeight.toString())


                    fetchImage(userData.avatar!!)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun fetchImage(avatarId: String) {
        val ref: StorageReference = FirebaseStorage.getInstance().reference
            .child(avatarId)
        val ONE_MEGABYTE: Long = 1024 * 1024
        ref.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            avatar.setImageBitmap(bitmap)
        }
    }

    private fun uploadImage(): String? {
        if (imageUri == null) return null

        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading..")
        progressDialog.setMessage("Uploading your image..")
        progressDialog.show()

        val id = UUID.randomUUID().toString()

        val ref: StorageReference = FirebaseStorage.getInstance().reference
            .child(id)

        ref.putFile(imageUri!!).addOnSuccessListener {
            progressDialog.dismiss()
            Toast.makeText(applicationContext, "Image Uploaded!!", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            progressDialog.dismiss()
            Toast.makeText(applicationContext, "Failed to upload image..", Toast.LENGTH_SHORT)
                .show()
        }
        return id
    }
}