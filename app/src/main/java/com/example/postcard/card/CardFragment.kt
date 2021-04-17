package com.example.postcard.card

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.postcard.R
import com.example.postcard.databinding.FragmentCardBinding
import com.google.android.material.textview.MaterialTextView

class CardFragment : Fragment() {

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
        binding.LayoutCard.transitionToEnd()
        animateText(binding.LayoutCardTitle)
        animateText(binding.LayoutCardName)
        animateShowMainText()
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