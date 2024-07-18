package ru.gribbirg.about

import android.content.Context
import android.view.ContextThemeWrapper
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.yandex.div.core.Div2Context
import com.yandex.div.core.DivConfiguration
import com.yandex.div.picasso.PicassoDivImageLoader
import com.yandex.div.rive.OkHttpDivRiveNetworkDelegate
import com.yandex.div.rive.RiveCustomViewAdapter
import com.yandex.div.zoom.DivPinchToZoomConfiguration
import com.yandex.div.zoom.DivPinchToZoomExtensionHandler
import okhttp3.OkHttpClient
import ru.gribbirg.todoapp.about.R
import ru.gribbirg.todoapp.about.databinding.FragmentContainerDivBinding
import ru.gribbirg.ui.extensions.findActivity

@Composable
fun AboutScreen() {
    val context = LocalContext.current
    val themeContext = ContextThemeWrapper(context, R.style.Theme_TodoApp)
    val lifeCycleOwner = LocalLifecycleOwner.current

    AndroidViewBinding(FragmentContainerDivBinding::inflate) {
        val assetReader = AssetReader(context)

        val divJson = assetReader.read("sample.json")
        val templatesJson = divJson.optJSONObject("templates")
        val cardJson = divJson.getJSONObject("card")

        val divContext = Div2Context(
            baseContext = themeContext,
            configuration = createDivConfiguration(context),
            lifecycleOwner = lifeCycleOwner,
        )

        val divView = Div2ViewFactory(divContext, templatesJson).createView(cardJson)
        fragmentContainerView.addView(divView)
    }
}

private fun createDivConfiguration(context: Context): DivConfiguration {
    return DivConfiguration.Builder(PicassoDivImageLoader(context))
        .actionHandler(SampleDivActionHandler())
        .extension(
            DivPinchToZoomExtensionHandler(
                DivPinchToZoomConfiguration.Builder(
                    context.findActivity() ?: throw Exception("Activity not found")
                ).build()
            )
        )
        .divCustomContainerViewAdapter(
            RiveCustomViewAdapter.Builder(
                context,
                OkHttpDivRiveNetworkDelegate(OkHttpClient.Builder().build())
            ).build()
        )
        .visualErrorsEnabled(true)
        .build()
}