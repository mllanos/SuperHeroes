package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.fight

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.SessionsService
import ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.cards.CardsFragment
import kotlinx.android.synthetic.main.activity_fight_search.*

class FightSearchActivity : AppCompatActivity(){

	private val sessionService by lazy { SessionsService(this) }

	private val fragmentFightSearch = FightSearchFragment.newInstance()

	private val fragmentFightVictory = FightVictoryFragment.newInstance()

	private val handler = Handler()

	override fun onResume(){
		super.onResume()
		/*val openDialog = Dialog(this)
		openDialog.setContentView(R.layout.activity_fight_search)
		openDialog.setCancelable(false)
		btn_go_back.setOnClickListener{
			this.goBack()
		}*/
	}

	private fun goBack(){
		super.onBackPressed()
	}

	@SuppressLint("StringFormatMatches")
	override fun onCreate(savedInstanceState: Bundle?){
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_fight_search)

		this.setDefaultFragment()

		//handler.postDelayed({ showFightResult() }, 5000)

		sessionService.getLoggedUser()?.let { user ->
			//this.title = this.getString(R.string.welcome, user.nickname)
		}
	}

	private fun setDefaultFragment() = this.replaceFragment(fragmentFightSearch)

	private fun replaceFragment(destFragment: Fragment) = this.supportFragmentManager
		.beginTransaction()
		.replace(R.id.fragment_fight_container, destFragment)
		.commit()

	private fun showFightResult() = this.replaceFragment(fragmentFightVictory)


}
