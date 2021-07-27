package dev.cappee.vodafone_r216.main

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import dev.cappee.vodafone_r216.api.Parser
import dev.cappee.vodafone_r216.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.content.html.movementMethod = ScrollingMovementMethod()

        html.observeForever {
            //binding.content.html.text = Parser.getData(it).toString()
            val result = lifecycleScope.async(Dispatchers.Default) {
                Parser.getData(it).toString()
            }
            lifecycleScope.launch(Dispatchers.Main) {
                binding.content.html.text = result.await()
            }
        }

        lifecycleScope.launch(Dispatchers.Main) {
            binding.webview.apply {
                settings.javaScriptEnabled = true
                addJavascriptInterface(this@MainActivity, "html")
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView, url: String) {
                        if (i==0) {
                            i++
                        } else {
                            loadUrl("javascript:window.html.parse(document.getElementsByTagName('html')[0].innerHTML);")
                            binding.progressBar.visibility = View.INVISIBLE
                            binding.content.root.visibility = View.VISIBLE
                        }
                    }
                }
                loadUrl("http://192.168.1.1")
            }
        }

    }

    @JavascriptInterface
    fun parse(html: String) {
        Companion.html.postValue(html)
    }

    companion object {
        val html = MutableLiveData<String>()
    }

}