package co.edu.udea.compumovil.gr05_20251.lab1.ui.personal

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.edu.udea.compumovil.gr05_20251.lab1.R
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalFormScreen(
    viewModel: PersonalFormViewModel = viewModel(),
    onNext: () -> Unit
) {
    val formState by viewModel.uiState.collectAsState()
    val esFormularioValido by viewModel.formValido.collectAsState()
    val context = LocalContext.current

    val opcionesSexo = stringArrayResource(id = R.array.lista_opciones_genero)
    val opcionesEscolaridad = stringArrayResource(id = R.array.lista_opciones_escolaridad)

    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Campo Nombres
        OutlinedTextField(
            value = formState.nombres,
            onValueChange = { viewModel.actualizarNombres(it) },
            label = { Text("Nombres *") },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next,
                autoCorrect = false
            ),
            modifier = Modifier.fillMaxWidth(),
            isError = formState.nombres.isEmpty()
        )

        // Campo Apellidos
        OutlinedTextField(
            value = formState.apellidos,
            onValueChange = { viewModel.actualizarApellidos(it) },
            label = { Text("Apellidos *") },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next,
                autoCorrect = false
            ),
            modifier = Modifier.fillMaxWidth(),
            isError = formState.apellidos.isEmpty()
        )

        // Campo Sexo (Radio button)
        Text("Sexo")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            opcionesSexo.forEach { opcion ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = formState.sexo == opcion,
                        onClick = { viewModel.actualizarSexo(opcion) }
                    )
                    Text(text = opcion)
                }
            }
        }

        // Campo Fecha de nacimiento
        FormDatePicker(
            date = formState.fechaNacimiento,
            onDateSelected = { viewModel.actualizarFechaNacimiento(it) },
            context = context,
            dateFormatter = dateFormatter,
            isError = formState.fechaNacimiento == null
        )

        // Campo Grado de escolaridad
        EscolaridadDropdown(
            selectedOption = formState.gradoEscolaridad,
            options = opcionesEscolaridad,
            onOptionSelected = { viewModel.actualizarGradoEscolaridad(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de envío
        Button(
            onClick = { onNext() },
            modifier = Modifier.fillMaxWidth(),
            enabled = esFormularioValido
        ) {
            Text("Enviar formulario")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDatePicker(
    date: Long?,
    onDateSelected: (Long) -> Unit,
    context: Context,
    dateFormatter: SimpleDateFormat,
    isError: Boolean
) {
    val displayText = if (date != null) dateFormatter.format(Date(date)) else "Seleccione fecha *"

    // Configuración para mostrar el DatePicker de Android
    val datePickerDialog = remember {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                onDateSelected(calendar.timeInMillis)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.maxDate = System.currentTimeMillis() // No fechas futuras
        }
    }

    OutlinedTextField(
        value = if (date != null) dateFormatter.format(Date(date)) else "",
        onValueChange = { },
        label = { Text("Fecha de nacimiento *") },
        readOnly = true,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { datePickerDialog.show() },
        trailingIcon = {
            IconButton(onClick = { datePickerDialog.show() }) {
                Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
            }
        },
        isError = isError
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EscolaridadDropdown(
    selectedOption: String,
    options: Array<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = { },
            readOnly = true,
            label = { Text("Grado de escolaridad") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}