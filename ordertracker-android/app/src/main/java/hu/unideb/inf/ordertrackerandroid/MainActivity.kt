package hu.unideb.inf.ordertrackerandroid

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import hu.unideb.inf.ordertrackerandroid.databinding.ActivityMainBinding
import hu.unideb.inf.ordertrackerandroid.databinding.DrawerHeaderBinding
import hu.unideb.inf.ordertrackerandroid.event.AuthFailedEvent
import hu.unideb.inf.ordertrackerandroid.fragment.AuthenticationFragmentDirections
import hu.unideb.inf.ordertrackerandroid.util.FileUtils
import hu.unideb.inf.ordertrackerandroid.util.WidgetUtils
import hu.unideb.inf.ordertrackerandroid.viewmodel.UserViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var navViewHeaderBinding: DrawerHeaderBinding

    fun navController() = findNavController(R.id.nav_host_fragment)

    val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigationBar()

        setupObservers()

        clearTempFiles()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }


    private fun setupNavigationBar() {
        navController().addOnDestinationChangedListener { controller, destination, arguments ->
            if (isDestinationFullScreen(destination)) {
                binding.toolbar.visibility = View.GONE
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            } else {
                binding.toolbar.visibility = View.VISIBLE
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }
        }

        val appBarConfiguration = AppBarConfiguration(navController().graph, binding.drawerLayout)
        binding.toolbar.setupWithNavController(navController(), appBarConfiguration)

        binding.navView.setupWithNavController(navController())

        binding.navView.fitsSystemWindows = false

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            handleMenuItemSelected(
                menuItem
            )
        }

        navViewHeaderBinding = DrawerHeaderBinding.inflate(layoutInflater)

        binding.navView.addHeaderView(navViewHeaderBinding.root)
    }


    private fun isDestinationFullScreen(destination: NavDestination): Boolean {
        return destination.id == R.id.login_fragment
                || destination.id == R.id.authentication_fragment
                || destination.id == R.id.registration_fragment
    }

    private fun handleMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.drawer_logout -> {
                navController().navigate(NavigationDirections.actionGlobalToAuthFragment())
            }
            else -> {
                navController().popBackStack(R.id.home_fragment, false)
                navController().navigate(menuItem.itemId)
            }
        }
        binding.drawerLayout.close()
        return true
    }

    private fun setupObservers() {
        userViewModel.user.observe(this) { user ->
            user?.let {
                navViewHeaderBinding.tvUser.text = "User: ${it.userName}"
            }
        }
    }

    private fun clearTempFiles() {
        FileUtils.findDirectory(this, FileUtils.Directory.IMAGES).listFiles()?.forEach { file ->
            file.delete()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAuthFailed(authFailedEvent: AuthFailedEvent) {
        WidgetUtils.createMessageDialog(this, "Failed", "Login expired!")
        val action =
            AuthenticationFragmentDirections.actionGlobalToAuthFragment().setRedirectToLogin(true)
        navController().navigate(action)
    }

}