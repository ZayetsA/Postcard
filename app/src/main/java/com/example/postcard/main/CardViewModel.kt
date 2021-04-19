package com.example.postcard.main

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.example.postcard.R
import com.example.postcard.main.model.ExampleModel
import java.util.*

class CardViewModel : ViewModel() {

    var listOfThemes = ArrayList<ExampleModel>()

    val icons = intArrayOf(
        R.drawable.angelicmushry,
        R.drawable.banana,
        R.drawable.apple,
        R.drawable.banana2,
        R.drawable.cherry,
        R.drawable.blackmushry
    )


    fun addTheme(title: String, text: String, drawable: Int) {
        listOfThemes.add(
            ExampleModel(title, text, drawable)
        )
    }

    fun savePreferences(activity: FragmentActivity, name: String, title: String, text: String, imageId:Int) {
        val preferences = activity.getPreferences(Context.MODE_PRIVATE)
        val editor = preferences?.edit()

        editor?.putString("name", name)
        editor?.putString("title", title)
        editor?.putString("text", text)
        editor?.putInt("imageId", imageId)
        editor?.apply()
    }

}