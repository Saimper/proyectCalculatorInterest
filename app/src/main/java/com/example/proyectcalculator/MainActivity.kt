package com.example.proyectcalculator

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.*


class MainActivity : AppCompatActivity() {

    private lateinit var inputLayout: LinearLayout
    private lateinit var btnCompute: Button
    private lateinit var btnClear: Button
    private lateinit var tvResult: TextView
    private var calculationType: CalculationType? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        inputLayout = findViewById(R.id.inputLayout)
        btnCompute = findViewById(R.id.btnCompute)
        tvResult = findViewById(R.id.tvResult)
        btnClear = findViewById(R.id.btnClear)

        findViewById<Button>(R.id.btnCalculateAmount).setOnClickListener {
            setupInputs(CalculationType.AMOUNT)
        }

        findViewById<Button>(R.id.btnCalculatePrincipal).setOnClickListener {
            setupInputs(CalculationType.PRINCIPAL)
        }

        findViewById<Button>(R.id.btnCalculateRate).setOnClickListener {
            setupInputs(CalculationType.RATE)
        }

        findViewById<Button>(R.id.btnCalculateTime).setOnClickListener {
            setupInputs(CalculationType.TIME)
        }

        btnCompute.setOnClickListener {
            performCalculation()
        }
        btnClear.setOnClickListener {
            clearInputs()
        }
    }

    private fun setupInputs(type: CalculationType) {
        calculationType = type
        inputLayout.removeAllViews()

        when (type) {
            CalculationType.AMOUNT -> {
                addEditText("Capital Inicial (Co)")
                addEditText("Tasa de Interés (i)")
                addEditText("Tiempo (n) en años")
            }
            CalculationType.PRINCIPAL -> {
                addEditText("Monto Final (Cn)")
                addEditText("Tasa de Interés (i)")
                addEditText("Tiempo (n) en años")
            }
            CalculationType.RATE -> {
                addEditText("Monto Final (Cn)")
                addEditText("Capital Inicial (Co)")
                addEditText("Tiempo (n) en años")
            }
            CalculationType.TIME -> {
                addEditText("Monto Final (Cn)")
                addEditText("Capital Inicial (Co)")
                addEditText("Tasa de Interés (i)")
            }
        }

        inputLayout.visibility = View.VISIBLE
        btnCompute.visibility = View.VISIBLE
        tvResult.visibility = View.GONE
        btnClear.visibility = View.VISIBLE
    }

    private fun addEditText(hint: String) {
        val editText = EditText(this)
        editText.hint = hint
        editText.inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        inputLayout.addView(editText)
    }

    private fun performCalculation() {
        val inputs = (0 until inputLayout.childCount).map {
            (inputLayout.getChildAt(it) as EditText).text.toString().toDoubleOrNull()
        }

        if (inputs.any { it == null }) {
            Toast.makeText(this, "Por favor, ingrese todos los valores correctamente.", Toast.LENGTH_SHORT).show()
            return
        }

        val result = when (calculationType) {
            CalculationType.AMOUNT -> calculateAmount(inputs[0]!!, inputs[1]!!, inputs[2]!!)
            CalculationType.PRINCIPAL -> calculatePrincipal(inputs[0]!!, inputs[1]!!, inputs[2]!!)
            CalculationType.RATE -> calculateRate(inputs[0]!!, inputs[1]!!, inputs[2]!!)
            CalculationType.TIME -> calculateTime(inputs[0]!!, inputs[1]!!, inputs[2]!!)
            else -> null
        }

        tvResult.text = "Resultado: $result"
        tvResult.visibility = View.VISIBLE
    }

    private fun calculateAmount(co: Double, i: Double, n: Double): Double {
        return co * (1 + n * i)
    }

    private fun calculatePrincipal(cn: Double, i: Double, n: Double): Double {
        return cn / (1 + n * i)
    }

    private fun calculateRate(cn: Double, co: Double, n: Double): Double {
        return (cn - co) / (co * n)
    }

    private fun calculateTime(cn: Double, co: Double, i: Double): Double {
        return (cn - co) / (co * i)
    }
    private fun clearInputs() {
        inputLayout.removeAllViews()
        inputLayout.visibility = View.GONE
        btnCompute.visibility = View.GONE
        btnClear.visibility = View.GONE
        tvResult.visibility = View.GONE
    }

    enum class CalculationType {
        AMOUNT, PRINCIPAL, RATE, TIME
    }
}

//By: Joel Solano