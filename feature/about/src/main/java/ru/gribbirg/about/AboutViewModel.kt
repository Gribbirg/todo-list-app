package ru.gribbirg.about

import android.content.Context
import android.view.ContextThemeWrapper
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.yandex.div.core.Div2Context
import com.yandex.div.core.DivConfiguration
import com.yandex.div.core.expression.variables.DivVariableController
import com.yandex.div.core.view2.Div2View
import com.yandex.div.data.Variable
import com.yandex.div.picasso.PicassoDivImageLoader
import ru.gribbirg.about.divkit.AssetReader
import ru.gribbirg.about.divkit.Div2ViewFactory
import ru.gribbirg.todoapp.about.R
import javax.inject.Inject

class AboutViewModel @Inject internal constructor(
    assetReader: AssetReader,
) : ViewModel() {
    private val divJson = assetReader.read(JSON_NAME)
    private val templatesJson = divJson.optJSONObject(TEMPLATES)
    private val cardJson = divJson.getJSONObject(CARD)

    internal fun createView(
        context: Context,
        lifeCycleOwner: LifecycleOwner,
        onBack: () -> Unit,
        locale: Locale,
        darkTheme: Boolean,
    ): Div2View {
        val themeContext = ContextThemeWrapper(context, R.style.Theme_TodoApp)
        val variableController = DivVariableController()
        val divContext = Div2Context(
            baseContext = themeContext,
            configuration = createDivConfiguration(context, variableController, onBack),
            lifecycleOwner = lifeCycleOwner,
        )

        variableController.putOrUpdate(
            Variable.StringVariable(
                APP_THEME_NAME,
                if (darkTheme) DARK_THEME_NAME else LIGHT_THEME_NAME,
            )
        )

        variableController.putOrUpdate(
            Variable.StringVariable(
                LANG_NAME,
                locale.language,
            )
        )

        return Div2ViewFactory(divContext, templatesJson).createView(cardJson)
    }

    private fun createDivConfiguration(
        context: Context,
        variableController: DivVariableController,
        goBack: () -> Unit,
    ): DivConfiguration {
        return DivConfiguration.Builder(PicassoDivImageLoader(context))
            .actionHandler(TodoAppDivActionHandler(goBack))
            .divVariableController(variableController)
            .visualErrorsEnabled(true)
            .build()
    }

    companion object {
        private const val JSON_NAME = "about.json"
        private const val TEMPLATES = "templates"
        private const val CARD = "card"
        private const val LANG_NAME = "lang"
        private const val APP_THEME_NAME = "app_theme"
        private const val LIGHT_THEME_NAME = "light"
        private const val DARK_THEME_NAME = "dark"
    }
}