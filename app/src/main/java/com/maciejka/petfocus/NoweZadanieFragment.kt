package com.maciejka.petfocus

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.icu.util.LocaleData
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.maciejka.petfocus.databinding.FragmentNoweZadanieBinding
import com.maciejka.petfocus.databinding.FragmentToDoBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class NoweZadanieFragment : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private var _binding: FragmentNoweZadanieBinding? = null
    private val binding get() = _binding!!
    private val calendar = Calendar.getInstance()
    private val formaterDate = SimpleDateFormat("d MMMM yyyy",Locale.UK)
    private val formaterTime = SimpleDateFormat("HH:mm",Locale.UK)
    private val today = calendar.time

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoweZadanieBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gson = Gson()
        val sharedPref = requireActivity().getSharedPreferences("zadaniaFile", Context.MODE_PRIVATE)
        val wczytaneZadanie = sharedPref.getString("ZADANIA",null)
        val typListyZadan = object: TypeToken<MutableList<Zadanie>>() {}.type
        var dane = gson.fromJson<MutableList<Zadanie>>(wczytaneZadanie,typListyZadan)
        if(dane.isNullOrEmpty()){
            dane = mutableListOf<Zadanie>()
        }
        var jsonString: String
        val editor = sharedPref.edit()

        binding.btnZapiszZadanie.setOnClickListener {
            val zadanie = Zadanie(View.generateViewId(),binding.nazwaZadania.text.toString(),null, null, "niewykonane")
            dane.plusAssign(zadanie)
            jsonString = gson.toJson(dane)
            editor.putString("ZADANIA",jsonString)
            editor.apply()

            pokazFragment(ToDoFragment())
        }

        binding.termin.setOnClickListener {
            context?.let { it1 -> DatePickerDialog(it1,this,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show() }
        }

        binding.godzina.setOnClickListener {
            TimePickerDialog(context,this,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show()
        }
    }

    private fun pokazFragment(fragment: Fragment){
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.replace(R.id.srodkowyKontener,fragment,null)
        fragmentTransaction.commit()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(year,month,dayOfMonth)
        if(calendar.time==today){
            binding.termin.text = "Termin: Dziś"
        }else{
            binding.termin.text = "Termin: " + formaterDate.format(calendar.timeInMillis)
        }
        TimePickerDialog(context,this,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
        }
        binding.godzina.text = "Godzina: " + formaterTime.format(calendar.timeInMillis)
    }
}