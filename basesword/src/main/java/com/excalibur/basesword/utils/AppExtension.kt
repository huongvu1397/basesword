package com.excalibur.basesword.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import java.io.File


private var appInstance: Application? = null

fun Application.initBaseApplication() {
    appInstance = this
}

fun getApplication() = appInstance!!

fun getAppString(@StringRes stringId: Int, context: Context? = appInstance): String {
    return context?.getString(stringId) ?: ""
}

fun getAppDrawable(@DrawableRes drawableId: Int, context: Context? = appInstance): Drawable? {
    if (context == null)
        return null
    return ContextCompat.getDrawable(context, drawableId)
}

fun getAppDimensionPixel(@DimenRes dimenId: Int, context: Context? = appInstance) =
    context?.resources?.getDimensionPixelSize(dimenId) ?: -1

fun getAppDimension(@DimenRes dimenId: Int, context: Context? = appInstance) =
    context?.resources?.getDimension(dimenId) ?: -1f

fun getAppColor(@ColorRes colorRes: Int, context: Context? = appInstance) =
    context?.let { ContextCompat.getColor(it, colorRes) } ?: Color.TRANSPARENT

fun getAppTypeFace(@FontRes fontId: Int, context: Context? = appInstance): Typeface? {
    if (context == null)
        return null
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.resources.getFont(fontId)
    } else
        ResourcesCompat.getFont(context, fontId)
}

fun getAppAnimation(@AnimRes animId: Int, context: Context? = appInstance): Animation {
    return AnimationUtils.loadAnimation(context, animId)
}

fun Activity.openAppSetting(REQ: Int) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivityForResult(intent, REQ)
}

fun Fragment.shareFiles(
    provider: String, typeShare: String = "audio/*",
    isShareWithNewTask: Boolean = false, vararg filePaths: String
) {
    activity?.shareFiles(provider, typeShare, isShareWithNewTask, *filePaths)
}

fun Fragment.shareFiles(
    provider: String, typeShare: String = "audio/*",
    isShareWithNewTask: Boolean = false, filePaths: List<String>
) {
    activity?.shareFiles(provider, typeShare, isShareWithNewTask, filePaths)
}

fun Context.shareFiles(
    provider: String, typeShare: String = "audio/*",
    isShareWithNewTask: Boolean = false, vararg filePaths: String
) {
    val uriArrayList: ArrayList<Uri> = ArrayList()
    filePaths.forEach {
        uriArrayList.add(
            FileProvider.getUriForFile(
                this,
                provider,
                File(it)
            )
        )
    }
    doShareFile(uriArrayList, typeShare, isShareWithNewTask)
}

fun Context.shareFiles(
    provider: String,
    typeShare: String = "audio/*",
    isShareWithNewTask: Boolean = false,
    filePaths: List<String>
) {
    val uriArrayList: ArrayList<Uri> = ArrayList()
    filePaths.forEach {
        uriArrayList.add(
            FileProvider.getUriForFile(
                this,
                provider,
                File(it)
            )
        )
    }
    doShareFile(uriArrayList, typeShare, isShareWithNewTask)
}

private fun Context.doShareFile(
    uriArrayList: ArrayList<Uri>,
    typeShare: String,
    isShareWithNewTask: Boolean
) {
    if (uriArrayList.size > 0) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND_MULTIPLE
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriArrayList)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        intent.type = typeShare
        val intentChooser = Intent.createChooser(intent, "Share files")
        if (isShareWithNewTask) {
            intentChooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intentChooser)
    }
}

fun Activity.openWriteSettingPermission(requestCode: Int = 1000) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        startActivityForResult(intent, requestCode)
    }
}
