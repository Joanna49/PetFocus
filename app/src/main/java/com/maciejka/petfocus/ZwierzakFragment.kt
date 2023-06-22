package com.maciejka.petfocus

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.slider.Slider
import com.maciejka.petfocus.databinding.FragmentZwierzakBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.sql.Time
import java.time.*
import java.time.format.DateTimeFormatter
import kotlin.concurrent.fixedRateTimer
import kotlin.math.absoluteValue


class ZwierzakFragment : Fragment() {
    private var _binding:FragmentZwierzakBinding?=null
    private val binding get()= _binding!!

    var rozrywka: Int = 0
    var jedzenie: Int = 0
    var higiena: Int = 0

    var isBrudny = false
    lateinit var sharedPrefZwierz:SharedPreferences

    var textInToast = "Click!"
    @RequiresApi(Build.VERSION_CODES.O)
    var actualTime = LocalDateTime.now(ZoneId.of("Europe/Warsaw"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentZwierzakBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPrefZwierz = requireActivity().getSharedPreferences("zwierzeFile",
            Context.MODE_PRIVATE
        )

        binding.btnKarmienie.setOnClickListener{
            feed()
            Toast.makeText(context, textInToast, Toast.LENGTH_SHORT).show()
        }
        binding.btnMycie.setOnClickListener{
            clean()
            Toast.makeText(context, textInToast, Toast.LENGTH_SHORT).show()
        }
        binding.btnZabawa.setOnClickListener{
            play()
            Toast.makeText(context, textInToast, Toast.LENGTH_SHORT).show()
        }

        rozrywka = sharedPrefZwierz.getInt("ROZRYWKA",0)
        jedzenie = sharedPrefZwierz.getInt("JEDZENIE",0)
        higiena = sharedPrefZwierz.getInt("HIGIENA",0)

        if(rozrywka<0){
            rozrywka = 10
        }
        if(jedzenie<0){
            jedzenie = 10
        }
        if(higiena<0){
            higiena = 10
        }

        AktualizujSlideryZwierzaka()

        if(higiena<=0){
            binding.imageViewWelcome.setImageResource(R.drawable.zwierzakbrudny)
            isBrudny=true
        }

        var lastUsingFragmentZwierz = sharedPrefZwierz.getString("OSTATNI_CZAS",
            LocalDateTime.now(ZoneId.of("Europe/Warsaw")).toString())
        lastUsingFragmentZwierz = lastUsingFragmentZwierz!!.replace("T"," ")
        val formater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        println(lastUsingFragmentZwierz)
        val lastTime = LocalDateTime.parse(lastUsingFragmentZwierz,formater)

        actualTime = LocalDateTime.now(ZoneId.of("Europe/Warsaw"))

        val diffDay = actualTime.dayOfYear - lastTime.dayOfYear
        val diffHour = actualTime.hour - lastTime.hour
        val diffMinute = actualTime.minute - lastTime.minute
        val diffTime = diffMinute + diffHour*60 + diffDay*24*60
        println(diffTime)
        GlobalScope.launch {
            repeat((diffTime/20).toInt()) {
                update()
            }
        }
        fixedRateTimer("timer",false,0,1000*60*20){
            update()
        }
    }

    fun feed() {
        val wczytanaEnergia = sharedPrefZwierz.getInt("ENERGIA",0)
        if(wczytanaEnergia>10){
            if (jedzenie < 100) {
                jedzenie += 30
                rozrywka -= 10
                higiena -= 5
                if (rozrywka < 0) {
                    rozrywka = 0
                }
                if (higiena < 0) {
                    higiena = 0
                }
                if(jedzenie > 100){
                    jedzenie = 100
                }
                println("Yum yum! Tamagotchi is fed.")
                textInToast = "Kitku sobie zjadł!"
                AktualizujSlideryZwierzaka()
                GlobalScope.launch(Dispatchers.Main) {
                    OdejmijEnergii(wczytanaEnergia)
                    if(isBrudny){
                        binding.imageViewWelcome.setImageResource(R.drawable.zwierzakbrudnyje)
                        delay(2_000)
                        binding.imageViewWelcome.setImageResource(R.drawable.zwierzakbrudny)
                    }else{
                        binding.imageViewWelcome.setImageResource(R.drawable.zwierzakje)
                        delay(2_000)
                        HappyAndDirtyFaceChanger()
                    }
                }
            } else {
                textInToast = "Kitku nie jest głodny!"
                println("Tamagotchi is not hungry.")
            }
        }

    }

    fun play() {
        val wczytanaEnergia = sharedPrefZwierz.getInt("ENERGIA",0)
        if (wczytanaEnergia>10){
            if (rozrywka < 100 && higiena >= 10) {
                rozrywka += 25
                jedzenie -= 10
                higiena -= 10
                if (jedzenie < 0) {
                    jedzenie = 0
                }
                if (higiena < 0) {
                    higiena = 0
                }
                if(rozrywka>100){
                    rozrywka = 100
                }
                println("Tamagotchi is happy and playful.")
                textInToast = "Kitku pobawił się!"
                AktualizujSlideryZwierzaka()
                GlobalScope.launch(Dispatchers.Main) {
                    OdejmijEnergii(wczytanaEnergia)
                    if(isBrudny) {
                        binding.imageViewWelcome.setImageResource(R.drawable.zwierzakbrudnybawi)
                        delay(2_000)
                        binding.imageViewWelcome.setImageResource(R.drawable.zwierzakbrudny)
                    }else{
                        binding.imageViewWelcome.setImageResource(R.drawable.zwierzakbawi)
                        delay(2_000)
                        HappyAndDirtyFaceChanger()
                    }
                }
            } else {
                textInToast = "Kitku jest zmęczony albo brudny!"
                println("Tamagotchi is too tired or dirty to play.")
            }
        }

    }

    fun clean() {
        val wczytanaEnergia = sharedPrefZwierz.getInt("ENERGIA",0)
        if (wczytanaEnergia>10){
            if (higiena < 100) {
                higiena += 100
                rozrywka -= 5
                if (higiena > 100) {
                    higiena = 100
                }
                if (rozrywka < 0) {
                    rozrywka = 0
                }
                println("Tamagotchi is clean.")
                textInToast = "Kitku został umyty!"
                AktualizujSlideryZwierzaka()
                GlobalScope.launch(Dispatchers.Main){
                    OdejmijEnergii(wczytanaEnergia)
                    binding.imageViewWelcome.setImageResource(R.drawable.zwierzakczysty)
                    delay(2_000)
                    HappyAndDirtyFaceChanger()
                    isBrudny=false
                }
            } else {
                textInToast = "Kitku jest już czysty!"
                println("Tamagotchi is already clean.")
            }
        }
    }

    fun update() {
        rozrywka -= 5
        jedzenie -= 2
        higiena -= 3

        if (rozrywka < 0) {
            rozrywka = 0
        }
        if (jedzenie < 0) {
            jedzenie = 100
        }
        if (higiena < 0) {
            higiena = 0
        }
        if(higiena < 20){
            binding.imageViewWelcome.setImageResource(R.drawable.zwierzakbrudny)
            isBrudny = true
        }

        AktualizujSlideryZwierzaka()

    }

    fun OdejmijEnergii(wczytanaEnergia:Int){
        val editorEnergia = sharedPrefZwierz.edit()
        editorEnergia.putInt("ENERGIA", wczytanaEnergia-10)
        editorEnergia.apply()
        requireActivity().findViewById<Slider>(R.id.kondycja).value =  wczytanaEnergia-10.toFloat()
    }

    fun AktualizujSlideryZwierzaka(){
        binding.karmienieSlider.value = jedzenie.toFloat()
        binding.zabawaSlider.value = rozrywka.toFloat()
        binding.mycieSlider.value = higiena.toFloat()
    }

    fun HappyAndDirtyFaceChanger(){
        if(jedzenie > 60 && rozrywka > 60 && higiena > 60){
            binding.imageViewWelcome.setImageResource(R.drawable.zwierzewesole2)
        }else if(higiena < 20){
            binding.imageViewWelcome.setImageResource(R.drawable.zwierzakbrudny)
            isBrudny = true
        }
        else{
            binding.imageViewWelcome.setImageResource(R.drawable.zwierzesmutne2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPause() {
        super.onPause()
        val editorEnergia = sharedPrefZwierz.edit()
        editorEnergia.putInt("ROZRYWKA", rozrywka)
        editorEnergia.putInt("JEDZENIE", jedzenie)
        editorEnergia.putInt("HIGIENA", higiena)
        editorEnergia.putString("OSTATNI_CZAS",actualTime.toString())
        editorEnergia.apply()
    }
}