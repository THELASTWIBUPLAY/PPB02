package com.example.myapplication

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
import kotlinx.coroutines.launch

class MymyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMymyBinding
    private lateinit var mymyusecase: Mymyusecase
    private lateinit var mymyadapter: Mymyadapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMymyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mymyusecase = Mymyusecase()
//        siapkan kebutuhan recyclerciew terlebih dahulu
        setupRecyclerView()


//        inisiasi mengambil data dari firestore
        initializeData()

    }

    private fun setupRecyclerView() {
        mymyadapter = Mymyadapter(mutableListOf())
        binding.container.apply {
            adapter = mymyadapter
            layoutManager = LinearLayoutManager(this@MymyActivity)
        }
    }

    private fun initializeData() {
        lifecycleScope.launch {
            // sembunyikan tambilan recyclerview terlebih dahulu dan tampilkan ui loading
            binding.container.visibility = View.GONE
            binding.uiLoading.visibility = View.VISIBLE

            try {
                //  ambil data dariu firebase
                var mymyList = mymyusecase.getMymy()

                // jika sudah mendapatkan data dan tidak ada error tampilkan kembali recyclerview dan sembunyikan ui loading
                binding.uiLoading.visibility = View.GONE
                binding.container.visibility = View.VISIBLE

                // update data yang ada di adapter
                mymyadapter.updateData(mymyList)

            } catch (e: Exception) {
                Toast.makeText(this@MymyActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }

    }
}