package com.maciejka.petfocus


import android.content.Context.MODE_PRIVATE
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Identity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
            /*val widok = layoutInflater.inflate(R.layout.task_layout, null)
            widok.findViewById<TextView>(R.id.tekstZadania).text = "Nowe zadanie"
            linearLayout.addView(widok,0)
            */
            pokazFragment(NoweZadanieFragment())

            /*
            val zadanie = Zadanie(View.generateViewId(),"Nowe zadanie",null, null, "niewykonane")
            dane.plusAssign(zadanie)
            jsonString = gson.toJson(dane)
            editor.putString("ZADANIA",jsonString)
            editor.apply()

            val widok = layoutInflater.inflate(R.layout.task_layout, binding.TasksContainer,false)
            widok.id = zadanie.id
            widok.findViewById<TextView>(R.id.tekstZadania).text = zadanie.nazwa
            ChangeColorOfTask(widok,false)

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

             */
        }


        binding.btnShowZrobione.setOnClickListener {
            linearLayout.removeAllViews()
            val daneZrobione = dane.filter { zad -> zad.status=="wykonane"}

            for(zadanie in daneZrobione){
                val widok = layoutInflater.inflate(R.layout.task_layout, binding.TasksContainer,false)
                widok.id = zadanie.id
                widok.findViewById<TextView>(R.id.tekstZadania).text = zadanie.nazwa
                ChangeColorOfTask(widok,true)

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
                ChangeColorOfTask(widok,false)

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
        //var listaZadan = mutableListOf<Zadanie>()
        //var zadanie1 = Zadanie("Nowe1",null, null)
        //var zadanie2 = Zadanie("Nowe2",null, null)
        //var zadanie3 = Zadanie("Nowe3",null, null)

        //listaZadan.plusAssign(zadanie1)
        //listaZadan.plusAssign(zadanie2)
        //listaZadan.plusAssign(zadanie3)
/*
        for(zad in listaZadan){
            val widok = layoutInflater.inflate(R.layout.task_layout, null)
            widok.findViewById<TextView>(R.id.tekstZadania).text = zad.nazwa
            linearLayout.addView(widok,0)
        }
*/
        // Zapianie danych
        //var gson = Gson()
        //var jsonString = gson.toJson(listaZadan)
        //val sharedPref = requireActivity().getSharedPreferences("zadaniaFile",MODE_PRIVATE)
        //val editor = sharedPref.edit()
        //editor.putString("ZADANIA",jsonString)
        //editor.apply()

        // Wczytanie danych
        //val wczytaneZadanie = sharedPref.getString("ZADANIA",null)
        //val typListyZadan = object: TypeToken<MutableList<Zadanie>>() {}.type
        //val dane = gson.fromJson<MutableList<Zadanie>>(wczytaneZadanie,typListyZadan)
        //val wczytaneDane = gson.fromJson(wczytaneZadanie, Zadanie::class.java)
        //val widok = layoutInflater.inflate(R.layout.task_layout, null)
        //widok.findViewById<TextView>(R.id.tekstZadania).text = wczytaneDane.nazwa
        //linearLayout.addView(widok,0)

        // Wypisanie zapisanych zadań
        if(!dane.isNullOrEmpty()){
            for(zadanie in dane){
                val widok = layoutInflater.inflate(R.layout.task_layout, binding.TasksContainer,false)
                widok.id = zadanie.id
                widok.findViewById<TextView>(R.id.tekstZadania).text = zadanie.nazwa
                if (zadanie.status == "wykonane"){
                    ChangeColorOfTask(widok,true)
                }else{
                    ChangeColorOfTask(widok,false)
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


        /*
        val linearLayout: LinearLayout = binding.TasksContainer
        val textView = TextView(context)
        textView.text = "Dynamiczny TextView"

        textView.setTextColor(Color.RED)
        textView.textSize = 24f
        linearLayout.addView(textView,0)

        val textView2 = TextView(context)
        textView2.text = "Drugi TextView po raz drugi"

        textView2.setTextColor(Color.RED)
        textView2.textSize = 24f
        linearLayout.addView(textView2,0)

        val widok1 = layoutInflater.inflate(R.layout.task_layout, null)
        widok1.findViewById<TextView>(R.id.tekstZadania).text = "Tekst"


        val widok2 = layoutInflater.inflate(R.layout.task_layout, null)
        widok2.findViewById<TextView>(R.id.tekstZadania).text = "Tak tak byczq!"

        val widok3 = layoutInflater.inflate(R.layout.task_layout, null)
        widok3.findViewById<TextView>(R.id.tekstZadania).text = "LOLOLOLO"
        linearLayout.addView(widok1,0)
        linearLayout.addView(widok2,0)

        */
    }

    fun ChangeColorOfTask(widok: View, zmienicNaWykonane: Boolean){
        if(zmienicNaWykonane)
        {
            widok.setBackgroundColor(resources.getColor(R.color.wykonaneZadanie))
            widok.findViewById<TextView>(R.id.tekstZadania).setTextColor(resources.getColor(R.color.wykonaneZadanieTekst))
            widok.findViewById<ImageView>(R.id.checkZadania).setImageDrawable(resources.getDrawable(R.drawable.baseline_check_box_24))
        }
        else
        {
            widok.setBackgroundColor(resources.getColor(R.color.niewykonaneZadanie))
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