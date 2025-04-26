package co.edu.udea.compumovil.gr05_20251.lab1

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import co.edu.udea.compumovil.gr05_20251.lab1.ui.theme.Labs20251Gr05Theme
import java.util.Calendar

const val PERSONAL_DATA_TAG = "PersonalActivity"

class PersonalDataActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, ContactDataActivity::class.java)
        Log.d(PERSONAL_DATA_TAG, "onCreate PersonalActivity")
        enableEdgeToEdge()

        setContent {
            Labs20251Gr05Theme {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    PersonalDataForm()
                    Button(onClick = {
                        startActivity(intent)
                    }) {
                        Text(
                            stringResource(id = R.string.btn_siguiente_label)

                        )
                    }
                }

            }
        }
    }
}

@Composable
fun PersonalDataForm() {
    var nombres by rememberSaveable { mutableStateOf("") }
    var apellidos by rememberSaveable { mutableStateOf("") }
    var sexo by rememberSaveable { mutableStateOf("") }
    var fechaNacimiento by rememberSaveable { mutableStateOf("") }
    var escolaridad by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        FormTitle()
        HorizontalDivider(thickness = 2.dp)
        RequiredTextField(
            stringResource(id = R.string.nombres_label),
            nombres, { nombres = it }
        )
        RequiredTextField(
            stringResource(id = R.string.apellidos_label),
            apellidos, { apellidos = it }
        )
        GenderSelector(sexo, { sexo = it })
        BirthDatePicker(fechaNacimiento, { fechaNacimiento = it })
        EducationDropdown(escolaridad) { escolaridad = it }

    }
}

@Composable
fun FormTitle() {
    Text(
        text = stringResource(id = R.string.personal_data_titulo),
        modifier = Modifier.padding(vertical = 16.dp)
    )
}

@Composable
fun RequiredTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        isError = value.isBlank(),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            autoCorrect = false,
            keyboardType = KeyboardType.Text
        )
    )
}

@Composable
fun GenderSelector(selectedGender: String, onGenderSelected: (String) -> Unit) {
    val options = stringArrayResource(id = R.array.lista_opciones_genero).toList()
    Column {
        Text(
            stringResource(id = R.string.sexo_label)
        )
        options.forEach { gender ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = gender == selectedGender,
                    onClick = { onGenderSelected(gender) }
                )
                Text(text = gender)
            }
        }
    }
}

@Composable
fun BirthDatePicker(
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val date = "%02d/%02d/%04d".format(dayOfMonth, month + 1, year)
                onDateSelected(date)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    Column {
        Text(
            text = stringResource(id = R.string.fecha_nacimiento_label)
        )
        OutlinedButton(onClick = { datePickerDialog.show() }) {
            //TODO: trasladar lógica a otro archivo, cuando se migre a ViweModel
            Text(text = if (selectedDate.isEmpty()) "Cambiar" else selectedDate)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EducationDropdown(
    selectedEducation: String,
    onEducationSelected: (String) -> Unit
) {
    val options = stringArrayResource(id = R.array.lista_opciones_escolaridad).toList()
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.grado_escolaridad_label)
            )
            // Campo de texto que se comporta como un desplegable
            TextField(
                value = selectedEducation,
                onValueChange = onEducationSelected ,
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier.menuAnchor() // Muy importante
            )

            // Menú desplegable
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onEducationSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }

}





