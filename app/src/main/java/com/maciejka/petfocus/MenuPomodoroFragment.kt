package com.maciejka.petfocus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maciejka.petfocus.databinding.FragmentMenuPomodoroBinding
import com.maciejka.petfocus.databinding.FragmentPomodoroBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MenuPomodoroFragment : Fragment() {
    private var _binding: FragmentMenuPomodoroBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuPomodoroBinding.inflate(layoutInflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btn50.setOnClickListener {
            pokazFragment(PomodoroFragment())
        }

        binding.btn25.setOnClickListener {
            pokazFragment(Pomodoro25Fragment())
        }

        binding.btnHelp.setOnClickListener {
            pokazFragment(PomocPomodoroFragment())
        }
    }

    private fun pokazFragment(fragment: Fragment){
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.replace(R.id.srodkowyKontener,fragment,null)
        fragmentTransaction.commit()
    }
}