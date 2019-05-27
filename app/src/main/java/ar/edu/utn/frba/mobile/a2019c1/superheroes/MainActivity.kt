package ar.edu.utn.frba.mobile.a2019c1.superheroes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.SessionsService
import ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.cards.CardsFragment
import ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.envelope.EnvelopeFragment
import ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.fight.FightFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

	val fragmentCards = CardsFragment.newInstance()
	val fragmentEnvelope = EnvelopeFragment.newInstance()
	val fragmentFight = FightFragment.newInstance()
	private val sessionService by lazy { SessionsService(this) }

	private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
		return@OnNavigationItemSelectedListener when (item.itemId) {
			R.id.navigation_cards -> {
				this.replaceFragment(fragmentCards)
				true
			}
			R.id.navigation_envelope -> {
				this.replaceFragment(fragmentEnvelope)
				true
			}
			R.id.navigation_fight -> {
				this.replaceFragment(fragmentFight)
				true
			}
			else -> false
		}
	}

	private fun setDefaultFragment(destFragment: Fragment) {
		this.replaceFragment(destFragment)
	}

	private fun replaceFragment(destFragment: Fragment) {
		val fragmentManager = this.supportFragmentManager
		val fragmentTransaction = fragmentManager.beginTransaction()
		fragmentTransaction.replace(R.id.fragment_container, destFragment)
		fragmentTransaction.commit()
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		val navView: BottomNavigationView = findViewById(R.id.nav_view)

		val fragmentDefault = CardsFragment.newInstance()
		this.setDefaultFragment(fragmentDefault)

		navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

		val user = sessionService.getLoggedUser()!!
		this.title = this.getString(R.string.welcome, user.nickname)
	}

}
