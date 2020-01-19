package ru.fvds

// okhttp3
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
fun doGetReuest (url: String,
                 params: Map<String, String>,
                 callback: (res: String) -> Unit,
                 errback: (ex: Exception) -> Unit) {
    try {
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
                    val res = response.body()?.string().toString()
                    callback(res)
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
fun doPostRequest (url:String,
                   params: Map <String, String>,
                   callback: (res: String) -> Unit,
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
                callback(response.body()?.string().toString())
            }
            catch (ex: Exception) {
                errback(ex)
            }
        }
    })
}


@ImplicitReflectionSerializer
fun onConnectBtnClickByOkhttpPostSingle() {

    val json = Json(JsonConfiguration.Stable)

    val url = "http://usd-suhanov.corp.tensor.ru:1444/test_page_2/"
    // val url = "http://test.fvds.ru/bl/api.php"

    @Serializable
    class Field {
        var mystatus: Int = 0
        var mymessage: String = ""
        var myextmessage: String = "42"
    }

    val postBody = "{\"name123\": \"morpheus\"}"
    val params = json.stringify(mapOf(
        "period" to "2019-11-01",
        "user" to "Ринат Абрамович"
    ))


    val client = OkHttpClient()
    val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), params)

    val request = Request.Builder().url(url).post(body).build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            val myErr = "Fuck is: ${e.message}"
        }
        override fun onResponse(call: Call, response: Response) {
            try {
                //val a = "Response is: ${response.body()?.string()}"
                val res = response.body()?.string().toString()
                val new_res = json.parse(Field.serializer(), res)

            }
            catch (e: Exception) {
                val a = 5
            }
        }
    })
}

fun onConnectBtnClickByOkhttpGetSingle() {

    // val url = "http://usd-suhanov.corp.tensor.ru:1444/test_page_2/"
    val url = "http://test.fvds.ru/bl/api.php"
    val client = OkHttpClient()
    val request = Request.Builder().url(url).build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            val myErr = "Fuck is: ${e.message}"
        }
        override fun onResponse(call: Call, response: Response) {
            try {
                val a = "Response is: ${response.body()?.string()}"
            }
            catch (e: Exception) {
                val a = 5
            }
        }
    })
}