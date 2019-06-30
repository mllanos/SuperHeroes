package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.fight

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
	private lateinit var cardsAdapterUser: CardsAdapter
	private lateinit var cardsAdapterOpponent: CardsAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_fight_result)

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

	fun showTeams(fight: Fight, myTeam: Team, opponentTeam: Team) {

		textview_fight_user.text = sessionService.getLoggedUser()?.nickname
		textview_fight_opponent.text = fight.opponent.nickname

		recycleview_fight_user.adapter = cardsAdapterUser
		cardsAdapterUser.replaceItems(myTeam.superheroes)

		recycleview_fight_opponent.adapter = cardsAdapterOpponent
		cardsAdapterOpponent.replaceItems(opponentTeam.superheroes)

		btn_fight_dofight.setOnClickListener {
			fight(fight.winner)
		}
	}

	fun fight(winner: String) {

		btn_fight_dofight.visibility = View.INVISIBLE
		textview_fight_info.visibility = View.VISIBLE
		textview_fight_info.text = getString(R.string.title_fight_loser)

		if (sessionService.getLoggedUser()?.nickname == winner) {
			textview_fight_info.text = getString(R.string.title_fight_winner)
		}

		btn_fight_close.setOnClickListener {
			finish()
		}

	}

}
