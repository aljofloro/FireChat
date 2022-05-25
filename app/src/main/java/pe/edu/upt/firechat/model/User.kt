package pe.edu.upt.firechat.model

data class User(val name:String,
                val bio: String,
                val profilePicturePath: String?) {
  constructor():this("","",null)
}