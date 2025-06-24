package co.edu.udea.compumovil.gr05_20251.lab1.Logger

import android.util.Log
import co.edu.udea.compumovil.gr05_20251.lab1.ui.contact.ContactUiState
import co.edu.udea.compumovil.gr05_20251.lab1.ui.personal.PersonalFormUiState
import java.text.SimpleDateFormat
import java.util.*

const val TAG = "FormsLogger"

fun imprimirInformacionForms(
    personalData: PersonalFormUiState,
    contactData: ContactUiState
){
    val msg = """
        Información personal:
        - Nombres: ${personalData.nombres}
        - Apellidos: ${personalData.apellidos}
        - Sexo: ${personalData.sexo}
        - Fecha de nacimiento: ${longToFechaTextoCompat(personalData.fechaNacimiento)}
        - Grado de Escolaridad: ${personalData.gradoEscolaridad}

        Información de Contacto
        - Teléfono: ${contactData.telefono}
        - Dirección: ${contactData.direccion}
        - Emails: ${contactData.email}
        - País: ${contactData.pais}
        - Ciudad: ${contactData.ciudad}
    """.trimIndent()

    Log.d(TAG, msg)
}

fun longToFechaTextoCompat(timestamp: Long?): String {
    if (timestamp == null || timestamp == 0L) return ""

    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(timestamp))
}
