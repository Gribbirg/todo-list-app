package ru.gribbirg.about

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
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
import ru.gribbirg.ui.components.AnimatedTopAppBar
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

@Composable
private fun AboutScreenContent(
    createDivView: (Context, LifecycleOwner, () -> Unit, Locale, darkTheme: Boolean) -> Div2View,
    onBack: () -> Unit,
) {
    val scrollState = rememberScrollState()

    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current
    val darkTheme = AppTheme.colors.isDark
    val locale = Locale.current

    Scaffold(
        topBar = {
            AnimatedTopAppBar(
                scrollState = scrollState,
                title = {},
                navigationIcon = {
                    CloseButton(onClick = onBack)
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.verticalScroll(scrollState)
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

