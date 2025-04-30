package co.edu.udea.compumovil.gr05_20251.lab1.ui.personal

data class PersonalFormUiState(
    val nombres: String = "",
    val apellidos: String = "",
    val sexo: String = "",
    val fechaNacimiento: Long? = null,
    val gradoEscolaridad: String = ""
)