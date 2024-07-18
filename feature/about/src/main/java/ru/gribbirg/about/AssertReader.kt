package ru.gribbirg.about

import android.content.Context
import com.yandex.div.core.annotations.InternalApi
import com.yandex.div.internal.util.IOUtils
import org.json.JSONObject

internal class AssetReader(private val context: Context) {

    @OptIn(InternalApi::class)
    fun read(filename: String): JSONObject {
        val data = IOUtils.toString(context.assets.open(filename))
        return JSONObject(data)
    }
}