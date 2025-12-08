package com.example.practica9.presentation
import com.example.practica9.R
import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : Activity() {

    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Aquí enlazamos con la vista que creaste en el paso anterior
        setContentView(R.layout.activity_main)

        val tvCounter = findViewById<TextView>(R.id.tvCounter)
        val btnTap = findViewById<Button>(R.id.btnTap)

        btnTap.setOnClickListener {
            counter++
            tvCounter.text = counter.toString()
        }
    }
}