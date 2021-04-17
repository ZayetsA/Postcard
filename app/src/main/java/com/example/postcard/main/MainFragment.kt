package com.example.postcard.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.postcard.R
import com.example.postcard.databinding.FragmentMainBinding
import java.util.*


class MainFragment : Fragment() {
    companion object {
        private val icons = intArrayOf(
            R.drawable.angelicmushry,
            R.drawable.banana,
            R.drawable.apple,
            R.drawable.banana2,
            R.drawable.cherry
        )
        private var index = 0
    }

    private lateinit var binding: FragmentMainBinding
    private var listOfThemes = ArrayList<ExampleModel>()
    private lateinit var adapter: ThemeAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false
        )
        binding.MainFragmentButtonTest.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToCardFragment())
        }
        binding.MainFragmentThemePrevious.setOnClickListener {
            setPreviousTheme()
        }

        binding.MainFragmentThemeNext.setOnClickListener {
            setNextTheme()
        }
        setupSwitcher()
        return binding.root
    }

    private fun setPreviousTheme() {
        try {
            binding.MainFragmentCardRecyclerView.layoutManager?.smoothScrollToPosition(
                binding.MainFragmentCardRecyclerView,
                null,
                layoutManager.findFirstVisibleItemPosition() - 1
            )
        } catch (e: IllegalArgumentException) {
        }

    }

    private fun setNextTheme() {
        binding.MainFragmentCardRecyclerView.layoutManager?.smoothScrollToPosition(
            binding.MainFragmentCardRecyclerView,
            null,
            layoutManager.findLastVisibleItemPosition() + 1
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buildButtonsRV()
    }

    private fun setupSwitcher() {
        val imageSwitcher = binding.MainFragmentImageSwitcher
        imageSwitcher.setFactory {
            val imgView = ImageView(requireContext())
            imgView.scaleType = ImageView.ScaleType.FIT_CENTER
            imgView.setPadding(8, 8, 8, 8)
            imgView
        }
        imageSwitcher.setImageResource(icons[index])

        val imgIn = AnimationUtils.loadAnimation(
            requireContext(), android.R.anim.slide_in_left
        )
        imageSwitcher.inAnimation = imgIn

        val imgOut = AnimationUtils.loadAnimation(
            requireContext(), android.R.anim.slide_out_right
        )
        imageSwitcher.outAnimation = imgOut

        binding.mainFragmentImagePrevious.setOnClickListener {
            index = if (index - 1 >= 0) index - 1 else 2
            imageSwitcher.setImageResource(icons[index])
        }

        binding.MainFragmentImageNext.setOnClickListener {
            index = if (index + 1 < icons.size) index + 1 else 0
            imageSwitcher.setImageResource(icons[index])
        }
    }

    private fun buildButtonsRV() {
        addTheme("zima", "Text", R.drawable.face1)
        addTheme("Leto", "Text2", R.drawable.profile_image)
        addTheme("zima", "Text", R.drawable.face1)
        addTheme("Leto", "Text2", R.drawable.profile_image)
        addTheme("zima", "Text", R.drawable.face1)
        addTheme("Leto", "Text2", R.drawable.profile_image)
        adapter = ThemeAdapter(listOfThemes)
        with(binding) {
            MainFragmentCardRecyclerView.adapter = adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val snapHelper: SnapHelper = PagerSnapHelper()
            MainFragmentCardRecyclerView.layoutManager = layoutManager
            snapHelper.attachToRecyclerView(MainFragmentCardRecyclerView)
        }
    }

    private fun addTheme(title: String, text: String, drawable: Int) {
        listOfThemes.add(
            ExampleModel(title, text, drawable)
        )
    }
}