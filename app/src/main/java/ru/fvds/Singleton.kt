package ru.fvds

// okhttp3
import kotlinx.serialization.DeserializationStrategy
import okhttp3.*
// JSON
import kotlinx.serialization.json.*
import kotlinx.serialization.stringify
import kotlinx.serialization.Serializable
import kotlinx.serialization.ImplicitReflectionSerializer // map to json

import java.io.IOException
import java.lang.Exception

/**
 * Синглтон для выполнения GET запроса
 * @param url целевой host, в конце обязателен '/'
 * @param params Map параметров вызова, которые будут переданы при вызове
 * @param callback функция-обработчик, успешного выполнения запроса
 * @param errback функция-обработчик, ошибки, возникшей при целевом вызове
 */
fun <T> doGetReuest (url: String,
                 params: Map<String, Any>,
                 deserializationStrategy: DeserializationStrategy<T>,
                 callback: (res: T) -> Unit,
                 errback: (ex: Exception) -> Unit) {
    try {
        val json = Json(JsonConfiguration.Stable)
        // генерация url для GET запроса
        val urlBuilder = StringBuilder()

        urlBuilder.append("$url?")

        for ((key, value) in params) {
            urlBuilder.append("$key=$value&")
        }
        val targetUrl = urlBuilder.toString()

        // вызов
        val client = OkHttpClient()
        val request = Request.Builder().url(targetUrl).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, ex: IOException) {
                errback(ex)
            }
            override fun onResponse(call: Call, response: Response) {
                try {
                    val bodyStr = (response.body()?.string() ?: "").toString()
                    val jsonVal = json.parse(deserializationStrategy, bodyStr)
                    callback(jsonVal)
                }
                catch (ex: Exception) {
                    errback(ex)
                }
            }
        })
    }
    catch (ex: Exception) {
        errback(ex)
    }
}


/**
 * Синглтон для выполнения POST запроса
 * @param url целевой host, в конце обязателен '/'
 * @param params Map параметров вызова, которые будут переданы при вызове
 * @param callback функция-обработчик, успешного выполнения запроса
 * @param errback функция-обработчик, ошибки, возникшей при целевом вызове
 */
@ImplicitReflectionSerializer
fun <T> doPostRequest (url:String,
                   params: JsonObject,
                   deserializationStrategy: DeserializationStrategy<T>,
                   callback: (res: T) -> Unit,
                   errback: (ex: Exception) -> Unit) {

    val json = Json(JsonConfiguration.Stable)
    val client = OkHttpClient()
    val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.stringify(params))

    val request = Request.Builder().url(url).post(body).build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, ex: IOException) {
            errback(ex)
        }
        override fun onResponse(call: Call, response: Response) {
            try {
                val bodyStr = (response.body()?.string() ?: "").toString()
                val jsonVal = json.parse(deserializationStrategy, bodyStr)
                callback(jsonVal)
            }
            catch (ex: Exception) {
                errback(ex)
            }
        }
    })
}