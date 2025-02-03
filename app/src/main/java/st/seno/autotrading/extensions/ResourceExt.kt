package st.seno.autotrading.extensions

import android.annotation.SuppressLint
import androidx.annotation.ArrayRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import st.seno.autotrading.App

@SuppressLint("UseCompatLoadingForDrawables")
fun getDrawable(@DrawableRes drawableRes: Int) = App.getInstance().getDrawable(drawableRes)

fun getString(@StringRes stringRes: Int) = App.getInstance().getString(stringRes)

fun getArrays(@ArrayRes arrayRes: Int): Array<String> = App.getInstance().resources.getStringArray(arrayRes)
