package ar.edu.utn.frba.mobile.a2019c1.superheroes

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.content.Intent
import androidx.core.app.NotificationCompat.getExtras





class MainActivity : AppCompatActivity() {

    private lateinit var textMessage: TextView
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_cards -> {
                textMessage.setText(R.string.title_cards)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_envelope -> {
                textMessage.setText(R.string.title_envelope)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_fight -> {
                textMessage.setText(R.string.title_fight)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
;
        textMessage = findViewById(R.id.message)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        val username = intent.extras?.getString("username")
        this.title = "Welcome, $username!"
    }
}
