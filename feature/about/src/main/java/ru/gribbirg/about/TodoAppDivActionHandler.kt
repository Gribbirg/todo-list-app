package ru.gribbirg.about

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.yandex.div.core.DivActionHandler
import com.yandex.div.core.DivViewFacade
import com.yandex.div.json.expressions.ExpressionResolver
import com.yandex.div2.DivAction

internal class TodoAppDivActionHandler(
    private val goBack: () -> Unit,
) : DivActionHandler() {
    override fun handleAction(
        action: DivAction,
        view: DivViewFacade,
        resolver: ExpressionResolver
    ): Boolean {
        val url =
            action.url?.evaluate(resolver) ?: return super.handleAction(action, view, resolver)
        val context = view.view.context

        return when {
            url.scheme in BROWSER_LINKS_SCHEME && handleBrowserAction(url, context) -> true
            url.scheme == NAV_SCHEME && handleNavAction(url) -> true
            else -> super.handleAction(action, view, resolver)
        }
    }

    private fun handleBrowserAction(url: Uri, context: Context): Boolean {
        val urlIntent = Intent(
            Intent.ACTION_VIEW,
            url,
        )
        context.startActivity(urlIntent)
        return true
    }

    private fun handleNavAction(action: Uri): Boolean {
        return when (action.host) {
            BACK_ACTION -> {
                goBack()
                true
            }

            else -> false
        }
    }

    companion object {
        private const val NAV_SCHEME = "nav-action"
        private val BROWSER_LINKS_SCHEME = listOf("https", "http", "mailto")
        private const val BACK_ACTION = "back"
    }
}