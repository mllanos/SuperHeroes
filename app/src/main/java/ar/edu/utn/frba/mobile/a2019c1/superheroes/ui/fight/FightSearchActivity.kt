package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.fight

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.ApiService
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.SessionsService
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_fight_search.*

class FightSearchActivity : AppCompatActivity(){

	private val sessionService by lazy { SessionsService(this) }
	private val apiService by lazy { ApiService(this) }

	@SuppressLint("StringFormatMatches", "MissingPermission")
	override fun onCreate(savedInstanceState: Bundle?){
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_fight_search)

		LocationServices.getFusedLocationProviderClient(this)
			.lastLocation.addOnSuccessListener { location: Location? ->
			sessionService.getLoggedUser()?.let { user ->
				apiService.startFight(user.id, location, System.currentTimeMillis(),
					{ id ->
						Toast.makeText(this, id.toString(), Toast.LENGTH_LONG).show()
						startActivity(Intent(this, FightVictoryFragment::class.java))
					},
					{ error ->
						title_search_fight.text = "No se han encontrado oponentes"
						loading_figth_icon.visibility = View.INVISIBLE
					})
			}
		}
	}
/*
	override fun onStart() {
		super.onStart()

		if (!checkPermissions()) {
			requestPermissions()
		}
	}

	private fun checkPermissions(): Boolean {
		val permissionState = ActivityCompat.checkSelfPermission(
			activity!!,
			Manifest.permission.ACCESS_COARSE_LOCATION
		)
		return permissionState == PackageManager.PERMISSION_GRANTED
	}

	private fun startLocationPermissionRequest() {
		ActivityCompat.requestPermissions(
			activity!!,
			arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
			34
		)
	}

	private fun requestPermissions() {
		val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
			activity!!,
			Manifest.permission.ACCESS_COARSE_LOCATION
		)

		// Provide an additional rationale to the user. This would happen if the user denied the
		// request previously, but didn't check the "Don't ask again" checkbox.
		if (shouldProvideRationale) {
			Log.i("Location", "Displaying Manifest.permission rationale to provide additional context.")
		} else {
			Log.i("Location", "Requesting Manifest.permission")
			// Request permission. It's possible this can be auto answered if device policy
			// sets the permission in a given state or the user denied the permission
			// previously and checked "Never ask again".
			startLocationPermissionRequest()
		}
	}


	companion object {
		@JvmStatic
		fun newInstance() = FightSearchFragment()
	}

	private fun gerErrorMessage(message: String, error: VolleyError) {
		val statusCode = error.networkResponse.statusCode
		val data = String(error.networkResponse.data, Charsets.UTF_8)
		val message = "$message - statusCode: $statusCode - data: $data"
		println(message)
		message
	}

*/

}
