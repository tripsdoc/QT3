package com.hsc.qda.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {
    companion object {
        const val AVAILABLE_TAG = "availableTag"
        const val USER_PREF = "quupaPref"
    }

    private val userPref: SharedPreferences = context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)

    @SuppressLint("CommitPrefEdits")
    fun setTag(tag: String) {
        val editor: SharedPreferences.Editor = userPref.edit()
        editor.putString(AVAILABLE_TAG, tag)
        editor.apply()
    }

    fun getTag(
        AVAILABLE_TAG: String,
        tag: String = ""
    ): String? {
        return userPref.getString(AVAILABLE_TAG, tag)
    }
}