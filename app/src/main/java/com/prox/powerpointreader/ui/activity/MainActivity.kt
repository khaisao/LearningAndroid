package com.prox.powerpointreader.ui.activity

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.prox.powerpointreader.BuildConfig
import com.prox.powerpointreader.R
import com.prox.powerpointreader.databinding.ActivityMainBinding
import com.prox.powerpointreader.model.PPTFile
import com.prox.powerpointreader.vm.PPTFileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_dialog_permission.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    val viewModel: PPTFileViewModel by viewModels()
    private val REQUEST_CODE = 333


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate()
        if (permission()) {
            getPdfList()

        } else {
            if(SDK_INT>=Build.VERSION_CODES.R){
                openPermissionDialog()
            }
            else{
                requestPermissionDialog()
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.bottomNavigation.itemIconTintList = null
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        binding.bottomNavigation.setupWithNavController(navController)
    }

    override fun onRestart() {
        super.onRestart()
        viewModel.pptFile.observe(this){
            for(item in it){
                if(!File(item.absolutePath).exists()){
                    viewModel.deteleFile(item)
                }
            }
        }
        getPdfList()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> if (grantResults.isNotEmpty()) {
                val storage = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val read = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (storage && read) {
                    getPdfList()
                } else {
                    Toast.makeText(this, "This app needs storage permission", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 333) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    getPdfList()
                    Toast.makeText(this, "Permissions alredy granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private fun requestPermissionDialog() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION,uri)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data = Uri.parse(
                    String.format(
                        "package:%s", *arrayOf<Any>(
                            applicationContext.packageName
                        )
                    )
                )
                startActivityForResult(intent, 333)
            } catch (e: Exception) {
                val obj = Intent()
                obj.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION

                startActivityForResult(obj, 333)
            }
        } else {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE),
                REQUEST_CODE
            )
        }
    }
    private fun openPermissionDialog() {
        val view = View.inflate(this@MainActivity,R.layout.layout_dialog_permission,null)
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        view.btn_ok_per.setOnClickListener {
            dialog.dismiss()
            requestPermissionDialog()
        }
        view.btn_cancel_per.setOnClickListener {
            dialog.dismiss()
        }

    }

    private fun permission(): Boolean {
        return if (SDK_INT >= Build.VERSION_CODES.R) { // R is Android 11
            Environment.isExternalStorageManager()
        } else {
            val write = ContextCompat.checkSelfPermission(
                applicationContext, WRITE_EXTERNAL_STORAGE
            )
            val read = ContextCompat.checkSelfPermission(applicationContext, READ_EXTERNAL_STORAGE)
            (write == PackageManager.PERMISSION_GRANTED
                    && read == PackageManager.PERMISSION_GRANTED)
        }
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

    private fun loadLocate() {
        val prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = prefs.getString("My lang", "")
        if (language != null) {
            setLocate(language)
        }
    }

    private fun getPdfList() {
        val projection = arrayOf(
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.MIME_TYPE
        )
        val selection = "_data LIKE '%.pptx' OR _data LIKE '%.ppt' "
        val collection: Uri = if (SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Files.getContentUri("external")
        }
        contentResolver.query(collection, projection, selection, null, null)
            .use { cursor ->
                assert(cursor != null)
                if (cursor!!.moveToFirst()) {
                    val columnData = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)
                    val columnDate = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_ADDED)
                    do {
                        val absoluteFile = cursor.getString(columnData)
                        val file = File(absoluteFile)
                        val createDate = getDate(cursor.getString(columnDate).toLong())
                        if (createDate != null) {
                            Log.d("ljasfl",createDate)
                        }
                        if(file.exists()){
                            viewModel.addFile(
                                PPTFile(
                                    file.name,
                                    0,
                                    file.parent,
                                    file.absolutePath,
                                    createDate,
                                )
                            )
                        }
                        //you can get your pdf files
                    } while (cursor.moveToNext())
                }
            }
    }

    private fun getDate(time: Long): String? {
        return SimpleDateFormat("hh:mm,dd/MMM/yyyy",Locale.US).format(Date(time * 1000))
    }






}