package pe.edu.upt.firechat.ui.dashboard

import android.view.View
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

  private val _imageView = MutableLiveData<ImageView>().apply {

  }

  val imageView: LiveData<ImageView> = _imageView
}