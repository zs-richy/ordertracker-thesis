package hu.unideb.inf.ordertrackerandroid.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import hu.unideb.inf.ordertrackerandroid.NavigationDirections
import hu.unideb.inf.ordertrackerandroid.R
import hu.unideb.inf.ordertrackerandroid.databinding.FragmentRegistrationBinding
import hu.unideb.inf.ordertrackerandroid.dialog.CustomProgressDialogHandler
import hu.unideb.inf.ordertrackerandroid.util.WidgetUtils
import hu.unideb.inf.ordertrackerandroid.viewmodel.RegistrationViewModel

class RegistrationFragment: Fragment() {

    lateinit var binding: FragmentRegistrationBinding

    private val registrationViewModel: RegistrationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.registrationViewModel = registrationViewModel

        setupObservers()

        return binding.root
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenStarted {
            registrationViewModel.registrationStatus.collect { registrationStatus ->
                when(registrationStatus) {
                    RegistrationViewModel.RegistrationStatus.PROGRESS ->
                        CustomProgressDialogHandler.createProgressDialog(childFragmentManager, "Registration in progress")
                    RegistrationViewModel.RegistrationStatus.SUCCESS -> {
                        CustomProgressDialogHandler.dismissDialog()
                        findNavController().navigate(NavigationDirections.actionGlobalToLoginfragmentFragment())
                    }
                    RegistrationViewModel.RegistrationStatus.ERROR -> {
                        CustomProgressDialogHandler.dismissDialog()
                        WidgetUtils.createMessageDialog(requireContext(), "Alert", "Failed")
                    }
                }
            }
        }
    }

}