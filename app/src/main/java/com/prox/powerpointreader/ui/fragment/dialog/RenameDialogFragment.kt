package com.prox.powerpointreader.ui.fragment.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.prox.powerpointreader.databinding.LayoutDialogRenameBinding
import com.prox.powerpointreader.model.PPTFile
import com.prox.powerpointreader.ui.activity.MainActivity
import com.prox.powerpointreader.vm.PPTFileViewModel
import java.io.File

class RenameDialogFragment : DialogFragment() {
    private lateinit var binding: LayoutDialogRenameBinding
    private lateinit var viewModel: PPTFileViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutDialogRenameBinding.inflate(inflater)
        viewModel = (activity as MainActivity).viewModel
        if (arguments != null) {
            val mArgs = arguments
            val fileFromArgs = mArgs?.getSerializable("file") as PPTFile
            var fileName = fileFromArgs.fileName
            var ext = fileFromArgs.absolutePath
            fileName = fileName.substring(0, fileName.lastIndexOf("."))
            binding.edtRename.setText(fileName)
            binding.edtRename.requestFocus()
            ext = ext.substring(ext.lastIndexOf("."))
            binding.btnOk.setOnClickListener {
                if (binding.edtRename.text.isEmpty()) {
                    Toast.makeText(context, "This cannnot be null!", Toast.LENGTH_SHORT).show()
                } else {
                    var dem = 0
                    viewModel.pptFile.observe(viewLifecycleOwner){
                        for(item in it){
                            if(item.fileName==binding.edtRename.text.toString()+ext){
                                dem++
                            }
                        }
                    }
                    if(dem==0){
                        val newPath =
                            File(fileFromArgs.parenAbsolutePath + "/" + binding.edtRename.text + ext)
                        val from = File(fileFromArgs.absolutePath)
                        val rename: Boolean = from.renameTo(newPath)
                        if (rename) {
                            val newName: String = binding.edtRename.text.toString() + ext
                            val fileLike = fileFromArgs.like
                            val fileParent = fileFromArgs.parenAbsolutePath
                            val newFileAbsolutePath =
                                fileFromArgs.parenAbsolutePath + "/" + binding.edtRename.text + ext
                            viewModel.deteleFile(fileFromArgs)
                            val newFile = PPTFile(
                                newName,
                                fileLike,
                                fileParent,
                                newFileAbsolutePath,
                                fileFromArgs.createDate,
                                fileFromArgs.aceesedDate
                            )
                            viewModel.addFile(newFile)
                            dismiss()
                        } else {
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Toast.makeText(context,"File name is already exit!",Toast.LENGTH_SHORT).show()
                    }

                }
            }
            binding.btnCancel.setOnClickListener {
                dismiss()
            }
        } else {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()

        }
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent);
            dialog!!.window?.requestFeature(Window.FEATURE_NO_TITLE);
        }
        return binding.root
    }
}