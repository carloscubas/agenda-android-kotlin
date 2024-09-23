package com.example.agenda

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.agenda.data.Contato
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.text.SimpleDateFormat

class ContatoActivity : AppCompatActivity() {
    private val localArquivoFoto: String? = null
    private var mCurrentPhotoPath: String? = null
    var cal: Calendar = Calendar.getInstance()
    var datanascimento: Button? = null
    var txtNome: EditText? = null
    var txtEndereco: EditText? = null
    var txtTelefone: EditText? = null
    var txtSite: EditText? = null
    var txtEmail: EditText? = null
    var contato: Contato? = null
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy")
    val REQUEST_IMAGE_CAPTURE = 1
    var imgContato: ImageView? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Agenda", "ContatoActivity onCreate")
        setContentView(R.layout.activity_contato)

        val myToolbar: Toolbar = findViewById(R.id.toolbar_child)

        imgContato = findViewById(R.id.imgContato)
        txtNome = findViewById(R.id.txtNome)
        txtEndereco = findViewById(R.id.txtEndereco)
        txtTelefone = findViewById(R.id.txtTelefone)
        txtSite = findViewById(R.id.txtSite)
        txtEmail = findViewById(R.id.txtEmail)
        var btnCadastro: Button = findViewById(R.id.btnCadastro)

        setSupportActionBar(myToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        btnCadastro.setOnClickListener {
            contato?.foto = mCurrentPhotoPath
            contato?.nome = txtNome?.text.toString()
            contato?.endereco = txtEndereco?.text.toString()
            contato?.telefone = txtTelefone?.text?.let {
                it.toString().toLongOrNull()
            }
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

        imgContato?.setOnClickListener{
            dispatchTakePictureIntentSimple();
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("Agenda", "ContatoActivity onActivityResult")
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            val extras = data?.extras
            val imageBitmap = extras!!.get("data") as Bitmap
            try {
                this.storeImage(imageBitmap)
                mCurrentPhotoPath?.let {
                    Log.d("agenda", "Path new: $it")
                    readBitmapFile(it)}
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("Agenda", "ContatoActivity On resume")
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
                contato?.telefone?.let {
                    txtTelefone?.setText(it.toString())
                }
                contato?.dataNascimento?.let {
                    txtDatanascimento.text = dateFormatter.format(Date(it))
                }

                mCurrentPhotoPath?.let{
                    readBitmapFile(it)
                } ?: run{
                    contato?.foto?.let {
                        readBitmapFile(it);
                        mCurrentPhotoPath = it
                        Log.d("agenda", "Path update: $it")
                    }
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

    private fun dispatchTakePictureIntentSimple() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    private fun storeImage(image: Bitmap) {
        val pictureFile = createImageFile()
        try {
            val fos = FileOutputStream(pictureFile)
            image.compress(Bitmap.CompressFormat.PNG, 90, fos)
            fos.close()
        } catch (e: FileNotFoundException) {
            Log.d("ERRO", "File not found: " + e.message)
        } catch (e: IOException) {
            Log.d("ERRO", "Error accessing file: " + e.message)
        }
    }

    private fun readBitmapFile(path: String) {
        val f = File(path)
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888

        try {
            var bitmap = BitmapFactory.decodeStream(FileInputStream(f), null, options)
            imgContato?.setImageBitmap(bitmap)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }
}
