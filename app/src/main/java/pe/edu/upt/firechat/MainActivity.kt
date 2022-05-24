package pe.edu.upt.firechat

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.util.Logger.debug
import com.google.firebase.ktx.Firebase
import pe.edu.upt.firechat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

  private val signInProviders =
    listOf(AuthUI.IdpConfig.EmailBuilder()
      .setAllowNewAccounts(true)
      .setRequireName(true)
      .build()
    )

  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    if(BuildConfig.DEBUG){
      Firebase.auth.useEmulator("10.0.2.2",9099)
    }

    val navView: BottomNavigationView = binding.navView

    val navController = findNavController(R.id.nav_host_fragment_activity_main)
    // Passing each menu ID as a set of Ids because each
    // menu should be considered as top level destinations.
    val appBarConfiguration = AppBarConfiguration(
      setOf(
        R.id.navigation_home, R.id.navigation_dashboard
      )
    )
    setupActionBarWithNavController(navController, appBarConfiguration)
    navView.setupWithNavController(navController)
  }

  override fun onStop() {
    super.onStop()
    AuthUI.getInstance().signOut(this)
    startActivity(Intent(this, LoginActivity::class.java))
    finish()
  }
}