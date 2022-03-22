package com.prox.powerpointreader.ui.fragment.dialog

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.prox.powerpointreader.databinding.LayoutDialogDeleteBinding
import com.prox.powerpointreader.ui.activity.MainActivity
import com.prox.powerpointreader.vm.PPTFileViewModel
import java.io.File
import java.nio.file.Files

class DeleteDialogFragment:DialogFragment() {
    private lateinit var binding:LayoutDialogDeleteBinding
    private lateinit var viewModel: PPTFileViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutDialogDeleteBinding.inflate(inflater)
        viewModel = (activity as MainActivity).viewModel
        val mArgs = arguments
        val onlyPath = mArgs?.getString("absolutePath")
        val file = File(onlyPath)

        binding.btnDelete.setOnClickListener {
            var result=false
            result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Files.deleteIfExists(file.toPath())
            } else{
                file.delete()
            }
            if (result){
                if (onlyPath != null) {
                    viewModel.deleteFileByPath(onlyPath)
                }
                dismiss()
            }
            else{
                Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        return binding.root
    }
}