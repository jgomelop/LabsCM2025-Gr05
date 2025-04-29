package co.edu.udea.compumovil.gr05_20251.lab1.ui.contact

data class ContactUiState(
    val telefono: String = "",
    val direccion: String = "",
    val email: String = "",
    val paisSeleccionado: String = "",
    val ciudadSeleccionada: String = "",
    val errorTelefono: String? = null,
    val errorEmail: String? = null,
    val errorPais: String? = null,
    val esFormularioValido: Boolean = false
)