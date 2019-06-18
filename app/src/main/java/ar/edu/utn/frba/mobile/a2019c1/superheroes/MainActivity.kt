package ar.edu.utn.frba.mobile.a2019c1.superheroes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ar.edu.utn.frba.mobile.a2019c1.superheroes.domain.User
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.ApiService
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.SessionsService
import ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.bundle.BundleFragment
import ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.cards.CardsFragment
import ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.fight.FightFragment
import ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.registration.RegistrationActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

	private val fragmentCards = CardsFragment.newInstance()
	private val fragmentBundle = BundleFragment.newInstance()
	private val fragmentFight = FightFragment.newInstance()

	private val sessionService by lazy { SessionsService(this) }
	private val apiService by lazy { ApiService(this) }

	private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
		return@OnNavigationItemSelectedListener when (item.itemId) {
			R.id.navigation_cards -> {
				this.replaceFragment(fragmentCards)
				true
			}
			R.id.navigation_bundle -> {
				this.replaceFragment(fragmentBundle)
				true
			}
			R.id.navigation_fight -> {
				this.replaceFragment(fragmentFight)
				true
			}
			else -> false
		}
	}

	private fun setDefaultFragment() = this.replaceFragment(fragmentCards)

	private fun replaceFragment(destFragment: Fragment) = this.supportFragmentManager
		.beginTransaction()
		.replace(R.id.fragment_container, destFragment)
		.commit()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

		this.setDefaultFragment()

		this.title = "" // sino muestra MainActivity mientras hace el get a la api
		sessionService.getLoggedUser()?.also { user ->
			sessionService.getLoggedUserTeamId()?.also { id ->
				apiService.getTeam(id, { team ->
					this.title = getString(R.string.welcome, user.nickname, team.totalPower)
				}, { error ->
					defaultTitle(user)
				})
			} ?: defaultTitle(user)
		} ?: handleUserNotLogged()
	}

	private fun defaultTitle(user: User) {
		this.title = getString(R.string.welcome, user.nickname, 0)
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.top_nav_menu, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
		R.id.logout -> {
			sessionService.logout()
			startRegistrationActivity()
			true
		}
		else -> {
			super.onOptionsItemSelected(item)
		}
	}

	private fun handleUserNotLogged() {
		println("Failed to get logged user in main activity")
		startRegistrationActivity()
	}

	private fun startRegistrationActivity() {
		val intent = Intent(this@MainActivity, RegistrationActivity::class.java)
		startActivity(intent)
		setResult(Activity.RESULT_OK)
		finish()
	}

}
