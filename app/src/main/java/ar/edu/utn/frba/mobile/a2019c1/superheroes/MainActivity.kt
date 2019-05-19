package ar.edu.utn.frba.mobile.a2019c1.superheroes

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.cards.CardsFragment
import ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.envelope.EnvelopeFragment
import ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.fight.FightFragment

class MainActivity : AppCompatActivity() {

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_cards -> {
				val fragmentCards = CardsFragment.newInstance()
				this.replaceFragment(fragmentCards)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_envelope -> {
				val fragmentEnvelope = EnvelopeFragment.newInstance()
				this.replaceFragment(fragmentEnvelope)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_fight -> {
				val fragmentFight = FightFragment.newInstance()
				this.replaceFragment(fragmentFight)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

	fun setDefaultFragment(destFragment: Fragment) {
		this.replaceFragment(destFragment)
	}

	fun replaceFragment(destFragment: Fragment) {
		var fragmentManager: FragmentManager = this.supportFragmentManager
		var fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
		fragmentTransaction.replace(R.id.fragment_container,destFragment)
		fragmentTransaction.commit()
	}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

		val fragmentDefault = CardsFragment.newInstance()
		this.setDefaultFragment(fragmentDefault)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        val username = intent.extras?.getString("username")
        this.title = "Welcome, $username!"
    }
}
