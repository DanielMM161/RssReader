package com.dmm.rssreader.ui.fragments

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.core.view.forEachIndexed
import com.dmm.rssreader.databinding.SettingsFragmentBinding


class SettingsFragment : BaseFragment<SettingsFragmentBinding>(
	SettingsFragmentBinding::inflate
) {

	override fun setupUI() {
		super.setupUI()
		setTheme()
	}

	fun setTheme() {
		binding.layoutDay.setOnClickListener {
			selectedView(binding.layoutDay, true)
			selectedView(binding.layoutNight, false)
			selectedView(binding.layoutAuto, false)
			viewModel.setTheme("day")
		}

		binding.layoutNight.setOnClickListener {
			selectedView(binding.layoutNight, true)
			selectedView(binding.layoutDay, false)
			selectedView(binding.layoutAuto, false)
			viewModel.setTheme("night")
		}

		binding.layoutAuto.setOnClickListener {
			selectedView(binding.layoutAuto, true)
			selectedView(binding.layoutDay, false)
			selectedView(binding.layoutNight, false)
			viewModel.setTheme("auto")
		}
	}

	fun selectedView(view: View, selected: Boolean) {
		(view as ViewGroup).forEachIndexed { index, view ->
				view.isSelected = selected
		}
	}


}