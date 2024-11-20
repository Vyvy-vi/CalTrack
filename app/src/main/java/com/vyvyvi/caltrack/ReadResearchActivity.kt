package com.vyvyvi.caltrack

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintJob
import android.print.PrintManager
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.vyvyvi.caltrack.utils.ArticleAdapter

data class Article(val title: String, val link: String)

val articles = arrayOf(
    Article(
        title = "10 tips for successful weight loss",
        link = "https://www.medicalnewstoday.com/articles/303409"
    ),
    Article(
        title = "Article on Health and Fitness",
        link = "https://infinitylearn.com/surge/english/article/article-on-health-and-fitness/"
    ),
    Article(
        title = "26 Simple Diet and Fitness Tips",
        link = "https://www.health.com/weight-loss/30-simple-diet-and-fitness-tips"
    ),
    Article(
        title = "Benefits of eating healthy: Heart health, better mood, and more",
        link = "https://www.medicalnewstoday.com/articles/322268"
    ),
    Article(
        title = "Healthy Living: Diet & Exercise Tips, and Things to Avoid",
        link = "https://www.medicinenet.com/healthy_living/article.htm"
    ),
    Article(
        title = "The Importance of Sleep for Weight Loss",
        link = "https://www.sleepfoundation.org/how-sleep-affects-health/how-sleep-affects-weight-loss"
    ),
    Article(
        title = "How to Start a Fitness Routine",
        link = "https://www.healthline.com/health/fitness-program-for-beginners"
    ),
    Article(
        title = "The Best Diet for Weight Loss",
        link = "https://www.healthline.com/nutrition/best-diet-for-weight-loss"
    ),
    Article(
        title = "The Importance of Hydration",
        link = "https://www.healthline.com/nutrition/importance-of-hydration"
    ),
    Article(
        title = "The Mental Health Benefits of Exercise",
        link = "https://www.helpguide.org/articles/stress/exercise-stress-anxiety.htm"
    )
)

class ReadResearchActivity : AppCompatActivity() {
    private lateinit var lv: ListView
    var printJob: PrintJob? = null
    var printWeb: WebView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_research)

        lv = findViewById(R.id.lv)
        val adapter = ArticleAdapter(this, articles)
        lv.adapter = adapter

        val title = findViewById<TextView>(R.id.title)
        val webView = findViewById<WebView>(R.id.webview)
        val downlaodBtn = findViewById<Button>(R.id.downloadBtn)
        val closeBtn = findViewById<Button>(R.id.closeBtn)

        lv.setOnItemClickListener { _, _, position, _ ->
            val article = articles[position]
            webView.visibility = View.VISIBLE
            lv.visibility = View.GONE
            downlaodBtn.visibility = View.VISIBLE
            closeBtn.visibility = View.VISIBLE
            title.text = article.title

            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    printWeb = webView
                }
            }

            webView.loadUrl(article.link)

            downlaodBtn.setOnClickListener {
                if (printWeb != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        // calling createWebPrintJob()
                        PrintTheWebPage(printWeb!!)
                    } else {
                        Toast.makeText(
                            this,
                            "Not available for devices below Android LOLLIPOP",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(this, "Webpage not fully loaded", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        closeBtn.setOnClickListener {
            title.text = "View Articles"
            lv.visibility = View.VISIBLE
            webView.visibility = View.GONE
            downlaodBtn.visibility = View.GONE
            closeBtn.visibility = View.GONE
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun PrintTheWebPage(webView: WebView) {
        val printManager = this
            .getSystemService(Context.PRINT_SERVICE) as PrintManager

        val jobName = "CSE227_ANDROID_2024" + " webpage" + webView.url

        // PrintDocumentAdapter instance
        val printAdapter = webView.createPrintDocumentAdapter(jobName)
        assert(printManager != null)
        printJob = printManager.print(
            jobName, printAdapter,
            PrintAttributes.Builder().build()
        )
    }
}