package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.ActivityEditMymyBinding
import com.example.myapplication.databinding.ActivityMymyBinding
import com.example.myapplication.entity.Mymy
import com.example.myapplication.usecase.Mymyusecase
import kotlinx.coroutines.launch

class EditMymyActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditMymyBinding
    private lateinit var mymyitemid : String
    private lateinit var mymyusecase: Mymyusecase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityEditMymyBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mymyitemid = intent.getStringExtra("mymy_item_id").toString()
        mymyusecase = Mymyusecase()

        binding.tombolEdit.setOnClickListener {
            lifecycleScope.launch {
                val title = binding.title.text.toString()
                val description = binding.description.text.toString()
                val payload = Mymy(
                    id = mymyitemid,
                    title = title,
                    description = description
                )

                try {
                    mymyusecase.updateMymy(payload)
                    displayMessage("Berhasil memperbarui data")
                    back()
                } catch (exc: Exception) {
                    displayMessage("Gagal memperbarui data My : ${exc.message}")
                }
            }
        }

    }

    fun loadMymy() {
        lifecycleScope.launch {
            val mymy = mymyusecase.getMymy(mymyitemid)

            if (mymy == null) {
                displayMessage("Data yang akan di edit tidak tersedia di server!")
                back()
            }

            binding.title.setText(mymy?.title)
            binding.description.setText(mymy?.description)
        }
    }

    fun back() {
        val intent = Intent(this, MymyActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun displayMessage(message: String) {
        Toast.makeText( this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        loadMymy()
    }
}