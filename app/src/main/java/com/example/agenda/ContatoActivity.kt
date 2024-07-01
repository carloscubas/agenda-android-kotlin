package com.example.agenda

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.util.*
import java.text.SimpleDateFormat


class ContatoActivity : AppCompatActivity() {

    var cal: Calendar = Calendar.getInstance()
    var datanascimento: Button? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contato)

        val myToolbar: Toolbar = findViewById(R.id.toolbar_child)
        val txtDatanascimento: Button = findViewById(R.id.txtDatanascimento)

        var imgContato: ImageView = findViewById(R.id.imgContato)
        var txtNome: EditText = findViewById(R.id.txtNome)
        var txtEndereco: EditText = findViewById(R.id.txtEndereco)
        var txtTelefone: EditText = findViewById(R.id.txtTelefone)
        var txtSite: EditText = findViewById(R.id.txtSite)
        var btnCadastro: Button = findViewById(R.id.btnCadastro)
        var txtEmail: EditText = findViewById(R.id.txtEmail)

        setSupportActionBar(myToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView(txtDatanascimento)
            }

        txtDatanascimento.setOnClickListener {
            DatePickerDialog(
                this@ContatoActivity,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updateDateInView(txtDatanascimento: Button) {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        txtDatanascimento.text = sdf.format(cal.time)
    }

}
