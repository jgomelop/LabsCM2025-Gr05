package co.edu.udea.compumovil.gr05_20251.lab1.ui.contact

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.util.Patterns

class ContactFormViewModel : ViewModel() {

    var uiState by mutableStateOf(ContactUiState())
        private set

    fun onTelefonoChanged(value: String) {
        uiState = uiState.copy(telefono = value)
    }

    fun onDireccionChanged(value: String) {
        uiState = uiState.copy(direccion = value)
    }

    fun onEmailChanged(value: String) {
        uiState = uiState.copy(email = value)
    }

    fun onPaisChanged(value: String) {
        uiState = uiState.copy(pais = value)
    }

    fun onCiudadChanged(value: String) {
        uiState = uiState.copy(ciudad = value)
    }

    fun validarFormulario(): Boolean {
        val errores = mutableMapOf<String, String>()

        if (uiState.telefono.isBlank()) errores["telefono"] = "El teléfono es obligatorio"
        if (uiState.email.isBlank()) errores["email"] = "El email es obligatorio"
        else if (!Patterns.EMAIL_ADDRESS.matcher(uiState.email).matches()) errores["email"] = "Email inválido"
        if (uiState.pais.isBlank()) errores["pais"] = "El país es obligatorio"

        uiState = uiState.copy(errores = errores)

        return errores.isEmpty()
    }
}
