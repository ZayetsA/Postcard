package com.example.postcard.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.postcard.R
import com.example.postcard.databinding.FragmentMainBinding
import com.example.postcard.main.adapter.ThemeAdapter


class MainFragment : Fragment() {
    companion object {

        private var index = 0
    }

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: CardViewModel
    private lateinit var adapter: ThemeAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(CardViewModel::class.java)

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false
        )
        binding.MainFragmentButtonTest.setOnClickListener { handleTestButton() }
        binding.MainFragmentButtonLaunch.setOnClickListener { handleLaunchButton() }
        imageSwitcherController()
        setupSwitcher()
        return binding.root
    }

    private fun handleLaunchButton() {
        if (binding.MainFragmentInputName.text.toString() == ""
            || binding.MainFragmentInputTitle.text.toString() == ""
            || binding.MainFragmentInputText.toString() == ""
        ) {
            val toast = Toast.makeText(context, "Заполните поля!", Toast.LENGTH_SHORT)
            toast.show()
        } else {
            this.activity?.let {
                viewModel.savePreferences(
                    it,
                    binding.MainFragmentInputName.text.toString(),
                    binding.MainFragmentInputTitle.text.toString(),
                    binding.MainFragmentInputText.text.toString(),
                    binding.MainFragmentImageSwitcher.tag as Int
                )
            }
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToCardFragment())
        }
    }

    private fun imageSwitcherController() {
        binding.MainFragmentThemePrevious.setOnClickListener {
            setPreviousTheme()
        }

        binding.MainFragmentThemeNext.setOnClickListener {
            setNextTheme()
        }
    }

    private fun handleTestButton() {
        if (binding.MainFragmentInputName.text.toString() == ""
            || binding.MainFragmentInputTitle.text.toString() == ""
            || binding.MainFragmentInputText.toString() == ""
        ) {
            val toast = Toast.makeText(context, "Заполните поля!", Toast.LENGTH_SHORT)
            toast.show()
        } else {
            setFragmentResult(
                "testCard",
                bundleOf(
                    "name" to binding.MainFragmentInputName.text.toString(),
                    "title" to binding.MainFragmentInputTitle.text.toString(),
                    "text" to binding.MainFragmentInputText.text.toString(),
                    "image" to binding.MainFragmentImageSwitcher.tag,
                )
            )
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToCardFragment())
        }
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
        imageSwitcher.setImageResource(viewModel.icons[index])
        imageSwitcher.tag = viewModel.icons[index]

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
            imageSwitcher.setImageResource(viewModel.icons[index])
            imageSwitcher.tag = viewModel.icons[index]
        }

        binding.MainFragmentImageNext.setOnClickListener {
            index = if (index + 1 < viewModel.icons.size) index + 1 else 0
            imageSwitcher.setImageResource(viewModel.icons[index])
            imageSwitcher.tag = viewModel.icons[index]
        }
    }

    private fun buildButtonsRV() {
        addDefaultThemes()
        adapter = ThemeAdapter(viewModel.listOfThemes)
        with(binding) {
            MainFragmentCardRecyclerView.adapter = adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val snapHelper: SnapHelper = PagerSnapHelper()
            MainFragmentCardRecyclerView.layoutManager = layoutManager
            snapHelper.attachToRecyclerView(MainFragmentCardRecyclerView)
        }
    }

    private fun addDefaultThemes() {
        viewModel.addTheme("zima", "Text", R.drawable.face1)
        viewModel.addTheme("Leto", "Text2", R.drawable.profile_image)
    }
}