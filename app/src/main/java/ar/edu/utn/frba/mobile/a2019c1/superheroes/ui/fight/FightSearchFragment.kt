package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.fight

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.ApiService
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.SessionsService
import ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.cards.CardsFragment
import ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.cards.ShowSingleCard
import com.android.volley.VolleyError
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.sql.Timestamp

class FightSearchFragment : Fragment() {
	private val apiService by lazy { ApiService(context!!) }
	private val sessionService by lazy { SessionsService(context!!) }
	private lateinit var fusedLocationClient: FusedLocationProviderClient

	private var mFusedLocationClient: FusedLocationProviderClient? = null

		protected var mLastLocation: Location? = null


	@SuppressLint("MissingPermission")
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

		var view = inflater.inflate(R.layout.fragment_fight_search, container, false)
		mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
		getLastLocation()
			sessionService.getLoggedUser()?.let { user ->

				apiService.startFight(user.id, mLastLocation, Timestamp(System.currentTimeMillis())
					, { startActivity(Intent(context!!, FightVictoryFragment::class.java)) },
					{ error -> gerErrorMessage("Failed to start fight", error) })
			}



		return view
	}

	public override fun onStart() {
		super.onStart()

		if (!checkPermissions()) {
			requestPermissions()
		} else {
			getLastLocation()
		}
	}

	private fun checkPermissions(): Boolean {
		val permissionState = ActivityCompat.checkSelfPermission(
			activity!!,
			Manifest.permission.ACCESS_COARSE_LOCATION
		)
		return permissionState == PackageManager.PERMISSION_GRANTED
	}

	@SuppressLint("MissingPermission")
	private fun getLastLocation() {
		mFusedLocationClient!!.lastLocation
			.addOnCompleteListener(activity!!) { task ->
				if (task.isSuccessful && task.result != null) {
					mLastLocation = task.result

				} else {
					Log.w("Error location", "getLastLocation:exception", task.exception)

				}
			}
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
		println("$message - statusCode: $statusCode - data: $data")
	}

}

