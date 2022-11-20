package hu.unideb.inf.ordertrackerandroid.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import hu.unideb.inf.ordertrackerandroid.databinding.DialogProgressBinding

class CustomProgressDialogHandler {

    class CustomProgressDialog(): DialogFragment() {

        private lateinit var binding: DialogProgressBinding

        var text: String? = null

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            binding = DialogProgressBinding.inflate(layoutInflater)

            binding.tvStatus.text = text ?: "Loading"

            return AlertDialog.Builder(requireContext())
                .setView(binding.root)
                .setCancelable(false)
                .create()
        }

    }

    companion object {

        private var dialog: CustomProgressDialog? = null

        fun createProgressDialog(fragmentManager: FragmentManager, text: String) {
            dialog?.dismiss()

            dialog = CustomProgressDialog().also {
                it.text = text
                it.isCancelable = false
                it.show(fragmentManager, "CustomProgressDialog") }
        }

        fun dismissDialog() {
            dialog?.dismiss()
        }

    }

}