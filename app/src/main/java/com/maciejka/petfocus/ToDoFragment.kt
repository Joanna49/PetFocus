package com.maciejka.petfocus


import android.Manifest
import android.content.Context.MODE_PRIVATE
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.opengl.Visibility
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Identity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.slider.Slider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.maciejka.petfocus.databinding.FragmentToDoBinding

class ToDoFragment : Fragment() {
    //private var _binding: TodoFragmentBinding? = null
    private var _binding: FragmentToDoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //param1 = it.getString(ARG_PARAM1)
            //param2 = it.getString(ARG_PARAM2)
        }



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentToDoBinding.inflate(layoutInflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayout: LinearLayout = binding.TasksContainer

        val gson = Gson()
        val sharedPref = requireActivity().getSharedPreferences("zadaniaFile",MODE_PRIVATE)
        val wczytaneZadanie = sharedPref.getString("ZADANIA",null)
        val typListyZadan = object: TypeToken<MutableList<Zadanie>>() {}.type
        var dane = gson.fromJson<MutableList<Zadanie>>(wczytaneZadanie,typListyZadan)
        if(dane.isNullOrEmpty()){
            dane = mutableListOf<Zadanie>()
        }
        var jsonString: String
        val editor = sharedPref.edit()

        binding.btnAddTask.setOnClickListener{
            pokazFragment(NoweZadanieFragment())
        }


        binding.btnShowZrobione.setOnClickListener {
            linearLayout.removeAllViews()
            val daneZrobione = dane.filter { zad -> zad.status=="wykonane"}

            for(zadanie in daneZrobione){
                val widok = layoutInflater.inflate(R.layout.task_layout, binding.TasksContainer,false)
                widok.id = zadanie.id
                widok.findViewById<TextView>(R.id.tekstZadania).text = zadanie.nazwa
                widok.findViewById<TextView>(R.id.ukrytyTermin).text = zadanie.termin + "   " + zadanie.godzina
                ChangeColorOfTask(widok,true)

                widok.setOnLongClickListener {
                    widok.findViewById<TextView>(R.id.ukrytyTermin).visibility = View.VISIBLE
                    true
                }

                widok.setOnClickListener{
                    var aktualneZadanie = dane.find { e -> e.id==zadanie.id }
                    if(aktualneZadanie?.status == "wykonane"){
                        aktualneZadanie?.status = "niewykonane"
                        ChangeColorOfTask(widok,false)
                    }else{
                        aktualneZadanie?.status = "wykonane"
                        ChangeColorOfTask(widok,true)
                    }
                    jsonString = gson.toJson(dane)
                    editor.putString("ZADANIA",jsonString)
                    editor.apply()
                }

                widok.findViewById<ImageView>(R.id.btnZamknieciaZadania).setOnClickListener{
                    dane.remove(zadanie)
                    jsonString = gson.toJson((dane))
                    editor.putString("ZADANIA",jsonString)
                    editor.apply()
                    linearLayout.removeView(widok)
                }
                linearLayout.addView(widok,0)
            }
        }

        binding.btnShowNiezrobione.setOnClickListener {
            linearLayout.removeAllViews()
            val daneNiezrobione = dane.filter { zad -> zad.status=="niewykonane"}

            for(zadanie in daneNiezrobione){
                val widok = layoutInflater.inflate(R.layout.task_layout, binding.TasksContainer,false)
                widok.id = zadanie.id
                widok.findViewById<TextView>(R.id.tekstZadania).text = zadanie.nazwa
                widok.findViewById<TextView>(R.id.ukrytyTermin).text = zadanie.termin + "   " + zadanie.godzina
                ChangeColorOfTask(widok,false)

                widok.setOnLongClickListener {
                    widok.findViewById<TextView>(R.id.ukrytyTermin).visibility = View.VISIBLE
                    true
                }

                widok.setOnClickListener{
                    var aktualneZadanie = dane.find { e -> e.id==zadanie.id }
                    if(aktualneZadanie?.status == "wykonane"){
                        aktualneZadanie?.status = "niewykonane"
                        ChangeColorOfTask(widok,false)
                    }else{
                        aktualneZadanie?.status = "wykonane"
                        ChangeColorOfTask(widok,true)
                    }
                    jsonString = gson.toJson(dane)
                    editor.putString("ZADANIA",jsonString)
                    editor.apply()
                }

                widok.findViewById<ImageView>(R.id.btnZamknieciaZadania).setOnClickListener{
                    dane.remove(zadanie)
                    jsonString = gson.toJson((dane))
                    editor.putString("ZADANIA",jsonString)
                    editor.apply()
                    linearLayout.removeView(widok)
                }
                linearLayout.addView(widok,0)
            }
        }


        // Wypisanie zapisanych zadań
        if(!dane.isNullOrEmpty()){
            for(zadanie in dane){
                val widok = layoutInflater.inflate(R.layout.task_layout, binding.TasksContainer,false)
                widok.id = zadanie.id
                widok.findViewById<TextView>(R.id.tekstZadania).text = zadanie.nazwa
                widok.findViewById<TextView>(R.id.ukrytyTermin).text = zadanie.termin + "   " + zadanie.godzina
                if (zadanie.status == "wykonane"){
                    ChangeColorOfTask(widok,true)
                }else{
                    ChangeColorOfTask(widok,false)
                }

                widok.setOnLongClickListener {
                    widok.findViewById<TextView>(R.id.ukrytyTermin).visibility = View.VISIBLE
                    true
                }

                widok.setOnClickListener{
                    var aktualneZadanie = dane.find { e -> e.id==zadanie.id }
                    if(aktualneZadanie?.status == "wykonane"){
                        aktualneZadanie?.status = "niewykonane"
                        ChangeColorOfTask(widok,false)
                    }else{
                        aktualneZadanie?.status = "wykonane"
                        ChangeColorOfTask(widok,true)
                        DodajEnergii()
                    }
                    jsonString = gson.toJson(dane)
                    editor.putString("ZADANIA",jsonString)
                    editor.apply()
                }

                widok.findViewById<ImageView>(R.id.btnZamknieciaZadania).setOnClickListener{
                    dane.remove(zadanie)
                    jsonString = gson.toJson((dane))
                    editor.putString("ZADANIA",jsonString)
                    editor.apply()
                    linearLayout.removeView(widok)
                }
                linearLayout.addView(widok,0)
            }
        }

    }

    fun DodajEnergii(){
        val sharedPrefZwierz = requireActivity().getSharedPreferences("zwierzeFile",MODE_PRIVATE)
        val wczytanaEnergia = sharedPrefZwierz.getInt("ENERGIA",0)
        val editorEnergia = sharedPrefZwierz.edit()
        if(wczytanaEnergia+20<=100){
            editorEnergia.putInt("ENERGIA", wczytanaEnergia+20)
            editorEnergia.apply()
            requireActivity().findViewById<Slider>(R.id.kondycja).value =  wczytanaEnergia+20.toFloat()
        }
    }
    fun ChangeColorOfTask(widok: View, zmienicNaWykonane: Boolean){
        if(zmienicNaWykonane)
        {
            widok.findViewById<LinearLayout>(R.id.zadanie).setBackgroundColor(resources.getColor(R.color.wykonaneZadanie))
            widok.findViewById<TextView>(R.id.tekstZadania).setTextColor(resources.getColor(R.color.wykonaneZadanieTekst))
            widok.findViewById<ImageView>(R.id.checkZadania).setImageDrawable(resources.getDrawable(R.drawable.baseline_check_box_24))
        }
        else
        {
            widok.findViewById<LinearLayout>(R.id.zadanie).setBackgroundColor(resources.getColor(R.color.niewykonaneZadanie))
            widok.findViewById<TextView>(R.id.tekstZadania).setTextColor(resources.getColor(R.color.niewykonaneZadanieTekst))
            widok.findViewById<ImageView>(R.id.checkZadania).setImageDrawable(resources.getDrawable(R.drawable.baseline_check_box_outline_blank_24))
        }

    }

    private fun pokazFragment(fragment: Fragment){
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.replace(R.id.srodkowyKontener,fragment,null)
        fragmentTransaction.commit()
    }
}