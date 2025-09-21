package com.example.simplenoteapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.simplenoteapp.R
import com.example.simplenoteapp.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        binding.ivSplashImg.alpha = 0f
//        binding.ivSplashImg.animate()
//            .alpha(1f)
//            .setDuration(2000)
//            .withEndAction {
//            val move = Intent(this, MainActivity::class.java)
//            startActivity(move)
//            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
//            finish()
//        }

//        binding.ivSplashImg.apply {
//            alpha = 0f
//            visibility = View.VISIBLE
//            animate()
//                .alpha(1f)
//                .setDuration(2000)
//                .start()
//        }

        binding.tvSplashText.apply {
            translationY = 100f
            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(2000)
                .setStartDelay(500)
                .withEndAction {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    finish()
                }
                .start()
        }


    }
}