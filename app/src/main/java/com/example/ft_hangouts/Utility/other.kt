package com.example.ft_hangouts.Utility

import android.content.res.Resources

fun getDp(dp: Int): Int
{
    return (dp * Resources.getSystem().displayMetrics.density).toInt()
}