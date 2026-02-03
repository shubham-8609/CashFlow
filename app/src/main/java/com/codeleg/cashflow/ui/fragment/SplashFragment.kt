package com.codeleg.cashflow.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.lifecycleScope
import com.codeleg.cashflow.R
import com.codeleg.cashflow.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        binding.appName.startAnimation(slideIn(0))        // immediately
        binding.tagline.startAnimation(slideIn(300))     // after 300ms
        binding.devName.startAnimation(slideIn(600))     // after 600ms


        viewLifecycleOwner.lifecycleScope.launch {
            delay(2000) // Simulate loading time
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, HomeFragment())
               .commit()
        }
        return binding.root
    }

    fun slideIn(delay: Long): Animation {
        return AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.slide_in_right_to_left
        ).apply {
            startOffset = delay
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
    