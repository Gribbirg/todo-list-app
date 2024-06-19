package ru.gribbirg.todoapp.ui.edititem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@Composable
fun EditItemScreen(
    viewModel: EditItemViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold {
        Box(modifier = Modifier.padding(it)) {
            if (uiState is EditItemUiState.Loaded) {
                Text(text = (uiState as EditItemUiState.Loaded).item.toString())
            }
        }
    }
}