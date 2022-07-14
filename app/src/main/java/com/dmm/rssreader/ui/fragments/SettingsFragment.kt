package com.dmm.rssreader.ui.fragments

import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.dmm.rssreader.databinding.SettingsFragmentBinding


class SettingsFragment : BaseFragment<SettingsFragmentBinding>(
	SettingsFragmentBinding::inflate
) {

	override fun setupUI() {
		super.setupUI()

		binding.layoutDay.setOnClickListener {
			binding.day.isSelected = !binding.day.isSelected
			binding.dayText.isSelected = !binding.dayText.isSelected
		}
	}
}