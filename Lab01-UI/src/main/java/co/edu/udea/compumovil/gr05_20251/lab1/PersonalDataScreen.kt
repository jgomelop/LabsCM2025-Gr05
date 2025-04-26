package co.edu.udea.compumovil.gr05_20251.lab1

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar

@Composable
fun PersonalDataScreen(modifier : Modifier = Modifier){
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var selectedGender by rememberSaveable { mutableStateOf("") }
    var birthDate by rememberSaveable { mutableStateOf("") }
    val btnDatePickerDefaultValue = stringResource(R.string.btn_birth_date_default_value)
    var showDatePicker by remember { mutableStateOf(false) }
    val calendar = Calendar.getInstance()
//    val educationLevels = listOf("Primaria", "Secundaria", "Técnico", "Tecnólogo", "Universitario", "Posgrado")
    val educationLevels = stringArrayResource(R.array.lista_opciones_escolaridad).toList()

    val genders = stringArrayResource(R.array.lista_opciones_genero).toList()
    var selectedEducation by rememberSaveable { mutableStateOf("") }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val dropdownIcon = Icons.Filled.ArrowDropDown

    val msgRequiredFieldsMissing = stringResource(R.string.msg_required_fields_missing)
    val msgTitulo = stringResource(R.string.personal_data_titulo)
    val msgSexoLabel = stringResource(R.string.sexo_label)
    val msgBirthdateLabel = stringResource(R.string.fecha_nacimiento_label)
    val msgEscolaridadLabel = stringResource(R.string.grado_escolaridad_label)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = msgTitulo,
            style = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.sp
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text(stringResource(id = R.string.nombres_label)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                autoCorrectEnabled = false,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text(stringResource(R.string.apellidos_label))},
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = false,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(msgSexoLabel)
        Row {
            genders.forEach { gender ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    RadioButton(
                        selected = selectedGender == gender,
                        onClick = { selectedGender = gender }
                    )
                    Text(text = gender)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row {
            Text(stringResource(R.string.fecha_nacimiento_label))
            Spacer(modifier = Modifier.width(12.dp))
            Button(
                onClick = {
                    showDatePickerDialog(context)
                    { selectedDate -> birthDate = selectedDate }
                          },
//                shape = RoundedCornerShape(0.dp) // sharp corners
            ) {
                Text(
                    text = if (birthDate.isEmpty())
                        stringResource(R.string.btn_birth_date_default_value)
                    else birthDate
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedEducation,
                onValueChange = {},
                readOnly = true,
                label = { Text(msgEscolaridadLabel)},
                trailingIcon = {
                    Icon(
                        imageVector = dropdownIcon,
                        contentDescription = null,
                        modifier = Modifier.clickable { isDropdownExpanded = true }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isDropdownExpanded = true }
            )

            DropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                educationLevels.forEach { level ->
                    DropdownMenuItem(
                        text = { Text(level) },
                        onClick = {
                            selectedEducation = level
                            isDropdownExpanded = false
//                            Toast.makeText(context, "Seleccionaste: $level", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // TODO: Eliminar duplicados
        Button(onClick = {
            if (firstName.isBlank() || lastName.isBlank() || birthDate.isBlank()) {
                Toast.makeText(context, msgRequiredFieldsMissing, Toast.LENGTH_SHORT).show()
            } else {
                println(msgTitulo)
                println("$firstName $lastName")
                println("$msgSexoLabel: $selectedGender")
                println("$msgBirthdateLabel: $birthDate")
                println("$msgEscolaridadLabel: $selectedEducation")
            }
        }) {
            Button(onClick = {
                if (firstName.isBlank() || lastName.isBlank() || birthDate.isBlank()) {
                    Toast.makeText(context, msgRequiredFieldsMissing, Toast.LENGTH_SHORT).show()
                } else {
                    println(msgTitulo)
                    println("$firstName $lastName")
                    println("$msgSexoLabel $selectedGender")
                    println("$msgBirthdateLabel $birthDate")
                    println("$msgEscolaridadLabel: $selectedEducation")

                    // Navegación a ContactDataActivity
                    val intent = Intent(context, ContactDataActivity::class.java)
                    context.startActivity(intent)
                }
            }) {
                Text(stringResource(R.string.btn_siguiente_label))
            }
        }
    }
}