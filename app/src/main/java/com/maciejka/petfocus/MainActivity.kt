package com.maciejka.petfocus

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
            pokazFragment(PomodoroFragment())
        }
        binding.btnTodo.setOnClickListener {
            pokazFragment(ToDoFragment())
        }
        binding.btnZwierzak.setOnClickListener{
            pokazFragment(ZwierzakFragment())
        }
    }

    private fun pokazFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.replace(R.id.srodkowyKontener,fragment,null)
        fragmentTransaction.commit()
    }
}