package com.example.postcard.card

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.example.postcard.R
import com.example.postcard.databinding.FragmentCardBinding
import com.google.android.material.textview.MaterialTextView

class CardTestFragment : Fragment() {

    private lateinit var binding: FragmentCardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_card, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataFromListener()
        getDataFromPreferences()
        binding.LayoutCard.transitionToEnd()
        animateText(binding.LayoutCardTitle)
        animateText(binding.LayoutCardName)
        animateShowMainText()
    }

    private fun getDataFromListener() {
        setFragmentResultListener("testCard") { key, bundle ->
            val name = bundle.getString("name")
            val title = bundle.getString("title")
            val text = bundle.getString("text")
            val image = bundle.getInt("image")
            binding.LayoutCardName.text = name
            binding.LayoutCardTitle.text = title
            binding.LayoutCardText.text = text
            binding.LayoutCardImage.setImageResource(image)
        }
    }

    private fun getDataFromPreferences() {
        val preferences = activity?.getPreferences(Context.MODE_PRIVATE)
        binding.LayoutCardName.text = preferences?.getString("title", "")
        binding.LayoutCardTitle.text = preferences?.getString("name", "")
        binding.LayoutCardText.text = preferences?.getString("text", "")
        val imageId = preferences?.getInt("imageId", 0)
        imageId?.let { binding.LayoutCardImage.setImageResource(it) }
    }

    private fun animateShowMainText() {
        val animator = ObjectAnimator.ofFloat(binding.LayoutCardText, View.ALPHA, 0f, 1f)
        animator.duration = 1000
        animator.start()
    }

    private fun animateText(layoutCardText: MaterialTextView) {
        val animator = ObjectAnimator.ofArgb(
            layoutCardText,
            "textColor", Color.BLUE, Color.RED
        )
        animator.repeatCount = Animation.INFINITE
        animator.duration = 1000
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.start()
    }
}