package com.prox.powerpointreader.ui.activity

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.prox.powerpointreader.R
import com.prox.powerpointreader.databinding.ActivityLanguageBinding
import java.util.*

class LanguageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLanguageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ivBack.setOnClickListener {
            finish()
        }
        val prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = prefs.getString("My lang", "")
        if (language == "vi") {
            setTickVi()
        }
        if (language == "en_US") {
            setTickEng()
        }
        if (language == "nl") {
            setTickNl()
        }
        if (language == "fr") {
            setTickFr()
        }
        if (language == "hi") {
            setTickHi()
        }
        if (language == "in") {
            setTickIn()
        }
        if (language == "ja") {
            setTickJa()
        }
        if (language == "pt") {
            setTickPt()
        }
        if (language == "es") {
            setTickSp()
        }
        Log.d("asjg", language.toString())
        binding.tvVie.setOnClickListener {
            setLocate("vi")
            setTickVi()
            if (language != "vi") {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent)
            }
        }
        binding.tvEng.setOnClickListener {
            setLocate("en_US")
            setTickEng()
            if (language != "en_US") {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent)
            }
        }
        binding.tvDu.setOnClickListener {
            //Dutch is nl
            setLocate("nl")
            setTickNl()
            if (language != "nl") {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent)
            }
        }
        binding.tvFr.setOnClickListener {
            setLocate("fr")
            setTickFr()
            if (language != "fr") {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent)
            }
        }
        binding.tvHi.setOnClickListener {
            setLocate("hi")
            setTickHi()
            if (language != "hi") {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent)
            }
        }
        binding.tvIn.setOnClickListener {
            setLocate("in")
            setTickIn()
            if (language != "in") {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent)
            }
        }
        binding.tvJa.setOnClickListener {
            setLocate("ja")
            setTickJa()
            if (language != "ja") {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent)
            }
        }
        binding.tvPo.setOnClickListener {
            setLocate("pt")
            setTickPt()
            if (language != "pt") {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent)
            }
        }
        binding.tvSp.setOnClickListener {
            //sp is es
            setLocate("es")
            setTickSp()
            if (language != "es") {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent)
            }
        }


    }

    private fun setTickEng() {
        binding.ivEng.visibility = android.view.View.VISIBLE
        binding.ivVi.visibility = android.view.View.INVISIBLE
        binding.ivDu.visibility = android.view.View.INVISIBLE
        binding.ivFr.visibility = android.view.View.INVISIBLE
        binding.ivHi.visibility = android.view.View.INVISIBLE
        binding.ivIn.visibility = android.view.View.INVISIBLE
        binding.ivJa.visibility = android.view.View.INVISIBLE
        binding.ivPo.visibility = android.view.View.INVISIBLE
        binding.ivSp.visibility = android.view.View.INVISIBLE
    }

    private fun setTickVi() {
        binding.ivVi.visibility = android.view.View.VISIBLE
        binding.ivEng.visibility = android.view.View.INVISIBLE
        binding.ivDu.visibility = android.view.View.INVISIBLE
        binding.ivFr.visibility = android.view.View.INVISIBLE
        binding.ivHi.visibility = android.view.View.INVISIBLE
        binding.ivIn.visibility = android.view.View.INVISIBLE
        binding.ivJa.visibility = android.view.View.INVISIBLE
        binding.ivPo.visibility = android.view.View.INVISIBLE
        binding.ivSp.visibility = android.view.View.INVISIBLE
    }
    private fun setTickJa() {
        binding.ivVi.visibility = android.view.View.INVISIBLE
        binding.ivEng.visibility = android.view.View.INVISIBLE
        binding.ivDu.visibility = android.view.View.INVISIBLE
        binding.ivFr.visibility = android.view.View.INVISIBLE
        binding.ivHi.visibility = android.view.View.INVISIBLE
        binding.ivIn.visibility = android.view.View.INVISIBLE
        binding.ivJa.visibility = android.view.View.VISIBLE
        binding.ivPo.visibility = android.view.View.INVISIBLE
        binding.ivSp.visibility = android.view.View.INVISIBLE
    }
    private fun setTickFr() {
        binding.ivVi.visibility = android.view.View.INVISIBLE
        binding.ivEng.visibility = android.view.View.INVISIBLE
        binding.ivDu.visibility = android.view.View.INVISIBLE
        binding.ivFr.visibility = android.view.View.VISIBLE
        binding.ivHi.visibility = android.view.View.INVISIBLE
        binding.ivIn.visibility = android.view.View.INVISIBLE
        binding.ivJa.visibility = android.view.View.INVISIBLE
        binding.ivPo.visibility = android.view.View.INVISIBLE
        binding.ivSp.visibility = android.view.View.INVISIBLE
    }
    private fun setTickHi() {
        binding.ivVi.visibility = android.view.View.INVISIBLE
        binding.ivEng.visibility = android.view.View.INVISIBLE
        binding.ivDu.visibility = android.view.View.INVISIBLE
        binding.ivFr.visibility = android.view.View.INVISIBLE
        binding.ivHi.visibility = android.view.View.VISIBLE
        binding.ivIn.visibility = android.view.View.INVISIBLE
        binding.ivJa.visibility = android.view.View.INVISIBLE
        binding.ivPo.visibility = android.view.View.INVISIBLE
        binding.ivSp.visibility = android.view.View.INVISIBLE
    }
    private fun setTickNl() {
        binding.ivVi.visibility = android.view.View.INVISIBLE
        binding.ivEng.visibility = android.view.View.INVISIBLE
        binding.ivDu.visibility = android.view.View.VISIBLE
        binding.ivFr.visibility = android.view.View.INVISIBLE
        binding.ivHi.visibility = android.view.View.INVISIBLE
        binding.ivIn.visibility = android.view.View.INVISIBLE
        binding.ivJa.visibility = android.view.View.INVISIBLE
        binding.ivPo.visibility = android.view.View.INVISIBLE
        binding.ivSp.visibility = android.view.View.INVISIBLE
    }
    private fun setTickPt() {
        binding.ivVi.visibility = android.view.View.INVISIBLE
        binding.ivEng.visibility = android.view.View.INVISIBLE
        binding.ivDu.visibility = android.view.View.INVISIBLE
        binding.ivFr.visibility = android.view.View.INVISIBLE
        binding.ivHi.visibility = android.view.View.INVISIBLE
        binding.ivIn.visibility = android.view.View.INVISIBLE
        binding.ivJa.visibility = android.view.View.INVISIBLE
        binding.ivPo.visibility = android.view.View.VISIBLE
        binding.ivSp.visibility = android.view.View.INVISIBLE
    }
    private fun setTickSp() {
        binding.ivVi.visibility = android.view.View.INVISIBLE
        binding.ivEng.visibility = android.view.View.INVISIBLE
        binding.ivDu.visibility = android.view.View.INVISIBLE
        binding.ivFr.visibility = android.view.View.INVISIBLE
        binding.ivHi.visibility = android.view.View.INVISIBLE
        binding.ivIn.visibility = android.view.View.INVISIBLE
        binding.ivJa.visibility = android.view.View.INVISIBLE
        binding.ivPo.visibility = android.view.View.INVISIBLE
        binding.ivSp.visibility = android.view.View.VISIBLE
    }
    private fun setTickIn() {
        binding.ivVi.visibility = android.view.View.INVISIBLE
        binding.ivEng.visibility = android.view.View.INVISIBLE
        binding.ivDu.visibility = android.view.View.INVISIBLE
        binding.ivFr.visibility = android.view.View.INVISIBLE
        binding.ivHi.visibility = android.view.View.INVISIBLE
        binding.ivIn.visibility = android.view.View.VISIBLE
        binding.ivJa.visibility = android.view.View.INVISIBLE
        binding.ivPo.visibility = android.view.View.INVISIBLE
        binding.ivSp.visibility = android.view.View.INVISIBLE
    }


    private fun setLocate(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        val editor = getSharedPreferences("Settings", MODE_PRIVATE).edit()
        editor.putString("My lang", lang)
        editor.apply()
    }

}