package dev.cappee.vodafone_r216.main

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dev.cappee.vodafone_r216.R
import dev.cappee.vodafone_r216.api.Parser
import dev.cappee.vodafone_r216.databinding.ActivityMainBinding
import dev.cappee.vodafone_r216.main.adapter.DataAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.fixedRateTimer

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var progressDialog: ProgressDialog
    private var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        progressDialog = ProgressDialog(this, R.style.Dialog_Progress)
        progressDialog.apply {
            setTitle("Loading")
            setMessage("Initializing...")
            setCancelable(false)
            show()
        }

        html.observeForever {
            val result = lifecycleScope.async(Dispatchers.Default) {
                Parser.getData(it)
            }
            lifecycleScope.launch(Dispatchers.Main) {
                binding.content.recyclerview.apply {
                    layoutManager = LinearLayoutManager(
                        this@MainActivity,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    adapter = DataAdapter(this@MainActivity, result.await(), R.string.general)
                }
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
                            lifecycleScope.launch {
                                delay(1000)
                                loadUrl("javascript:window.html.parse(document.getElementsByTagName('html')[0].innerHTML);")
                                binding.content.root.visibility = View.VISIBLE
                                progressDialog.dismiss()
                            }
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