package com.maciejka.petfocus

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val gson = Gson()
        val sharedPref = context.getSharedPreferences("zadaniaFile", Context.MODE_PRIVATE)
        val wczytaneZadanie = sharedPref.getString("ZADANIA", null)
        val typListyZadan = object : TypeToken<MutableList<Zadanie>>() {}.type
        var dane = gson.fromJson<MutableList<Zadanie>>(wczytaneZadanie, typListyZadan)


        val builder = context?.let { it1 ->
            NotificationCompat.Builder(it1, "PETFOCUS").setContentTitle("PetFocus wzywa!")
                .setSmallIcon(R.drawable.baseline_pets_24).setContentText("Twoje zadanie: " + dane.last().nazwa +" , czeka na Ciebie!")
        }

        with(NotificationManagerCompat.from(context)) setOnClickListener@{
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return@setOnClickListener
            }
            notify(123, builder!!.build())
        }
    }
}