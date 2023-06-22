package com.maciejka.petfocus

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isInvisible
import com.maciejka.petfocus.databinding.FragmentPrzerwaBinding
import com.maciejka.petfocus.databinding.FragmentZwierzakBinding
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PomodoroFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PrzerwaFragment : Fragment() {
    private var _binding: FragmentPrzerwaBinding? = null
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

        binding.btnPomodoroPowrot.setOnClickListener {

            pokazFragment(PomodoroFragment())
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
        val minutes = (mTimeLeftInMillis /1000)/ 60
        val seconds = (mTimeLeftInMillis /1000)% 60

        val timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds)

        mTextViewCountDown.setText(timeLeftFormatted)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPrzerwaBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PomodoroFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PomodoroFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun pokazFragment(fragment: Fragment){
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.replace(R.id.srodkowyKontener,fragment,null)
        fragmentTransaction.commit()
    }
}