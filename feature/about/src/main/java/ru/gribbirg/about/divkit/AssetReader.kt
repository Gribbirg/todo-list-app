package ru.gribbirg.about.divkit

import android.content.Context
import com.yandex.div.core.annotations.InternalApi
import com.yandex.div.internal.util.IOUtils
import org.json.JSONObject
import javax.inject.Inject

internal class AssetReader @Inject constructor(private val context: Context) {

    @OptIn(InternalApi::class)
    fun read(filename: String): JSONObject {
        val data = IOUtils.toString(context.assets.open(filename))
        return JSONObject(data)
    }
}