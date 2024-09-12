package com.example.agenda

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.ArrayAdapter
import android.widget.ListView
import android.graphics.Color
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class Lista : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity);

        val contatos = arrayOf("Maria", "José", "Carlos")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contatos)

        val lista: ListView = findViewById(R.id.lista)
        lista.setAdapter(adapter);
    }
}
