package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.fight

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R
import ar.edu.utn.frba.mobile.a2019c1.superheroes.domain.Geolocation
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.SessionsService
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.VolleySingleton
import ar.edu.utn.frba.mobile.a2019c1.superheroes.utils.Permissions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.lang.System.currentTimeMillis

class FightFragment : Fragment(), ShakeEventManager.ShakeListener {

	private val shakeEventManager = ShakeEventManager()
	private val handler = Handler()
	private var shakeEnabled = false
	private var locationPermissionGranted = false
	private lateinit var fusedLocationClient: FusedLocationProviderClient
	private val sessionService by lazy { SessionsService(context!!) }

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_fight, container, false)
		fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)
		checkForLocationPermissions()
		return view
	}

	override fun onResume() {
		super.onResume()
		shakeEventManager.setListener(this)
		shakeEventManager.init(context!!)
		enableShake()
		VolleySingleton.getInstance(context!!).cancelAll()
	}

	override fun onStop() {
		super.onStop()
		shakeEventManager.setListener(null)
	}

	override fun onShake() {
		if (shakeEnabled) {
			shakeEnabled = false

			if (sessionService.getLoggedUserTeamId() === null) {
				enableShake()
				return Toast.makeText(context!!, "You need to select a team first.", Toast.LENGTH_LONG).show()
			}

			if (locationPermissionGranted) {
				searchFight()
			} else {
				checkForLocationPermissions()
			}

		}
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
		when (requestCode) {
			Permissions.ACCESS_COARSE_LOCATION -> {
				if (grantResults.count() > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					locationPermissionGranted = true
					searchFight()
				} else {
					locationPermissionGranted = false
				}
				return
			}
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
	}

	@SuppressLint("MissingPermission")
	private fun searchFight() {
		fusedLocationClient.lastLocation
			.addOnSuccessListener { location: Location? ->
				val geolocation = Geolocation(location?.latitude, location?.altitude, currentTimeMillis())
				val intent = Intent(context!!, FightSearchActivity::class.java)
				intent.putExtra("geolocation", geolocation)
				startActivity(intent)
			}
	}

	private fun checkForLocationPermissions() {
		Permissions.checkForPermissions(
			this,
			Manifest.permission.ACCESS_COARSE_LOCATION,
			"Application needs access to location",
			object : Permissions.Callback {
				override fun onPermissionAlreadyGranted() {
					locationPermissionGranted = true
				}
			})
	}

	private fun enableShake() {
		handler.postDelayed({
			shakeEnabled = true
		}, 1500)
	}

	companion object {
		@JvmStatic
		fun newInstance() = FightFragment()
	}

}
