package pe.edu.upt.firechat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    if(BuildConfig.DEBUG){
      Firebase.auth.useEmulator("10.0.2.2",9099)
    }

    if(FirebaseAuth.getInstance().currentUser == null){
      val intent = Intent(this,LoginActivity::class.java)
      startActivity(intent)
      finish()
    }else{
      val intent = Intent(this,MainActivity::class.java)
      startActivity(intent)
      finish()
    }
  }
}