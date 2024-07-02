package com.example.agenda

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.ArrayAdapter
import android.widget.ListView
import android.graphics.Color
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.agenda.data.Contato

class Lista : AppCompatActivity() {

    private var contatoSelecionado:Contato? = null
    var contatos: List<Contato>? = null
    var lista: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity);

        val myToolbar: Toolbar = findViewById(R.id.toolbar)
        myToolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(myToolbar)

        lista = findViewById(R.id.lista)
    }

    override fun onResume() {
        super.onResume()
        carregaLista()
        registerForContextMenu(lista);

        lista?.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this@Lista, ContatoActivity::class.java)
            intent.putExtra("contato", contatos?.get(position))
            startActivity(intent)
        }

        lista?.setOnItemLongClickListener { _, _, posicao, _ ->
            contatoSelecionado = contatos?.get(posicao)
            false
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) {
        menuInflater.inflate(R.menu.menu_contato_contexto, menu)
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.excluir -> {
                AlertDialog.Builder(this@Lista)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Deletar")
                    .setMessage("Deseja mesmo deletar ?")
                    .setPositiveButton("Quero",
                        DialogInterface.OnClickListener { _, _ ->
                            contatoSelecionado?.let { Application.database?.contatoDao()?.delete(it) }
                            carregaLista()
                        }).setNegativeButton("Nao", null).show()
                return false
            }
            else -> return super.onContextItemSelected(item)
        }
    }

    private fun carregaLista() {
        contatos = Application.database?.contatoDao()?.getAllContatos()
        val adapter = contatos?.let {
            ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                it.toTypedArray()
            )
        }
        lista?.adapter = adapter
        adapter?.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.novo -> {
                val intent = Intent(this, ContatoActivity::class.java)
                startActivity(intent)
                return false
            }

            R.id.sincronizar -> {
                Toast.makeText(this, "Enviar", Toast.LENGTH_LONG).show()
                return false
            }

            R.id.receber -> {
                Toast.makeText(this, "Receber", Toast.LENGTH_LONG).show()
                return false
            }

            R.id.mapa -> {
                Toast.makeText(this, "Mapa", Toast.LENGTH_LONG).show()
                return false
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
}
