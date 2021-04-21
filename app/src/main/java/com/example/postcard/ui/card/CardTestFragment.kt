package com.example.postcard.ui.card

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.postcard.R
import com.example.postcard.databinding.FragmentCardBinding
import com.example.postcard.ui.card.model.CardModel
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
        getDataFromPreferences()
        getDataFromListener()
        binding.cardContainer.transitionToEnd()
        animateText(binding.cardContainerTitle)
        animateText(binding.cardContainerName)
        animateShowMainText()
    }

    private fun getDataFromListener() {
        with(binding) {
            val bundle = arguments
            if (bundle !== null) {
                if (!bundle.isEmpty) {
                    val model = bundle.getSerializable("card") as CardModel
                    cardContainerName.text = model.name
                    cardContainerText.text = model.text
                    cardContainerTitle.text = model.title
                    cardContainerImage.setImageResource(model.imageId)
                    cardContainerAvatar.setImageURI(model.profileImageId as Uri)
                }
            }
        }
    }

    private fun getDataFromPreferences() {
        with(binding) {
            val preferences = activity?.getPreferences(Context.MODE_PRIVATE)
            cardContainerName.text = preferences?.getString("name", "")
            cardContainerTitle.text = preferences?.getString("title", "")
            cardContainerText.text = preferences?.getString("text", "")
            val imageId = preferences?.getInt("imageId", 0)
            imageId?.let {
                cardContainerImage.setImageResource(it)
            }
        }
    }

    private fun animateShowMainText() {
        val animator = ObjectAnimator.ofFloat(binding.cardContainerText, View.ALPHA, 0f, 1f)
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