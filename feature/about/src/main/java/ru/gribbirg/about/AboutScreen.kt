package ru.gribbirg.about

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.yandex.div.core.view2.Div2View
import ru.gribbirg.theme.custom.AppTheme
import ru.gribbirg.todoapp.about.databinding.FragmentContainerDivBinding
import ru.gribbirg.ui.components.CloseButton

@Composable
fun AboutScreen(
    viewModel: AboutViewModel,
    onBack: () -> Unit,
) {
    AboutScreenContent(
        createDivView = viewModel::createView,
        onBack = onBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AboutScreenContent(
    createDivView: (Context, LifecycleOwner, () -> Unit, Locale, darkTheme: Boolean) -> Div2View,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current
    val darkTheme = AppTheme.colors.isDark
    val locale = Locale.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    CloseButton(onClick = onBack)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.colors.primaryBack,
                    navigationIconContentColor = AppTheme.colors.blue,
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            AndroidViewBinding(
                FragmentContainerDivBinding::inflate,
                modifier = Modifier.padding(top = paddingValues.calculateTopPadding())
            ) {
                val divView = createDivView(context, lifeCycleOwner, onBack, locale, darkTheme)
                fragmentContainerView.addView(divView)
            }
            Spacer(modifier = Modifier.height(paddingValues.calculateBottomPadding()))
        }
    }
}

