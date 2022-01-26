package io.appwrite.almostnetflix

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.appwrite.Client
import io.appwrite.almostnetflix.core.Configuration

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        Configuration.client = Client(this)
            .setEndpoint(Configuration.ENDPOINT)
            .setProject(Configuration.PROJECT_ID)
            .setSelfSigned(true)
    }
}