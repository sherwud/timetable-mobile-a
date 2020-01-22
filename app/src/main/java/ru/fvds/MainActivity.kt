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


@Serializable
class Field {
    var mystatus: Int = 0
    var mymessage: String = ""
    var myextmessage: String = "42"
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    @ImplicitReflectionSerializer
    fun onClickBtn(view: View) {
        try {

            val url = "http://usd-suhanov.corp.tensor.ru:1444/test_page_2/";

            val getParams = mapOf(
                "name" to "Andru",
                "comment" to "good boy",
                "age" to 27,
                "checked" to true
            )

            doGetReuest(url, getParams, Field.serializer(), ::prepareGetResult, errback=::prepareError)

            val postParams = json {
                "name" to "Andru"
                "comment" to "good boy"
                "age" to 27
                "checked" to true
            }

            doPostRequest(url, postParams, Field.serializer(), ::preparePostResult, ::prepareError)
        }
        catch (e: Exception) {
            val b = 7
        }
    }

    private fun prepareGetResult (res: Field) {
        try {
            conn_btn.text = res.toString()
        }
        catch (e: Exception) {
            val b = 7
        }
    }
    private fun preparePostResult (res: Field) {
        try {
            conn_btn.text = res.toString()
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
}
