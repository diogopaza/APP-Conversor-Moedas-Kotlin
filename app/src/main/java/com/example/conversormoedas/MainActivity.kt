package com.example.conversormoedas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.net.URL
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import org.json.JSONObject
import java.text.DecimalFormat
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        converter()
    }

    private fun converter() {
        val result = findViewById<TextView>(R.id.text_result);
        val buttonConverter = findViewById<Button>(R.id.btn_converter);
        buttonConverter.setOnClickListener() {
            val valueRadio = findViewById<RadioGroup>(R.id.radioSelecaoMoeda);
            val radioButtonChecked = valueRadio.checkedRadioButtonId;
            when (radioButtonChecked) {
                R.id.radio_usd -> {
                    getURL("usd",
                        "https://economia.awesomeapi.com.br/json/last/USD",
                        result
                    );
                }
                R.id.radio_eur -> {
                    getURL("eur",
                        "https://economia.awesomeapi.com.br/json/last/EUR",
                        result
                    );
                }
            }
        }
    }

    private fun getURL(coin: String, url: String, textView: TextView) {
            //aqui acontece em paralelo
            Thread {
                val input = findViewById<EditText>(R.id.edit_field);
                val pathCoin: String
                when(coin){
                    "usd" ->  { pathCoin = "USDBRL" }
                    "eur" ->  { pathCoin = "EURBRL" }
                     else -> { pathCoin = "" }
                }
                val url = URL(url)
                val conn = url.openConnection() as HttpsURLConnection
                try {
                    val data = conn.inputStream.bufferedReader().readText()
                    runOnUiThread(){
                        val valorMoeda = (JSONObject(data).get(pathCoin)
                                as JSONObject).getString("bid").toFloat()
                        val resultCalculation = input.text.toString().toFloat()  /
                                valorMoeda
                        val df = DecimalFormat("#.##")
                        textView.text = df.format(resultCalculation).toString()
                        textView.visibility = View.VISIBLE                    }
                    } catch (ex: Exception) {
                    println(ex.message)
                } finally {
                    conn.disconnect()
                }
            }.start()
        }
}



