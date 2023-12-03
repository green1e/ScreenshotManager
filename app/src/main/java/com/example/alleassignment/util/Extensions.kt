package com.example.alleassignment.util

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.BaseRequestOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.alleassignment.R

fun View.show() {
    if (visibility == View.VISIBLE) return
    visibility = View.VISIBLE
}

fun View.gone() {
    if (visibility == View.GONE) return
    visibility = View.GONE
}

fun ImageView.loadImageWithFallBackDrawable(
    uri: Uri?,
    progressBar: ProgressBar?,
    @DrawableRes fallbackDrawableRes: Int
) {
    if (uri == null) return
    val activity = context as? Activity
    if (activity?.isFinishing == true || activity?.isDestroyed == true)
        return

    progressBar?.show()
    Glide.with(context ?: return)
        .load(uri)
        .centerInside()
        .apply(getCacheRequestOptions(fallbackDrawableRes, fallbackDrawableRes))
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                progressBar?.gone()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                progressBar?.gone()
                return false
            }
        }).into(this)
}


private fun getCacheRequestOptions(
    @DrawableRes placeholder: Int = R.color.black,
    @DrawableRes error: Int = R.color.black
): BaseRequestOptions<RequestOptions> {
    return RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(placeholder)
        .error(error)
}

fun Activity.openPermissionSettings(binder: ActivityResultLauncher<Intent>) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.fromParts("package", packageName, null)
    binder.launch(intent)
}