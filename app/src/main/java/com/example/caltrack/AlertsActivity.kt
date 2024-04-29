package com.example.caltrack

import android.annotation.SuppressLint
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class AlertsActivity : AppCompatActivity() {
    var jobScheduler : JobScheduler? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alerts)

        val stopJob = findViewById<Button>(R.id.stop)
        val startJob = findViewById<Button>(R.id.start)

        startJob.setOnClickListener{
            jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler

            val componentName = ComponentName(this, AlertsJobService::class.java)
            val builder = JobInfo.Builder(123,componentName)
            builder.setMinimumLatency(8000)
            builder.setOverrideDeadline(10000)
            builder.setPersisted(true)
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            builder.setRequiresCharging(false)
            jobScheduler!!.schedule(builder.build())
            Toast.makeText(this,"Repeating Water Alarm Set", Toast.LENGTH_SHORT).show()
        }

        stopJob.setOnClickListener{
            if(jobScheduler != null){
                jobScheduler!!.cancel(123)
                jobScheduler = null
                Toast.makeText(this,"Water Alarm Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}