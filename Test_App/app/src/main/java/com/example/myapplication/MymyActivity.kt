package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.Mymyadapter
import com.example.myapplication.databinding.ActivityMymyBinding
import com.example.myapplication.usecase.Mymyusecase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MymyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMymyBinding
    private lateinit var adapter: Mymyadapter
    private val todoUseCase = Mymyusecase()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMymyBinding.inflate(layoutInflater)
        setContentView(binding.root)


        adapter = Mymyadapter(mutableListOf())
        binding.container.layoutManager = LinearLayoutManager(this)
        binding.container.adapter = adapter


        loadTodos()

        registerEvents()
    }

    private fun registerEvents() {
        binding.TombolCreateMymy.setOnClickListener{
            val intent = Intent(this, CreateMymyActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loadTodos() {
        binding.uiLoading.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val todos = todoUseCase.getMymy()
                withContext(Dispatchers.Main) {
                    adapter.updateData(todos)
                    binding.uiLoading.visibility = View.GONE
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.uiLoading.visibility = View.GONE
                }
            }
        }
    }
}