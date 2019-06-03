package ar.edu.utn.frba.mobile.a2019c1.superheroes.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class PagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

	private val fragments = ArrayList<Fragment>()
	lateinit var navigator: BottomNavigationView

	override fun getItem(position: Int): Fragment {

		return when (position) {
			0 -> fragments[0]
			1 -> fragments[1]
			else -> fragments[2]
		}
	}

	override fun getCount() = fragments.size

	fun addFragment(fragment: Fragment): PagerAdapter {
		fragments.add(fragment)
		return this
	}

}
