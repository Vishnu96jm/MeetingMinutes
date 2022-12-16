package com.xome.meetingminutes.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.xome.meetingminutes.R
import com.xome.meetingminutes.databinding.DialogSortingBinding
import com.xome.meetingminutes.utils.*
import com.xome.meetingminutes.view.activities.MainActivity.Companion.KEY_SORT_OPTION

class SortOptionDialog(
    private val onSortOptionsSelected: (SortOption) -> Unit
) : DialogFragment() {

    lateinit var binding : DialogSortingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogSortingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        binding.confirmButton.setOnClickListener {
            onSortOptionSelected()
        }

        val currentSort = getSortOption()

        binding.sortOptions.check(
            when (currentSort) {
                ByList -> R.id.listView
                ByGrid -> R.id.gridView
            }
        )
    }

    private fun getSortOption(): SortOption {
        val localPreferences = activity?.getPreferences(Context.MODE_PRIVATE)

        return getSortOptionFromName(
            localPreferences?.getString(KEY_SORT_OPTION, "") ?: ""
        )
    }

    private fun onSortOptionSelected() {
        val selectedOption = binding.sortOptions.checkedRadioButtonId

        onSortOptionsSelected(
            when (selectedOption) {
                R.id.listView -> ByList
                R.id.gridView -> ByGrid
                else -> ByList
            }
        )

        dismissAllowingStateLoss()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }
}