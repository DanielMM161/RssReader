package com.dmm.rssreader.presentation.fragments.auth

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dmm.rssreader.R
import com.dmm.rssreader.databinding.ForgetPasswordFragmentBinding
import com.dmm.rssreader.presentation.viewModel.AuthViewModel
import com.dmm.rssreader.utils.Resource
import com.dmm.rssreader.utils.Utils

class ForgetPasswordFragment : Fragment() {

	private lateinit var binding: ForgetPasswordFragmentBinding
	private lateinit var authViewModel: AuthViewModel

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = ForgetPasswordFragmentBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		authViewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
		resetPassword()
	}

	private fun resetPassword() {
		binding.btnResetPassword.setOnClickListener {
			val email = binding.emailLayout.editText?.text.toString()
			val validateEmail = authViewModel.validateEmail(email)
			if(validateEmail.successful) {
				authViewModel.resetPassword(email).observe(viewLifecycleOwner) {
					when(it) {
						is Resource.Success -> {
							handleAlterDialog(
								title = getString(R.string.email_was_sent),
								message = getString(R.string.email_sent_succesfully)
							) {
								it.cancel()
								findNavController().navigate(R.id.action_forgetPasswordFragment_to_loginFragment)
							}
						}
						is Resource.Error -> {
							handleAlterDialog(
								title = getString(R.string.error_sending_reset_password),
								message = it.message.toString()
							) {
								it.cancel()
							}
						}
					}
				}
			} else {
				binding.emailLayout.error = getString(validateEmail.resId!!)
			}
		}
	}

	private fun handleAlterDialog(
		message: String,
		title: String = "",
		callback: (DialogInterface) -> Unit
	) {
		context?.let {
			var alert = AlertDialog.Builder(it)
			Utils.alertDialog(
				title = title,
				message = message,
				alertDialog = alert,
				textPositiveButton = getString(R.string.accept)
			) {
				callback(it)
			}
		}
	}


}