package hu.unideb.inf.ordertrackerandroid.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import hu.unideb.inf.ordertrackerandroid.R
import hu.unideb.inf.ordertrackerandroid.databinding.FragmentHomeBinding
import hu.unideb.inf.ordertrackerandroid.handler.SharedPreferencesHandler
import hu.unideb.inf.ordertrackerandroid.util.FileUtils
import hu.unideb.inf.ordertrackerandroid.viewmodel.HomeViewModel
import hu.unideb.inf.ordertrackerandroid.viewmodel.UserViewModel
import java.lang.Exception

class HomeFragment : Fragment() {

    val userViewModel: UserViewModel by activityViewModels()
    val homeViewModel: HomeViewModel by viewModels()

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.homeViewModel = homeViewModel

        binding.lifecycleOwner = viewLifecycleOwner

        setupObservers()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.updateSessionState()

        if (userViewModel.user.value?.isTokenValid == false) {
            handleUserNavigation()
        }
    }

    private fun setupObservers() {
        homeViewModel.welcomeImage.observe(this) { welcomeImage ->
            welcomeImage?.let {
                try {
                    val imageFile =
                        FileUtils.findPath(requireContext(), FileUtils.Directory.IMAGES, it)
                    Glide.with(requireContext())
                        .load(imageFile)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .placeholder(
                            ContextCompat.getDrawable(requireContext(), R.drawable.ic_burger)
                                ?.also { it.setTint(ContextCompat.getColor(requireContext(), R.color.black)) })
                        .into(binding.ivWelcome)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun handleUserNavigation() {
        if (SharedPreferencesHandler.getUsername(requireContext()) != null) {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAuthenticationFragment().setRedirectToLogin(true))
        } else {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAuthenticationFragment())
        }
    }

}