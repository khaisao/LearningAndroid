package com.prox.powerpointreader.ui.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider.getUriForFile
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.prox.powerpointreader.adapter.PPTExploreAdapter
import com.prox.powerpointreader.callback.OnPPTFileItemClickListener
import com.prox.powerpointreader.databinding.FragmentHomeBinding
import com.prox.powerpointreader.model.PPTFile
import com.prox.powerpointreader.ui.activity.MainActivity
import com.prox.powerpointreader.ui.fragment.dialog.DeleteDialogFragment
import com.prox.powerpointreader.ui.fragment.dialog.RenameDialogFragment
import com.prox.powerpointreader.ui.fragment.dialog.SortDialogFragment
import com.prox.powerpointreader.vm.PPTFileViewModel
import com.prox.powerpointreader.vm.SortOrder
import com.wxiwei.office.constant.MainConstant
import com.wxiwei.office.officereader.AppActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.File


class HomeFragment : Fragment(), OnPPTFileItemClickListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: PPTFileViewModel
    private lateinit var adapter: PPTExploreAdapter
    private val mBroadcast = SortBroadcast()
    private var sort_action = 1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("cxxc","Create")
        binding = FragmentHomeBinding.inflate(inflater)
        viewModel = (activity as MainActivity).viewModel

        adapter = PPTExploreAdapter(this)
        binding.rv.adapter = adapter
        binding.rv.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL, false
        )
        binding.rv.itemAnimator = null
        if (binding.edtSearch.text.isEmpty()) {
            viewModel.pptFile.observe(viewLifecycleOwner) {
                checkNoItem(it)
            }
        }
        binding.ivSort.setOnClickListener {
            showDialog()
        }
        var job: Job? = null
        binding.ivClearEdt.setOnClickListener {
            binding.edtSearch.text.clear()
        }
        binding.edtSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                editable?.let {
                    viewModel.searchQuery.value = editable.toString()
                }
                if(editable.toString() != ""){
                    binding.ivClearEdt.visibility = View.VISIBLE
                }
                else{
                    binding.ivClearEdt.visibility = View.INVISIBLE
                }

            }
        }
        return binding.root
    }


    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter("com.prox.powerpointreader")
        requireActivity().registerReceiver(mBroadcast, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        Log.d("checklife","Stop ne")
        requireActivity().unregisterReceiver(mBroadcast)
    }

    private fun checkNoItem(listPPTFile: List<PPTFile>) {
        adapter.submitList(listPPTFile)
        if (listPPTFile.isEmpty()) {
            binding.tvNoItem.visibility = View.VISIBLE
        } else {
            binding.tvNoItem.visibility = View.INVISIBLE
        }
    }


    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        binding.edtSearch.text.clear()
    }


    inner class SortBroadcast : BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            if (intent?.action == "com.prox.powerpointreader") {
                val sortAction = intent.getIntExtra("action_sort", 0)
                if (sortAction == 1) {
                    viewModel.sortOrder.value = SortOrder.BY_NAME
                    sort_action = 1
                }
                if (sortAction == 2) {
                    viewModel.sortOrder.value = SortOrder.BY_CREATE_DATE
                    sort_action = 2
                }
                if (sortAction == 3) {
                    viewModel.sortOrder.value = SortOrder.BY_ACCESSED_DATE
                    sort_action = 3
                }
            }
        }
    }

    private fun showDialog() {
        val dialog = SortDialogFragment()
        val bundle = Bundle()
        bundle.putInt("sort_action", sort_action)
        dialog.arguments = bundle
        dialog.show(childFragmentManager, "SortDialogFragment")
    }

    override fun onClick(pptFile: PPTFile) {
        if (File(pptFile.absolutePath).exists()) {
            val intent = Intent(activity, AppActivity::class.java)
            val time = System.currentTimeMillis()
            viewModel.updateFile(
                PPTFile(
                    pptFile.fileName,
                    pptFile.like,
                    pptFile.parenAbsolutePath,
                    pptFile.absolutePath,
                    pptFile.createDate,
                    time
                )
            )
            intent.putExtra(MainConstant.INTENT_FILED_FILE_PATH, pptFile.absolutePath)
            startActivity(intent)
        } else {
            Toast.makeText(context, "File does not exist", Toast.LENGTH_SHORT).show()
            viewModel.deleteFileByPath(pptFile.absolutePath)
        }
        binding.edtSearch.clearFocus()
    }

    override fun onLikeClick(pptFile: PPTFile) {
        viewModel.updateFile(
            PPTFile(
                pptFile.fileName,
                1,
                pptFile.parenAbsolutePath,
                pptFile.absolutePath,
                pptFile.createDate,
                pptFile.aceesedDate
            )
        )
        binding.edtSearch.clearFocus()
        Toast.makeText(context, "Added to Bookmark", Toast.LENGTH_SHORT).show()
    }

    override fun onUnLikeClick(pptFile: PPTFile) {
        viewModel.updateFile(
            PPTFile(
                pptFile.fileName,
                0,
                pptFile.parenAbsolutePath,
                pptFile.absolutePath,
                pptFile.createDate,
                pptFile.aceesedDate
            )
        )
        binding.edtSearch.clearFocus()
        Toast.makeText(context, "Remove to Bookmark", Toast.LENGTH_SHORT).show()
    }

    override fun onShareClick(pptFile: PPTFile) {

        val newFile = File(pptFile.absolutePath)
        val contentUri: Uri =
            getUriForFile(requireContext(), "com.prox.powerpointreader.fileprovider", newFile)
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_STREAM, contentUri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = "file/*"
        startActivity(Intent.createChooser(intent, "Share File"))
    }

    override fun onRenameClick(pptFile: PPTFile) {
        val dialog = RenameDialogFragment()
        val bundle = Bundle()
        bundle.putSerializable("file", pptFile)
        dialog.arguments = bundle
        dialog.show(childFragmentManager, "DialogFragmentWithSetter")
    }

    override fun onDeleteClick(pptFile: PPTFile) {
        val dialog = DeleteDialogFragment()
        val bundle = Bundle()
        bundle.putString("absolutePath", pptFile.absolutePath)
        dialog.arguments = bundle
        dialog.show(childFragmentManager, "DialogFragmentWithSetter")
    }

}


