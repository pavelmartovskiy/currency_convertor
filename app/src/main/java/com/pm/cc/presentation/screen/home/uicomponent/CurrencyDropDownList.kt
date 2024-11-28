package com.pm.cc.presentation.screen.home.uicomponent

import android.util.Log
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType.Companion.PrimaryNotEditable
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pm.cc.presentation.screen.home.model.CurrencyItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyDropDownList(
    selectedValue: CurrencyItem,
    options: List<CurrencyItem>,
    onValueChangedEvent: (CurrencyItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {


        TextField(
            readOnly = true,
            value = selectedValue.code,
            onValueChange = {},
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = OutlinedTextFieldDefaults.colors(),
            modifier = Modifier
                .menuAnchor(type = PrimaryNotEditable)
                .fillMaxWidth()
                .indicatorLine(
                    enabled = false,
                    isError = false,
                    interactionSource = interactionSource,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent
                    ),
                    focusedIndicatorLineThickness = 0.dp,
                    unfocusedIndicatorLineThickness = 0.dp
                )
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option: CurrencyItem ->
                DropdownMenuItem(
                    text = { Text(text = option.code) },
                    onClick = {
                        expanded = false
                        Log.w("DsC", "select value: $option")
                        onValueChangedEvent.invoke(option)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun DsC() {
    val data = listOf(
        CurrencyItem(id = 1, code = "EUR"),
        CurrencyItem(id = 1, code = "UDS"),
        CurrencyItem(id = 1, code = "AUS"),
        CurrencyItem(id = 1, code = "AUS2"),
    )
    CurrencyDropDownList(
        selectedValue = data[0],
        options = data,
        onValueChangedEvent = { value -> Log.w("DsC", "select value: $value") },
        modifier = Modifier
    )
}