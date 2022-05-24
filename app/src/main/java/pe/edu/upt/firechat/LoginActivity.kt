package pe.edu.upt.firechat

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse

class LoginActivity : AppCompatActivity() {

  private val RC_SIGN_IN = 120

  private val signInProviders = listOf(AuthUI.IdpConfig.EmailBuilder()
    .setAllowNewAccounts(true)
    .setRequireName(true)
    .build())

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    val account_sign_in = findViewById<Button>(R.id.account_sign_in)

    account_sign_in.setOnClickListener{
      val intent = AuthUI.getInstance().createSignInIntentBuilder()
        .setAvailableProviders(signInProviders)
        .setLogo(R.mipmap.ic_launcher_round)
        .build()
      startActivityForResult(intent,RC_SIGN_IN)
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if(requestCode == RC_SIGN_IN){
      val response = IdpResponse.fromResultIntent(data)
      if(resultCode == Activity.RESULT_OK){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
      }else if(resultCode == Activity.RESULT_CANCELED){
        if(response == null) return
        when(response.error?.errorCode){
          ErrorCodes.NO_NETWORK -> Toast.makeText(this,"Sin conexión de red",Toast.LENGTH_SHORT).show()
          ErrorCodes.UNKNOWN_ERROR -> Toast.makeText(this,"Error desconocido",Toast.LENGTH_SHORT).show()
        }
      }
    }
  }
}