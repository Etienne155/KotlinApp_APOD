package com.etien.mykotlinapplication2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val urlAPOD = "https://api.nasa.gov/planetary/apod?api_key=UFMUzL1CFPs2aarVh9nQAqwi9ASq2UUi5eRCGowh"

        var textViewTitle = findViewById<TextView>(R.id.textViewTitle)
        var textViewDate = findViewById<TextView>(R.id.textViewDate)
        var imagedujour = findViewById<ImageView>(R.id.imagedujour)
        var textViewExplanation = findViewById<TextView>(R.id.textViewExplanation)
        var textViewCopyright = findViewById<TextView>(R.id.textViewCopyright)


        doAsync {

            val data = URL(urlAPOD).readText()
            val json = JSONObject(data)

            val apodData = APODData(
                json.get("copyright").toString(),
                json.get("date").toString(),
                json.get("explanation").toString(),
                json.get("media_type").toString(),
                json.get("title").toString(),
                json.get("url").toString()
            )

            uiThread {
                textViewTitle.setText(apodData.title)
                textViewDate.setText(apodData.date)
                textViewExplanation.setText(apodData.explanation)
                textViewCopyright.setText("Copyright: " + apodData.copyright)

                if (apodData.media_type == "image") {
                    Picasso
                        .get()
                        .load(apodData.url)
                        .into(imagedujour)
                } else {
                    Toast.makeText(this@MainActivity, "Video", Toast.LENGTH_SHORT).show()
                }

            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_about -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }

}
