package dev.cappee.vodafone_r216

import android.graphics.Bitmap
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.html)
        textView.movementMethod = ScrollingMovementMethod()

        /*val result = GlobalScope.async(Dispatchers.IO) {
            URL("http://192.168.1.1").readText()
        }

        GlobalScope.launch(Dispatchers.Main) {
            textView.text = result.await()
        }*/

        html.observeForever {
            textView.text = it
        }

        val webView = findViewById<WebView>(R.id.webview)
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(this, "HTMLOUT")
        webView.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                webView.loadUrl("javascript:window.HTMLOUT.html('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');")
            }
        })
        webView.loadUrl("http://192.168.1.1")

    }

    @JavascriptInterface
    fun html(html: String) {
        MainActivity.html.postValue(html)
    }

    companion object {
        val html = MutableLiveData<String>()
    }

}