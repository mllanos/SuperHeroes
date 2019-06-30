package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.fight

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.ApiService
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.SessionsService
import com.google.gson.Gson

class FightResultActivity : AppCompatActivity() {

	private val sessionService by lazy { SessionsService(this) }
	private val apiService by lazy { ApiService(this) }

	private val gson = Gson()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_fight_result)

		val response = intent.getParcelableExtra<ApiService.FightResponseResource>("fightResult")

		val opponent = response.opponent
		val winner = response.winner
		val opponentTeamId = opponent.team_id
		val opponentName = opponent.nickname
		Log.d("WINNER", winner)
		Log.d("OPPONENT_NAME", opponentName)
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
