package com.example.postcard.ui.main

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.postcard.R
import com.example.postcard.databinding.FragmentMainBinding
import com.example.postcard.ui.card.model.CardModel
import com.example.postcard.ui.main.adapter.ThemeAdapter


class MainFragment : Fragment() {
    companion object {
        private const val IMAGE_PICK_CODE = 1000;
        private const val PERMISSION_CODE = 1001;
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
        binding.parentFragmentProfileImage.setOnClickListener {
            chooseImageFromGallery()
        }
        binding.parentFragmentButtonTest.setOnClickListener { handleTestButton() }
        binding.parentFragmentButtonLaunch.setOnClickListener { handleLaunchButton() }
        imageSwitcherController()
        setupSwitcher()
        checkArgs()
        return binding.root
    }

    private fun checkArgs() {
        val preferences = activity?.getPreferences(Context.MODE_PRIVATE)
        if (preferences?.getString("title", "") != "") {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToCardFragment())
        }
    }

    private fun chooseImageFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.context?.let {
                    checkSelfPermission(
                        it,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                } ==
                PackageManager.PERMISSION_DENIED) {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                requestPermissions(permissions, PERMISSION_CODE);
            } else {
                pickImageFromGallery();
            }
        } else {
            pickImageFromGallery();
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    pickImageFromGallery()
                } else {
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        with(binding) {
            if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
                parentFragmentProfileImage.setImageURI(data?.data)
                parentFragmentProfileImage.tag = data?.data
            }
        }
    }

    private fun handleLaunchButton() {
        with(binding) {
            if (parentFragmentProfileImage.tag == null) {
                parentFragmentProfileImage.tag =
                    Uri.parse("android.resource://" + (context?.packageName) + "/drawable/face1")
            }
            if (parentFragmentInputName.text.toString() == ""
                || parentFragmentInputTitle.text.toString() == ""
                || parentFragmentInputText.text.toString() == ""
            ) {
                val toast = Toast.makeText(context, "Заполните поля!", Toast.LENGTH_SHORT)
                toast.show()
            } else {
                activity?.let {
                    viewModel.savePreferences(
                        it,
                        parentFragmentInputName.text.toString(),
                        parentFragmentInputTitle.text.toString(),
                        parentFragmentInputText.text.toString(),
                        parentFragmentImageSwitcher.tag as Int,
                    )
                }
                activity?.finish();
            }
        }
    }

    private fun imageSwitcherController() {
        binding.parentFragmentThemePrevious.setOnClickListener {
            setPreviousTheme()
        }

        binding.parentFragmentThemeNext.setOnClickListener {
            setNextTheme()
        }
    }

    private fun handleTestButton() {
        with(binding) {
            if (parentFragmentProfileImage.tag == null) {
                parentFragmentProfileImage.tag =
                    Uri.parse("android.resource://" + (context?.packageName) + "/drawable/face1")
            }
            if (parentFragmentInputName.text.toString() == ""
                || parentFragmentInputTitle.text.toString() == ""
                || parentFragmentInputText.text.toString() == ""
            ) {
                val toast = Toast.makeText(context, "Заполните поля!", Toast.LENGTH_SHORT)
                toast.show()
            } else {
                val model = CardModel(
                    parentFragmentInputName.text.toString(),
                    parentFragmentInputTitle.text.toString(),
                    parentFragmentInputText.text.toString(),
                    parentFragmentImageSwitcher.tag as Int,
                    parentFragmentProfileImage.tag
                )

                val bundle = Bundle()
                bundle.putSerializable("card", model)
                findNavController().navigate(R.id.action_mainFragment_to_cardFragment, bundle)
            }
        }
    }

    private fun setPreviousTheme() {
        try {
            binding.parentFragmentCards.layoutManager?.smoothScrollToPosition(
                binding.parentFragmentCards,
                null,
                layoutManager.findFirstVisibleItemPosition() - 1
            )
        } catch (e: IllegalArgumentException) {
        }

    }

    private fun setNextTheme() {
        binding.parentFragmentCards.layoutManager?.smoothScrollToPosition(
            binding.parentFragmentCards,
            null,
            layoutManager.findLastVisibleItemPosition() + 1
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buildButtonsRV()
    }

    private fun setupSwitcher() {
        val imageSwitcher = binding.parentFragmentImageSwitcher
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

        binding.parentFragmentButtonPrevious.setOnClickListener {
            index = if (index - 1 >= 0) index - 1 else 2
            imageSwitcher.setImageResource(viewModel.icons[index])
            imageSwitcher.tag = viewModel.icons[index]
        }

        binding.parentFragmentButtonNext.setOnClickListener {
            index = if (index + 1 < viewModel.icons.size) index + 1 else 0
            imageSwitcher.setImageResource(viewModel.icons[index])
            imageSwitcher.tag = viewModel.icons[index]
        }
    }

    private fun buildButtonsRV() {
        addDefaultThemes()
        adapter = ThemeAdapter(viewModel.listOfThemes)
        with(binding) {
            parentFragmentCards.adapter = adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val snapHelper: SnapHelper = PagerSnapHelper()
            parentFragmentCards.layoutManager = layoutManager
            snapHelper.attachToRecyclerView(parentFragmentCards)
        }
    }

    private fun addDefaultThemes() {
        with(viewModel) {
            addTheme("zima", "Text", R.drawable.face1)
            addTheme("Leto", "Text2", R.drawable.profile_image)
        }
    }
}