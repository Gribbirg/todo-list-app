package ru.gribbirg.todoapp.ui.edititem

import androidx.compose.animation.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import ru.gribbirg.todoapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItemScreen(
    viewModel: EditItemViewModel,
    onClose: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val systemUiController = rememberSystemUiController()

    val appBarColor = MaterialTheme.colorScheme.background
    val scrolledAppBarColor = if (isSystemInDarkTheme())
        MaterialTheme.colorScheme.surface
    else
        MaterialTheme.colorScheme.background
    val topColor = remember {
        Animatable(if (scrollState.canScrollBackward) appBarColor else scrolledAppBarColor)
    }

    val topElevation = remember {
        androidx.compose.animation.core.Animatable(if (scrollState.canScrollBackward) 30f else 0f)
    }

    LaunchedEffect(scrollState.canScrollBackward) {
        launch { topElevation.animateTo(if (scrollState.canScrollBackward) 30f else 0f) }
        launch { topColor.animateTo(if (scrollState.canScrollBackward) scrolledAppBarColor else appBarColor) }
    }

    systemUiController.setStatusBarColor(topColor.value)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = {
                            viewModel.save()
                            onClose()
                        }) {
                            Text(
                                text = stringResource(id = R.string.save),
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                },
                modifier = Modifier
                    .shadow(
                        elevation = topElevation.value.dp
                    )
                    .background(topColor.value),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                ),
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Filled.Close, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .padding(top = paddingValue.calculateTopPadding())
                .verticalScroll(scrollState)
        ) {
            if (uiState is EditItemUiState.Loaded) {
                Text(text = stringResource(id = R.string.long_test_text))
            }
        }
    }
}