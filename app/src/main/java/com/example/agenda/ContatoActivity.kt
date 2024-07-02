package com.example.agenda

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.agenda.data.Contato
import java.util.*
import java.text.SimpleDateFormat


class ContatoActivity : AppCompatActivity() {

    var cal: Calendar = Calendar.getInstance()
    var datanascimento: Button? = null
    var txtNome: EditText? = null
    var txtEndereco: EditText? = null
    var txtTelefone: EditText? = null
    var txtSite: EditText? = null
    var txtEmail: EditText? = null
    var contato: Contato? = null
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy")

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contato)

        val myToolbar: Toolbar = findViewById(R.id.toolbar_child)

        var imgContato: ImageView = findViewById(R.id.imgContato)
        txtNome = findViewById(R.id.txtNome)
        txtEndereco = findViewById(R.id.txtEndereco)
        txtTelefone = findViewById(R.id.txtTelefone)
        txtSite = findViewById(R.id.txtSite)
        txtEmail = findViewById(R.id.txtEmail)
        var btnCadastro: Button = findViewById(R.id.btnCadastro)

        setSupportActionBar(myToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        btnCadastro.setOnClickListener {
            contato?.nome = txtNome?.text.toString()
            contato?.endereco = txtEndereco?.text.toString()
            contato?.telefone = txtTelefone?.text.toString().toLong()
            contato?.dataNascimento = cal.timeInMillis
            contato?.email = txtEmail?.text.toString()
            contato?.site = txtSite?.text.toString()

            if(contato?.id?.toInt() == 0){
                Application.database?.contatoDao()?.insert(contato!!)
            }else{
                Application.database?.contatoDao()?.update(contato!!)
            }
            finish()
        }

    }

    override fun onResume() {
        super.onResume()

        val txtDatanascimento: Button = findViewById(R.id.txtDatanascimento)

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
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        val intent = intent
        if(intent != null) {
            if (intent.getSerializableExtra("contato") != null) {
                contato = intent.getSerializableExtra("contato") as Contato
                txtNome?.setText(contato?.nome)
                txtEndereco?.setText(contato?.endereco)
                txtTelefone?.setText(contato?.telefone.toString())
                if ((contato?.dataNascimento != null)) {
                    txtDatanascimento.text = dateFormatter.format(Date(contato?.dataNascimento!!))
                }
                txtEmail?.setText(contato?.email)
                txtSite?.setText(contato?.site)
            } else {
                contato = Contato()
            }
        }
    }

    private fun updateDateInView(txtDatanascimento: Button) {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        txtDatanascimento.text = sdf.format(cal.time)
    }
}
