package com.maciejka.petfocus

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isInvisible
import com.google.android.material.slider.Slider
import com.maciejka.petfocus.databinding.FragmentPomodoro25Binding
import java.util.*


class Pomodoro25Fragment : Fragment() {
    private var _binding: FragmentPomodoro25Binding? = null
    private val binding get() = _binding!!
    private val START_TIME_IN_MILLIS = 600000

    private lateinit var mTextViewCountDown: TextView
    private lateinit var mButtonStartPauza: Button
    private lateinit var mButtonReset: Button

    private lateinit var mCountDownTimer: CountDownTimer

    private var mTimerRunning = false

    private var mTimeLeftInMillis = START_TIME_IN_MILLIS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mTextViewCountDown = binding.textOdliczanie

        mButtonStartPauza = binding.btnStartPauza
        mButtonReset = binding.btnReset

        mButtonStartPauza.setOnClickListener {

            if (mTimerRunning) {
                pauseTimer()
            } else {
                startTimer()
            }
        }
        mButtonReset.setOnClickListener {
            resetTimer()
        }
        updateCountDownText()

        binding.btnPrzerwa.setOnClickListener {

            pokazFragment(Przerwa25Fragment())
        }
    }
    fun startTimer() {
        mCountDownTimer = object : CountDownTimer(mTimeLeftInMillis.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mTimeLeftInMillis = millisUntilFinished.toInt()
                updateCountDownText()

            }

            override fun onFinish() {
                mTimerRunning = false
                mButtonStartPauza.text ="Start"
                mButtonStartPauza.setVisibility(View.INVISIBLE)
                mButtonReset.setVisibility(View.VISIBLE)
                DodajEnergii()
            }
        }.start()

        mTimerRunning= true
        mButtonStartPauza.text ="pauza"
        mButtonReset.setVisibility(View.INVISIBLE)
    }

    fun pauseTimer() {
        mCountDownTimer.cancel()
        mTimerRunning = false
        mButtonStartPauza.text ="Start"
        mButtonReset.setVisibility(View.VISIBLE)
    }

    fun resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS
        updateCountDownText()
        mButtonReset.setVisibility(View.INVISIBLE)
        mButtonStartPauza.setVisibility(View.VISIBLE)

    }
    fun updateCountDownText(){
        val minutes = (mTimeLeftInMillis /(1000*24))
        val seconds = (mTimeLeftInMillis /1000)% 60

        val timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds)

        mTextViewCountDown.setText(timeLeftFormatted)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPomodoro25Binding.inflate(layoutInflater, container, false)
        return binding.root
    }


    private fun pokazFragment(fragment: Fragment){
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.replace(R.id.srodkowyKontener,fragment,null)
        fragmentTransaction.commit()
    }

    fun DodajEnergii(){
        val sharedPrefZwierz = requireActivity().getSharedPreferences("zwierzeFile",
            Context.MODE_PRIVATE
        )
        val wczytanaEnergia = sharedPrefZwierz.getInt("ENERGIA",0)
        val editorEnergia = sharedPrefZwierz.edit()
        if(wczytanaEnergia+20<=100){
            editorEnergia.putInt("ENERGIA", wczytanaEnergia+20)
            editorEnergia.apply()
            requireActivity().findViewById<Slider>(R.id.kondycja).value =  wczytanaEnergia+20.toFloat()
        }
    }
}