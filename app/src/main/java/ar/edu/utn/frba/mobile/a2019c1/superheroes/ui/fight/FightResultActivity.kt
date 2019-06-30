package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.fight

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R
import ar.edu.utn.frba.mobile.a2019c1.superheroes.domain.Fight
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.ApiService
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.SessionsService

class FightResultActivity : AppCompatActivity() {

	private val sessionService by lazy { SessionsService(this) }
	private val apiService by lazy { ApiService(this) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_fight_result)

		val fight = intent.getSerializableExtra("fightResult") as Fight
		val opponent = fight.opponent
		val winner = fight.winner
		val opponentTeamId = opponent.team_id
		val opponentName = opponent.nickname

		sessionService.getLoggedUserTeamId()?.let { id ->
			apiService.getTeam(id, { team ->
				Log.d("MY_TEAM", team.toString())
				apiService.getTeam(opponentTeamId, { opponentTeam ->
					// TODO show teams
					Log.d("OPPONENT_TEAM", opponentTeam.toString())
				}, { error ->
					Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
				})
			}, { error ->
				Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
			})
		}
	}

}
