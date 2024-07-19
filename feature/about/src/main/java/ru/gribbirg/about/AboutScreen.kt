package ru.gribbirg.about

import android.content.Context
import android.view.ContextThemeWrapper
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.yandex.div.core.Div2Context
import com.yandex.div.core.DivConfiguration
import com.yandex.div.core.expression.variables.DivVariableController
import com.yandex.div.data.Variable
import com.yandex.div.picasso.PicassoDivImageLoader
import com.yandex.div.rive.OkHttpDivRiveNetworkDelegate
import com.yandex.div.rive.RiveCustomViewAdapter
import okhttp3.OkHttpClient
import ru.gribbirg.theme.custom.AppTheme
import ru.gribbirg.todoapp.about.R
import ru.gribbirg.todoapp.about.databinding.FragmentContainerDivBinding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val themeContext = ContextThemeWrapper(context, R.style.Theme_TodoApp)
    val lifeCycleOwner = LocalLifecycleOwner.current
    val darkTheme = AppTheme.colors.isDark
    val locale = Locale.current.language

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = null, // TODO
                        )
                    }
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
                val assetReader = AssetReader(context)

                val divJson = assetReader.read("about.json")
                val templatesJson = divJson.optJSONObject("templates")
                val cardJson = divJson.getJSONObject("card")
                val variableController = DivVariableController()

                val divContext = Div2Context(
                    baseContext = themeContext,
                    configuration = createDivConfiguration(context, variableController, onBack),
                    lifecycleOwner = lifeCycleOwner,
                )

                variableController.putOrUpdate(
                    Variable.StringVariable(
                        "app_theme",
                        if (darkTheme) "dark" else "light",
                    )
                )

                variableController.putOrUpdate(
                    Variable.StringVariable(
                        "lang",
                        locale,
                    )
                )

                val divView = Div2ViewFactory(divContext, templatesJson).createView(cardJson)

                fragmentContainerView.addView(divView)
            }
            Spacer(modifier = Modifier.height(paddingValues.calculateBottomPadding()))
        }
    }
}

private fun createDivConfiguration(
    context: Context,
    variableController: DivVariableController,
    goBack: () -> Unit,
): DivConfiguration {
    return DivConfiguration.Builder(PicassoDivImageLoader(context))
        .actionHandler(TodoAppDivActionHandler(goBack))
        .divCustomContainerViewAdapter(
            RiveCustomViewAdapter.Builder(
                context,
                OkHttpDivRiveNetworkDelegate(OkHttpClient.Builder().build())
            ).build()
        )
        .divVariableController(variableController)
        .visualErrorsEnabled(true)
        .build()
}