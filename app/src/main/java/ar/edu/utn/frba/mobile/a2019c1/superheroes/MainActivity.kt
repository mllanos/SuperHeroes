package ar.edu.utn.frba.mobile.a2019c1.superheroes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import ar.edu.utn.frba.mobile.a2019c1.superheroes.adapters.PagerAdapter
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.SessionsService
import ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.bundle.BundleFragment
import ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.cards.CardsFragment
import ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.fight.FightFragment
import ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.registration.RegistrationActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

	private val fragmentCards = CardsFragment.newInstance()
	private val fragmentBundle = BundleFragment.newInstance()
	private val fragmentFight = FightFragment.newInstance()

	private val sessionService by lazy { SessionsService(this) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		val navView = findViewById<BottomNavigationView>(R.id.nav_view)
		val pager = findViewById<ViewPager>(R.id.fragment_container)
		val adapter = PagerAdapter(supportFragmentManager)
		adapter.navigator = navView

		pager.adapter = adapter
			.addFragment(fragmentCards)
			.addFragment(fragmentBundle)
			.addFragment(fragmentFight)

		navView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
			when (item.itemId) {
				R.id.navigation_cards -> pager.currentItem = 0
				R.id.navigation_bundle -> pager.currentItem = 1
				R.id.navigation_fight -> pager.currentItem = 2
			}
			return@OnNavigationItemSelectedListener true
		})

		pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

			override fun onPageScrollStateChanged(state: Int) {}

			override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

			override fun onPageSelected(position: Int) {
				navView.selectedItemId = when(position) {
					0 -> R.id.navigation_cards
					1 -> R.id.navigation_bundle
					else -> R.id.navigation_fight
				}
			}
		})

		pager.currentItem = 1

		sessionService.getLoggedUser()?.let { user ->
			this.title = this.getString(R.string.welcome, user.nickname)
		} ?: handleUserNotLogged()
	}

	private fun handleUserNotLogged() {
		println("Failed to get logged user in main activity")
		val intent = Intent(this@MainActivity, RegistrationActivity::class.java)
		startActivity(intent)
		setResult(Activity.RESULT_OK)
		finish()
	}

}
