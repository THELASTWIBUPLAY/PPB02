package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.Mymyadapter
import com.example.myapplication.databinding.ActivityMymyBinding
import com.example.myapplication.entity.Mymy
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


        adapter = Mymyadapter(mutableListOf(), object : Mymyadapter.MymyItemEvents {
            override fun onDelete(mymy: Mymy) {
                val builder = AlertDialog.Builder(this@MymyActivity)
                builder.setTitle("Konfirmasi Hapus Data")
                builder.setMessage("Apakah anda ingin menghapus data?")

                builder.setPositiveButton("ya") { dialog, _->
                    lifecycleScope.launch {
                        try {
                            todoUseCase.deleteMymy(mymy.id)
                            displayMessage("Berhasil menghapus data")
                        } catch (exc: Exception) {
                            displayMessage("Gagal menghapus data")
                        }

                        loadMymys()
                    }
                }

                builder.setNeutralButton("Tidak") { dialog, _->
                    dialog.dismiss()
                }

                val dialog = builder.create()
                dialog.show()

            }

            override fun onEdit(mymy: Mymy) {
                val intent = Intent(this@MymyActivity, EditMymyActivity::class.java)
                intent.putExtra("mymy_item_id", mymy.id)
                startActivity(intent)
            }
        })
        binding.container.layoutManager = LinearLayoutManager(this)
        binding.container.adapter = adapter


        loadMymys()

        registerEvents()
    }

    private fun registerEvents() {
        binding.TombolCreateMymy.setOnClickListener{
            val intent = Intent(this, CreateMymyActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadMymys() {
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

    fun displayMessage(message: String) {
        Toast.makeText(this@MymyActivity, message, Toast.LENGTH_SHORT).show()
    }

    // MymyActivity.kt (TAMBAHAN PENTING)
    override fun onStart() {
        super.onStart()
        // Ini memastikan data dimuat ulang setiap kali kembali ke Activity ini
        loadMymys()
    }
}