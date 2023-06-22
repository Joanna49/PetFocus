package com.maciejka.petfocus

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.maciejka.petfocus.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPomodoro.setOnClickListener {
            pokazFragment(MenuPomodoroFragment())
        }
        binding.btnTodo.setOnClickListener {
            pokazFragment(ToDoFragment())
        }
        binding.btnZwierzak.setOnClickListener{
            pokazFragment(ZwierzakFragment())
        }
        createNotificationChannel()
    }

    private fun pokazFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.replace(R.id.srodkowyKontener,fragment,null)
        fragmentTransaction.commit()
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "kanalNotyfikacji"
            val descriptionText = "PetFocus"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("PETFOCUS",name,importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}