package co.edu.udea.compumovil.gr05_20251.lab1.ui.personal

import android.app.DatePickerDialog
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.edu.udea.compumovil.gr05_20251.lab1.R
import co.edu.udea.compumovil.gr05_20251.lab1.ui.utils.isLandscape
import java.text.SimpleDateFormat
import java.util.*

const val PersonalFormScreenLogTag = "PersonalScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalFormScreen(
    viewModel: PersonalFormViewModel,
    onNext: () -> Unit
){

    if (isLandscape()) {
        Log.d(PersonalFormScreenLogTag,"PersonalScreenLandscape")
        PersonalFormScreenLandscape(
            viewModel,
            onNext
        )
    } else {
        Log.d(PersonalFormScreenLogTag,"PersonalScreenPortrait")
        PersonalFormScreenPortrait (
            viewModel,
            onNext
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalFormScreenPortrait(
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
        // TÍTULO
        Text(
            text = stringResource(R.string.titulo_formulario_datos_personales),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            color = MaterialTheme.colorScheme.primary
        )

        // Campo Nombres
        OutlinedTextField(
            value = formState.nombres,
            onValueChange = { viewModel.actualizarNombres(it) },
            label = { Text(stringResource(R.string.nombres_label))},
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
            label = { Text(stringResource(R.string.apellidos_label)) },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next,
                autoCorrect = false
            ),
            modifier = Modifier.fillMaxWidth(),
            isError = formState.apellidos.isEmpty()
        )

        Text(stringResource(R.string.sexo_label))
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
            Text(stringResource(R.string.btn_siguiente_label))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalFormScreenLandscape(
    viewModel: PersonalFormViewModel = viewModel(),
    onNext: () -> Unit
) {

    val formState by viewModel.uiState.collectAsState()
    val esFormularioValido by viewModel.formValido.collectAsState()
    val context = LocalContext.current

    val opcionesSexo = stringArrayResource(id = R.array.lista_opciones_genero)
    val opcionesEscolaridad = stringArrayResource(id = R.array.lista_opciones_escolaridad)

    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        // Columna izquierda: Nombres y apellidos
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.titulo_formulario_datos_personales),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.primary
            )
            OutlinedTextField(
                value = formState.nombres,
                onValueChange = { viewModel.actualizarNombres(it) },
                label = { Text(stringResource(R.string.nombres_label)) },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next,
                    autoCorrect = false
                ),
                modifier = Modifier.fillMaxWidth(),
                isError = formState.nombres.isEmpty()
            )

            OutlinedTextField(
                value = formState.apellidos,
                onValueChange = { viewModel.actualizarApellidos(it) },
                label = { Text(stringResource(R.string.apellidos_label)) },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next,
                    autoCorrect = false
                ),
                modifier = Modifier.fillMaxWidth(),
                isError = formState.apellidos.isEmpty()
            )

            // Campo Fecha de nacimiento
            FormDatePicker(
                date = formState.fechaNacimiento,
                onDateSelected = { viewModel.actualizarFechaNacimiento(it) },
                context = context,
                dateFormatter = dateFormatter,
                isError = formState.fechaNacimiento == null
            )
        }

        // Columna derecha: Sexo, Escolaridad, Botón
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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

            EscolaridadDropdown(
                selectedOption = formState.gradoEscolaridad,
                options = opcionesEscolaridad,
                onOptionSelected = { viewModel.actualizarGradoEscolaridad(it) }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onNext() },
                modifier = Modifier.fillMaxWidth(),
                enabled = esFormularioValido
            ) {
                Text(stringResource(R.string.btn_siguiente_label))
            }
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
    val datePickerLabel = stringResource(R.string.fecha_nacimiento_texto_datepicker)
    val displayText = if (date != null) dateFormatter.format(Date(date)) else datePickerLabel

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
        label = { Text(stringResource(R.string.fecha_nacimiento_label)) },
        readOnly = true,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { datePickerDialog.show() },
        trailingIcon = {
            IconButton(onClick = { datePickerDialog.show() }) {
                Icon(Icons.Default.DateRange, contentDescription = datePickerLabel)
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
            label = { Text(stringResource(R.string.grado_escolaridad_label)) },
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