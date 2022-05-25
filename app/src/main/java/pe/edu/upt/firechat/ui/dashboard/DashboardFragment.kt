package pe.edu.upt.firechat.ui.dashboard

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.google.android.material.textfield.TextInputEditText
import pe.edu.upt.firechat.LoginActivity
import pe.edu.upt.firechat.R
import pe.edu.upt.firechat.databinding.FragmentDashboardBinding
import pe.edu.upt.firechat.glide.GlideApp
import pe.edu.upt.firechat.model.FirestoreUtil
import java.io.ByteArrayOutputStream
import pe.edu.upt.firechat.model.StorageUtil

class DashboardFragment : Fragment() {

  private val RC_SELECT_IMAGE = 2
  private lateinit var selectedImageBytes:ByteArray
  private var pictureJustChanged = false

  private var _binding: FragmentDashboardBinding? = null

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  @SuppressLint("UseRequireInsteadOfGet")
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val dashboardViewModel =
      ViewModelProvider(this).get(DashboardViewModel::class.java)

    _binding = FragmentDashboardBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val imageView: ImageView = binding.ivAvatar
    val btn_save: Button = binding.btnSave
    val tv_nombre:TextInputEditText = binding.tvNombre
    val tv_bio:TextInputEditText = binding.tvBio
    imageView.setOnClickListener{
      val intent = Intent().apply {
        type="image/*"
        action = Intent.ACTION_GET_CONTENT
        putExtra(Intent.EXTRA_MIME_TYPES,arrayOf("image/jpeg","image/png"))
      }
      startActivityForResult(Intent.createChooser(intent,
        "Selecciones una Imagen"),RC_SELECT_IMAGE)
    }
    binding.btnSave.setOnClickListener {
      if(::selectedImageBytes.isInitialized) StorageUtil
        .uploadProfilePhoto(selectedImageBytes){ imagePath ->
          FirestoreUtil.updateCurrentUser(tv_nombre.text.toString(),
          tv_bio.text.toString(), imagePath)
      }else{
        FirestoreUtil.updateCurrentUser(tv_nombre.text.toString(),
          tv_bio.text.toString(), null)
      }
    }

    binding.btnSignOut.setOnClickListener {
      AuthUI.getInstance().signOut(this@DashboardFragment.context!!)
      startActivity(Intent(this@DashboardFragment.context!!
        , LoginActivity::class.java))
    }

    dashboardViewModel.imageView.observe(viewLifecycleOwner) {

    }


    return root
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if(requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK
      && data != null && data.data != null) {
      val selectedImagePath = data.data
      val selectedImageBmp = MediaStore.Images.Media
        .getBitmap(activity?.contentResolver, selectedImagePath)
      val outputStream = ByteArrayOutputStream()
      selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
      selectedImageBytes = outputStream.toByteArray()
      GlideApp.with(this)
        .load(selectedImageBytes)
        .into(binding.ivAvatar)
      pictureJustChanged = true
    }
  }

  override fun onStart(){
    super.onStart()
    FirestoreUtil.getCurrentUser { user->
      if(this@DashboardFragment.isVisible){
        binding.tvNombre.setText(user.name)
        binding.tvBio.setText(user.bio)
        if(!pictureJustChanged && user.profilePicturePath != null)
          GlideApp.with(this)
            .load(StorageUtil.pathToReference(user.profilePicturePath))
            .placeholder(R.drawable.ic_baseline_account_circle_24)
            .into(binding.ivAvatar)
      }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}