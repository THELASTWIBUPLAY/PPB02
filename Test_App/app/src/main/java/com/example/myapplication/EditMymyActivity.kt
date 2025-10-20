package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.ActivityEditMymyBinding
import com.example.myapplication.databinding.ActivityMymyBinding
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

    }
    fun loadMymy() {
        lifecycleScope.launch {
            val mymy = mymyusecase.getMymy(mymyitemid)
            if (mymy == null) {
                val intent = Intent(this@EditMymyActivity, MymyActivity::class.java)
                startActivity(intent)
                finish()
            }

            binding.title.setText(mymy?.title)
            binding.description.setText(mymy?.description)
        }
    }

    override fun onStart() {
        super.onStart()
        loadMymy()
    }
}