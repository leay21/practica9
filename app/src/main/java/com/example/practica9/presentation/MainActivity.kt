package com.example.practica9.presentation

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.practica9.R
import kotlin.math.sqrt
import com.google.firebase.database.DatabaseReference // <--- NUEVO
import com.google.firebase.database.FirebaseDatabase // <--- NUEVO
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : Activity(), SensorEventListener {

    private lateinit var textView: TextView
    private lateinit var button: Button
    private var counter = 0

    // Persistencia
    private lateinit var sharedPreferences: SharedPreferences

    // Variables Sensor
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var accelerationCurrent = 0f
    private var accelerationLast = 0f
    private var shakeThreshold = 12f
    // Variable para Firebase
    private lateinit var myRef: DatabaseReference // <--- NUEVO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.tvCounter)
        button = findViewById(R.id.btnTap)
        findViewById<TextView>(R.id.tvTitle).text = "Movimientos"

        // 1. Inicializar SharedPreferences (nombre del archivo: "wear_prefs")
        sharedPreferences = getSharedPreferences("wear_prefs", Context.MODE_PRIVATE)

        // 2. Recuperar el valor guardado (si no existe, usa 0)
        counter = sharedPreferences.getInt("counter_key", 0)
        updateCounterUI()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        accelerationCurrent = SensorManager.GRAVITY_EARTH
        accelerationLast = SensorManager.GRAVITY_EARTH

        button.setOnClickListener {
            counter = 0
            saveCounter() // Guardar el reinicio
            updateCounterUI()
        }
        button.text = "Reiniciar"

        // INICIALIZAR FIREBASE
        // "movimientos" será el nombre de la carpeta en la nube donde se guarde el número
        val database = FirebaseDatabase.getInstance()
        myRef = database.getReference("movimientos")

        // Pedir permiso de notificaciones si es necesario (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM_TEST", "Error al obtener el token", task.exception)
                return@addOnCompleteListener
            }

            // Obtener el nuevo token
            val token = task.result

            // Imprimirlo en el Logcat
            Log.d("FCM_TEST", "MI TOKEN ES: $token")
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val x = it.values[0]
            val y = it.values[1]
            val z = it.values[2]

            accelerationLast = accelerationCurrent
            accelerationCurrent = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta = accelerationCurrent - accelerationLast

            if (delta > shakeThreshold || delta < -shakeThreshold) {
                incrementCounter()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun incrementCounter() {
        counter++
        saveCounter() // Guardar cada vez que aumenta
        updateCounterUI()
    }
    private fun saveCounter() {
        // 1. Guardado Local (lo que ya tenías)
        val editor = sharedPreferences.edit()
        editor.putInt("counter_key", counter)
        editor.apply()

        // 2. Guardado en Nube (NUEVO)
        // Esto sube el valor actual de 'counter' a Firebase en tiempo real
        myRef.setValue(counter)
    }

    private fun updateCounterUI() {
        textView.text = "$counter" // Solo mostramos el número para que quepa bien
    }
}