package com.abg.liltalemoderation.ui.login

import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.abg.liltalemoderation.R
import com.abg.liltalemoderation.data.remote.HandleResponse
import com.abg.liltalemoderation.data.remote.RemoteInstance
import com.abg.liltalemoderation.data.remote.exception.UnauthorizedException
import com.abg.liltalemoderation.data.remote.exception.UserAlreadyExistException
import com.abg.liltalemoderation.data.remote.exception.UserNotFoundException
import com.abg.liltalemoderation.model.pojo.User
import com.abg.liltalemoderation.ui.BaseFragment

open class AuthBaseFragment : BaseFragment(), HandleResponse<User> {

    protected var qwrt = ""

    override fun error(e: Exception) {

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

    override fun success(t: User) {

        configuration.saveLogin(t.username)
        configuration.savePass(qwrt)
        configuration.saveFirstStart(true)

        RemoteInstance.setUser(User(t.username, qwrt))
        RemoteInstance.setPicasso(thisContext)

        //findNavController().navigate(R.id.action_registrationFragment_to_accountFragment)

    }

}