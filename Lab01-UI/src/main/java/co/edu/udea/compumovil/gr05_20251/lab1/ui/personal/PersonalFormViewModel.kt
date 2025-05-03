package co.edu.udea.compumovil.gr05_20251.lab1.ui.personal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

class PersonalFormViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PersonalFormUiState())
    val uiState: StateFlow<PersonalFormUiState> = _uiState.asStateFlow()

    private val _formValido = MutableStateFlow(false)
    val formValido: StateFlow<Boolean> = _formValido.asStateFlow()

    fun actualizarNombres(nombres: String) {
        _uiState.update { it.copy(nombres = nombres) }
        validarFormulario()
    }

    fun actualizarApellidos(apellidos: String) {
        _uiState.update { it.copy(apellidos = apellidos) }
        validarFormulario()
    }

    fun actualizarSexo(sexo: String) {
        _uiState.update { it.copy(sexo = sexo) }
        validarFormulario()
    }

    fun actualizarFechaNacimiento(fechaNacimiento: Long) {
        _uiState.update { it.copy(fechaNacimiento = fechaNacimiento) }
        validarFormulario()
    }

    fun actualizarGradoEscolaridad(gradoEscolaridad: String) {
        _uiState.update { it.copy(gradoEscolaridad = gradoEscolaridad) }
        validarFormulario()
    }

    private fun validarFormulario() {
        viewModelScope.launch {
            val state = _uiState.value

            // Validar campos obligatorios
            val nombresValidos = state.nombres.trim().isNotEmpty()
            val apellidosValidos = state.apellidos.trim().isNotEmpty()
            val fechaNacimientoValida = state.fechaNacimiento != null &&
                    esFechaValida(state.fechaNacimiento)

            _formValido.value = nombresValidos && apellidosValidos && fechaNacimientoValida
        }
    }

    private fun esFechaValida(fechaNacimiento: Long): Boolean {
        val hoy = Calendar.getInstance().timeInMillis
        return fechaNacimiento <= hoy
    }
}