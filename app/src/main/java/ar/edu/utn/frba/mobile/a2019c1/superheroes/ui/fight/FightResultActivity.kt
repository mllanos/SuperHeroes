package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.fight

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R
import ar.edu.utn.frba.mobile.a2019c1.superheroes.adapters.CardsAdapter
import ar.edu.utn.frba.mobile.a2019c1.superheroes.domain.Fight
import ar.edu.utn.frba.mobile.a2019c1.superheroes.domain.Team
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.ApiService
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.SessionsService
import kotlinx.android.synthetic.main.activity_fight_result.*

class FightResultActivity : AppCompatActivity() {

	private val sessionService by lazy { SessionsService(this) }
	private val apiService by lazy { ApiService(this) }
	private var cardsAdapterUser = CardsAdapter()
	private var cardsAdapterOpponent = CardsAdapter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_fight_result)

		recycleview_fight_user.layoutManager = LinearLayoutManager(this)
		recycleview_fight_user.adapter = cardsAdapterUser

		recycleview_fight_opponent.layoutManager = LinearLayoutManager(this)
		recycleview_fight_opponent.adapter = cardsAdapterOpponent

		val fight = intent.getSerializableExtra("fightResult") as Fight
		sessionService.getLoggedUserTeamId()?.let { id ->
			apiService.getTeam(id, { myTeam ->
				apiService.getTeam(fight.opponent.team_id, { opponentTeam ->
					showTeams(fight, myTeam, opponentTeam)
				}, { error ->
					Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
				})
			}, { error ->
				Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
			})
		}
	}

	private fun showTeams(fight: Fight, myTeam: Team, opponentTeam: Team) {
		textview_fight_user.text =
			getString(R.string.title_fight_hero_info, sessionService.getLoggedUser()?.nickname, myTeam.totalPower)
		textview_fight_opponent.text =
			getString(R.string.title_fight_hero_info, fight.opponent.nickname, opponentTeam.totalPower)

		cardsAdapterUser.replaceItems(myTeam.superheroes)
		cardsAdapterOpponent.replaceItems(opponentTeam.superheroes)

		btn_fight_dofight.setOnClickListener {
			fight(fight.winner)
		}
	}

	fun fight(winner: String) {
		btn_fight_dofight.visibility = View.INVISIBLE
		textview_fight_info.visibility = View.VISIBLE
		textview_fight_info.text = getString(R.string.title_fight_loser)
		btn_fight_close.visibility = View.VISIBLE

		if (sessionService.getLoggedUser()?.nickname == winner) {
			textview_fight_info.text = getString(R.string.title_fight_winner)
		}

		btn_fight_close.setOnClickListener {
			finish()
		}
	}

}
