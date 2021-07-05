package com.example.workmanagerdemo

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*


class MainActivity : AppCompatActivity() {
    private var buttonEnque: Button? = null
    private var textView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonEnque = findViewById(R.id.buttonEnqueue)
        textView = findViewById(R.id.textViewStatus)

        //creating constraints
        val constraints: Constraints = Constraints.Builder()
            .setRequiresCharging(true) // you can add as many constraints as you want
            .build()

        // passing data
        val data: Data = Data.Builder()
            .putString(MyWorker.TASK_DESC, "The task data passed from MainActivity")
            .build()


        //This is the subclass of our WorkRequest
        val workRequest: WorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
            .setInputData(data)
            .setConstraints(constraints)
            .build()

        buttonEnque?.setOnClickListener {
            //Enqueue the work request
            WorkManager.getInstance(this).enqueue(workRequest)
        }

        //Listening to the work status
        WorkManager.getInstance().getWorkInfoByIdLiveData(workRequest.getId())
            .observe(this, { workInfo -> //Displaying the status into TextView
                textView?.append(
                    """
            ${workInfo.state.name}
            
            """.trimIndent()
                )
            })
    }

}

