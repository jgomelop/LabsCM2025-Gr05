package co.edu.udea.compumovil.gr05_20251.lab1.ui.contact

data class ContactUiState(
    val telefono: String = "",
    val direccion: String = "",
    val email: String = "",
    val pais: String = "",
    val ciudad: String = "",
    val errores: Map<String, String> = emptyMap()
)