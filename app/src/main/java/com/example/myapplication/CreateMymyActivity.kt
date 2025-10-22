package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.ActivityCreateMymyBinding
import com.example.myapplication.entity.Mymy
import com.example.myapplication.usecase.Mymyusecase
import kotlinx.coroutines.launch

class CreateMymyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateMymyBinding
    private lateinit var todoUseCase: Mymyusecase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCreateMymyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        todoUseCase = Mymyusecase()
        registerEvents()
    }

    fun registerEvents() {
        binding.tombolTambah.setOnClickListener {
            saveDataToFirestore()
        }
    }

    private fun saveDataToFirestore() {
        val mymy = Mymy(
            id = "",
            title = binding.title.text.toString(),
            description = binding.description.text.toString()
        )

        lifecycleScope.launch {
            todoUseCase.createMymy(mymy)

            Toast.makeText(this@CreateMymyActivity, "Data berhasil ditambahkan", Toast.LENGTH_LONG).show()

            toTodoListActivity()
        }
    }

    private fun toTodoListActivity() {
        val intent = Intent(this, MymyActivity::class.java)
        startActivity(intent)
        finish()
    }
}