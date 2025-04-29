package co.edu.udea.compumovil.gr05_20251.lab1.ui.contact

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ContactFormViewModel : ViewModel() {

    // Estado del formulario usando ContactUiState
    var uiState by mutableStateOf(ContactUiState())
        private set

    // Estados para mostrar menús desplegables
    var expandidoPaises by mutableStateOf(false)
        private set

    var expandidoCiudades by mutableStateOf(false)
        private set

    // Función para actualizar el teléfono
    fun actualizarTelefono(nuevoTelefono: String) {
        uiState = uiState.copy(telefono = nuevoTelefono)
        validarTelefono()
        validarFormulario()
    }

    // Función para actualizar la dirección
    fun actualizarDireccion(nuevaDireccion: String) {
        uiState = uiState.copy(direccion = nuevaDireccion)
    }

    // Función para actualizar el email
    fun actualizarEmail(nuevoEmail: String) {
        uiState = uiState.copy(email = nuevoEmail)
        validarEmail()
        validarFormulario()
    }

    // Función para actualizar el país seleccionado
    fun actualizarPais(nuevoPais: String) {
        uiState = uiState.copy(
            paisSeleccionado = nuevoPais,
            // Si cambia el país, reiniciamos la ciudad
            ciudadSeleccionada = ""
        )
        validarPais()
        validarFormulario()
        // Cerramos el menú desplegable
        expandidoPaises = false
    }

    // Función para actualizar la ciudad seleccionada
    fun actualizarCiudad(nuevaCiudad: String) {
        uiState = uiState.copy(ciudadSeleccionada = nuevaCiudad)
        // Cerramos el menú desplegable
        expandidoCiudades = false
    }

    // Funciones para controlar los menús desplegables
    fun alternarMenuPaises() {
        expandidoPaises = !expandidoPaises
        if (expandidoPaises) {
            expandidoCiudades = false
        }
    }

    fun alternarMenuCiudades() {
        expandidoCiudades = !expandidoCiudades
        if (expandidoCiudades) {
            expandidoPaises = false
        }
    }

    // Validaciones
    private fun validarTelefono() {
        val errorTelefono = when {
            uiState.telefono.isEmpty() -> "El teléfono es obligatorio"
            uiState.telefono.any { !it.isDigit() } -> "El teléfono debe contener solo números"
            !esFormatoTelefonoValido(uiState.telefono) -> "Formato de teléfono inválido"
            else -> null
        }
        uiState = uiState.copy(errorTelefono = errorTelefono)
    }

    private fun esFormatoTelefonoValido(telefono: String): Boolean {
        // Validación específica del formato de teléfono
        // Por ejemplo, que tenga al menos 7 dígitos y menos de 10
        return telefono.length >= 7 && telefono.length <= 10
    }

    private fun validarEmail() {
        val errorEmail = when {
            uiState.email.isEmpty() -> "El email es obligatorio"
            !esEmailValido(uiState.email) -> "Email con formato inválido"
            else -> null
        }
        uiState = uiState.copy(errorEmail = errorEmail)
    }

    private fun esEmailValido(email: String): Boolean {
        val patronEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(patronEmail.toRegex())
    }

    private fun validarPais() {
        val errorPais = when {
            uiState.paisSeleccionado.isEmpty() -> "El país es obligatorio"
            else -> null
        }
        uiState = uiState.copy(errorPais = errorPais)
    }

    // Validar todo el formulario
    private fun validarFormulario() {
        val esFormularioValido = uiState.errorTelefono == null &&
                uiState.telefono.isNotEmpty() &&
                uiState.errorEmail == null &&
                uiState.email.isNotEmpty() &&
                uiState.errorPais == null &&
                uiState.paisSeleccionado.isNotEmpty()

        uiState = uiState.copy(esFormularioValido = esFormularioValido)
    }

    // Función para enviar el formulario
    fun enviarFormulario(): Boolean {
        // Validamos una última vez antes de enviar
        validarTelefono()
        validarEmail()
        validarPais()
        validarFormulario()

        return if (uiState.esFormularioValido) {
            // Aquí implementarías la lógica para enviar los datos
            // Por ejemplo, llamar a un repositorio o servicio
            true
        } else {
            false
        }
    }
}