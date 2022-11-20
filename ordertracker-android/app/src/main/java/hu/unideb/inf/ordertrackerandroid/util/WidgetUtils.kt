package hu.unideb.inf.ordertrackerandroid.util

import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.util.TypedValue
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import hu.unideb.inf.ordertrackerandroid.R


object WidgetUtils {

    fun getColoredDrwable(context: Context, res: Int, color: Int): Drawable? {
        var drawable = ContextCompat.getDrawable(context, res)?.mutate()
        return tint(drawable, ColorStateList.valueOf(color))
    }

    fun tint(input: Drawable?, tint: ColorStateList?): Drawable? {
        if (input == null) {
            return null
        }
        val wrappedDrawable = DrawableCompat.wrap(input)
        DrawableCompat.setTintList(wrappedDrawable, tint)
        DrawableCompat.setTintMode(wrappedDrawable, PorterDuff.Mode.MULTIPLY)
        return wrappedDrawable
    }

    fun createConfirmDialog(context: Context, title: String, message: String,
                            clickListener: DialogInterface.OnClickListener, hasNegativeButton: Boolean = false): AlertDialog {
        var dialogBuilder = MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", clickListener)

        if (hasNegativeButton) {
            dialogBuilder.setNegativeButton("Cancel", clickListener)
        }

        return dialogBuilder.create()
    }

    fun createMessageDialog(context: Context, title: String? = null, message: String,
                            positiveButtonText: String? = null,
                            clickListener: DialogInterface.OnClickListener? = null) {
        AlertDialog.Builder(context)
            .setTitle(title ?: "Alert!")
            .setMessage(message)
            .setPositiveButton(positiveButtonText ?: "OK", clickListener ?: DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
            })
            .create()
            .show()
    }

    fun getColorFromTheme(context: Context, attr: Int): Int {
        val value = TypedValue()
        context.theme.resolveAttribute(R.attr.colorPrimary, value, true)

        return value.data
    }

}