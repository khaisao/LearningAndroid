package com.prox.powerpointreader.ui.fragment.dialog

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.prox.powerpointreader.databinding.LayoutDialogSortBinding
import com.prox.powerpointreader.model.PPTFile

class SortDialogFragment:DialogFragment() {
    private lateinit var binding:LayoutDialogSortBinding
    companion object{
        val MY_ACTION = "com.prox.powerpointreader"
        val SortName = 1
        val SortCreate = 2
        val SortAceesed = 3
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutDialogSortBinding.inflate(inflater)
        var sort_action = 1
        if (arguments != null) {
            val mArgs = arguments
            sort_action = mArgs?.getInt("sort_action",1)!!

        }
        if(sort_action == 1 ){
            binding.ivTickName.visibility = View.VISIBLE
            binding.ivTickCreate.visibility = View.INVISIBLE
            binding.ivTickAcc.visibility = View.INVISIBLE
        }
        if(sort_action == 2 ){
            binding.ivTickName.visibility = View.INVISIBLE
            binding.ivTickCreate.visibility = View.VISIBLE
            binding.ivTickAcc.visibility = View.INVISIBLE
        }
        if(sort_action == 3 ){
            binding.ivTickName.visibility = View.INVISIBLE
            binding.ivTickCreate.visibility = View.INVISIBLE
            binding.ivTickAcc.visibility = View.VISIBLE
        }

        binding.llName.setOnClickListener {
            binding.ivTickName.visibility = View.VISIBLE
            binding.ivTickAcc.visibility = View.INVISIBLE
            binding.ivTickCreate.visibility = View.INVISIBLE
            val intent = Intent(MY_ACTION)
            intent.putExtra("action_sort", SortName)
            activity?.sendBroadcast(intent)
            dismiss()

        }
        binding.llCreate.setOnClickListener {
            binding.ivTickName.visibility = View.INVISIBLE
            binding.ivTickAcc.visibility = View.INVISIBLE
            binding.ivTickCreate.visibility = View.VISIBLE
            val intent = Intent(MY_ACTION)
            intent.putExtra("action_sort", SortCreate)
            activity?.sendBroadcast(intent)
            dismiss()
        }
        binding.llAccess.setOnClickListener {
            binding.ivTickName.visibility = View.INVISIBLE
            binding.ivTickAcc.visibility = View.VISIBLE
            binding.ivTickCreate.visibility = View.INVISIBLE
            val intent = Intent(MY_ACTION)
            intent.putExtra("action_sort", SortAceesed)
            activity?.sendBroadcast(intent)
            dismiss()
        }
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent);
            dialog!!.window?.requestFeature(Window.FEATURE_NO_TITLE);
        }
        return binding.root
    }
}