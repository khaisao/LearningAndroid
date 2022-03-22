package com.prox.powerpointreader.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.prox.powerpointreader.databinding.FragmentPersonBinding
import com.prox.powerpointreader.ui.activity.LanguageActivity

class PersonFragment:Fragment() {
    private lateinit var binding :FragmentPersonBinding
    private var currentLangCode: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPersonBinding.inflate(inflater)
        currentLangCode = resources.configuration.locale.language;
        binding.llLanguage.setOnClickListener {
            val intent = Intent(context,LanguageActivity::class.java)
            startActivity(intent)
        }
        binding.llFeedback.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val recipients = arrayOf("testerprox@gmail.com")
            intent.putExtra(Intent.EXTRA_EMAIL, recipients)
            intent.putExtra(Intent.EXTRA_SUBJECT, "PPT Reader Feedback")
            intent.putExtra(Intent.EXTRA_TEXT, "")
            intent.putExtra(Intent.EXTRA_CC, "testerprox@gmail.com")
            intent.type = "text/html"
            intent.setPackage("com.google.android.gm")
            startActivity(Intent.createChooser(intent, "Send mail"))
        }
        binding.llMoreApp.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Andromeda+App"))
            startActivity(intent)
        }
        binding.llPrivacy.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://hellowordapp.github.io/policy/privacy.html"))
            startActivity(intent)
        }
        binding.llShareApp.setOnClickListener {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Power Point Reader")
                var shareMessage = "https://play.google.com/store/apps/details?id=com.documents.reader.ppt.txt.pdf.viewer"
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent.createChooser(shareIntent, "choose one"))
            } catch (e: Exception) {
                //e.toString();
            }
        }

        Log.d("cgnfgnfgn","Ceatte")
        return binding.root
    }
}