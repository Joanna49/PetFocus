package com.maciejka.petfocus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.maciejka.petfocus.databinding.FragmentZwierzakBinding


class ZwierzakFragment : Fragment() {
    private var _binding:FragmentZwierzakBinding?=null
    private val binding get()= _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnKarmienie.setOnClickListener{
            Toast.makeText(context, "This is button", Toast.LENGTH_SHORT).show()
        }
        binding.btnMycie.setOnClickListener{
            Toast.makeText(context, "This is button", Toast.LENGTH_SHORT).show()
        }
        binding.btnZabawa.setOnClickListener{
            Toast.makeText(context, "This is button", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentZwierzakBinding.inflate(layoutInflater, container, false)
        return binding.root

    }
    var rozrywka: Int = 100
    var jedzenie: Int = 0
    var higiena: Int = 100

    fun feed() {
        if (jedzenie > 0) {
            jedzenie -= 10
            rozrywka += 10
            higiena -= 5
            if (rozrywka > 100) {
                rozrywka = 100
            }
            if (higiena < 0) {
                higiena = 0
            }
            println("Yum yum! Tamagotchi is fed.")
        } else {
            println("Tamagotchi is not hungry.")
        }
    }

    fun play() {
        if (rozrywka >= 20 && higiena >= 10) {
            rozrywka -= 20
            jedzenie += 10
            higiena -= 10
            if (jedzenie > 100) {
                jedzenie = 100
            }
            if (higiena < 0) {
                higiena = 0
            }
            println("Tamagotchi is happy and playful.")
        } else {
            println("Tamagotchi is too tired or dirty to play.")
        }
    }

    fun clean() {
        if (higiena < 100) {
            higiena += 10
            rozrywka -= 5
            if (higiena > 100) {
                higiena = 100
            }
            if (rozrywka < 0) {
                rozrywka = 0
            }
            println("Tamagotchi is clean.")
        } else {
            println("Tamagotchi is already clean.")
        }
    }

    fun update() {
        rozrywka -= 5
        jedzenie += 2
        higiena -= 3

        if (rozrywka < 0) {
            rozrywka = 0
        }
        if (jedzenie > 100) {
            jedzenie = 100
        }
        if (higiena < 0) {
            higiena = 0
        }
    }
}