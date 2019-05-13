package ar.edu.utn.frba.mobile.a2019c1.superheroes

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.content.Intent
import androidx.appcompat.app.ActionBar
import androidx.core.app.NotificationCompat.getExtras
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener


class MainActivity : AppCompatActivity() {

	lateinit var toolbar: ActionBar

    private val onNavigationItemSelectedListener = OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_fight -> {
				toolbar.title="Pelea"
				val fightFragment = FightFragment.newInstance()
				openFragment(fightFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_envelope -> {
				toolbar.title="Abrir Sobre"
				val envelopeFragment = EnvelopeFragment.newInstance()
				openFragment(envelopeFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_cards -> {
				toolbar.title="Cartas"
				val cardsFragment = CardsFragment.newInstance()
				openFragment(cardsFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
	private fun openFragment(fragment: Fragment) {
		val transaction = supportFragmentManager.beginTransaction()
		transaction.replace(R.id.container, fragment)
		transaction.addToBackStack(null)
		transaction.commit()
	}
    override fun onCreate(savedInstanceState: Bundle?) {


		super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
		toolbar = supportActionBar!!
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)


        val username = intent.extras?.getString("username")
        this.title = "Welcome, $username!"
    }
}
