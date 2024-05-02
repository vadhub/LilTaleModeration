package com.abg.liltalemoderation.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.abg.liltalemoderation.R
import com.abg.liltalemoderation.data.remote.RemoteInstance
import com.abg.liltalemoderation.data.remote.exception.UnauthorizedException
import com.abg.liltalemoderation.data.remote.exception.UserAlreadyExistException
import com.abg.liltalemoderation.data.remote.exception.UserNotFoundException
import com.abg.liltalemoderation.databinding.FragmentLoginBinding
import com.abg.liltalemoderation.ui.BaseFragment

class LoginFragment : BaseFragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val buttonLogin: Button = binding.loginButton
        val username = binding.usernameLoginEditText
        val password = binding.passwordLoginEditText

        reportViewModel.error.observe(viewLifecycleOwner) { e ->
            if (e is UserAlreadyExistException) {
                Toast.makeText(
                    thisContext,
                    getString(R.string.user_with_this_nik_already_exist), Toast.LENGTH_SHORT
                ).show()
            }
            if (e is UserNotFoundException) {
                Toast.makeText(
                    thisContext,
                    getString(R.string.invalid_password_or_username), Toast.LENGTH_SHORT
                ).show()
            }

            if (e is UnauthorizedException) {
                Toast.makeText(
                    thisContext,
                    getString(R.string.invalid_password_or_username), Toast.LENGTH_SHORT
                ).show()
            }
        }

        reportViewModel.success.observe(viewLifecycleOwner) {
            RemoteInstance.setPicasso(thisContext)
            //findNavController().navigate(R.id.action_registrationFragment_to_accountFragment)
        }

        buttonLogin.setOnClickListener {

            if (username.text.isNullOrBlank()) {
                username.error = getString(R.string.field_empty)
                Toast.makeText(thisContext, getString(R.string.enter_username), Toast.LENGTH_SHORT).show()
            } else if (password.text.isNullOrBlank()) {
                password.error = getString(R.string.field_empty)
                Toast.makeText(thisContext, getString(R.string.enter_password), Toast.LENGTH_SHORT).show()
            } else {
                reportViewModel.auth(username.text.toString().trim(), password.text.toString().trim())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}