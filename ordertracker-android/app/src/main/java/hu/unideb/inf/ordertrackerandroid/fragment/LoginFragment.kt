package hu.unideb.inf.ordertrackerandroid.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import hu.unideb.inf.ordertrackerandroid.R
import hu.unideb.inf.ordertrackerandroid.databinding.FragmentLoginBinding
import hu.unideb.inf.ordertrackerandroid.dialog.CustomProgressDialogHandler
import hu.unideb.inf.ordertrackerandroid.model.api.User
import hu.unideb.inf.ordertrackerandroid.util.WidgetUtils
import hu.unideb.inf.ordertrackerandroid.viewmodel.LoginViewModel
import hu.unideb.inf.ordertrackerandroid.viewmodel.UserViewModel

class LoginFragment: Fragment() {

    lateinit var binding: FragmentLoginBinding

    val userViewModel: UserViewModel by activityViewModels()

    val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        binding.loginViewModel = loginViewModel

        userViewModel.clearUserData()

        userViewModel.user.observe(this, { user ->
            onUserChanged(user)
        })

        lifecycleScope.launchWhenStarted {
            loginViewModel.loginStatus.collect { status ->
                handleLoginStatus(status)
            }
        }

        return binding.root
    }

    private fun onUserChanged(user: User?) {
        if (user?.isTokenValid == true) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
        }
    }

    private fun handleLoginStatus(status: LoginViewModel.LoginStatus) {
        when (status) {
            LoginViewModel.LoginStatus.PROGRESS -> CustomProgressDialogHandler.createProgressDialog(childFragmentManager, "Signing in")
            LoginViewModel.LoginStatus.SUCCESS -> {
                CustomProgressDialogHandler.dismissDialog()
                userViewModel.updateSessionState()
            }
            LoginViewModel.LoginStatus.ERROR -> {
                CustomProgressDialogHandler.dismissDialog()
                WidgetUtils.createMessageDialog(requireContext(), "Error", "Login failed!")
            }
            else -> CustomProgressDialogHandler.dismissDialog()
        }
    }

}