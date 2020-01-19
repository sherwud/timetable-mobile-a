package ru.fvds

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

// okhttp3
import okhttp3.*
// JSON
import kotlinx.serialization.json.*
import kotlinx.serialization.stringify
import kotlinx.serialization.Serializable
import kotlinx.serialization.ImplicitReflectionSerializer // map to json
// json by google
import com.google.gson.annotations.SerializedName

import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import kotlinx.serialization.parse
import java.io.IOException
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun onClickBtn(view: View) {
        try {
            val params = mapOf(
                "testname" to "testnametext",
                "testcomment" to "testcommenttext"
            )
            val url = "http://usd-suhanov.corp.tensor.ru:1444/test_page_2/";
            //doGetReuest(url, params, ::prepareResult, ::prepareError)
            data class Topic(
                @SerializedName("id") val id: Long,
                @SerializedName("name") val name: String,
                @SerializedName("image") val image: String,
                @SerializedName("description") val description: String
            )

        }
        catch (e: Exception) {
            val b = 7
        }
    }
    private fun prepareResult (res: String) {
        try {
            conn_btn.text = res
        }
        catch (e: Exception) {
            val b = 7
        }
    }
    private fun prepareError (ex: Exception) {
        try {
            conn_btn.text = ex.message
        }
        catch (e: Exception) {
            val b = 7
        }
    }


    @ImplicitReflectionSerializer
    fun onConnectBtnClickByOkhttpPost(view: View) {

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
                conn_btn.text = "Fuck is: ${e.message}"
            }
            override fun onResponse(call: Call, response: Response) {
                try {
                    //val a = "Response is: ${response.body()?.string()}"
                    val res = response.body()?.string().toString()
                    val new_res = json.parse(Field.serializer(), res)
                    conn_btn.text = res
                }
                catch (e: Exception) {
                    val a = 5
                }
            }
        })
    }


    fun onConnectBtnClickByOkhttpGet(view: View) {

        // val url = "http://usd-suhanov.corp.tensor.ru:1444/test_page_2/"
        val url = "http://test.fvds.ru/bl/api.php"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                conn_btn.text = "Fuck is: ${e.message}"
            }
            override fun onResponse(call: Call, response: Response) {
                try {
                    val a = "Response is: ${response.body()?.string()}"
                    conn_btn.text = a
                }
                catch (e: Exception) {
                    val a = 5
                }
            }
        })
    }
}
