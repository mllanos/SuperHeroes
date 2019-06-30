package ar.edu.utn.frba.mobile.a2019c1.superheroes.utils

import android.app.Activity
import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object Permissions {
	val ACCESS_COARSE_LOCATION = 1

	private fun hasPermissions(activity: Activity, permissionCode: String): Boolean {
		return ContextCompat.checkSelfPermission(activity, permissionCode) == PackageManager.PERMISSION_GRANTED
	}

	fun checkForPermissions(activity: Activity, permissionCode: String, reason: String, callback: Callback) {
		if (!hasPermissions(activity, permissionCode)) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionCode)) {
				showStoragePermissionExplanation(
					activity,
					permissionCode,
					reason
				)
			} else {
				dispatchStoragePermissionRequest(
					activity,
					permissionCode
				)
			}
			callback.onRequestSent()
		} else {
			callback.onPermissionAlreadyGranted()
		}
	}

	private fun dispatchStoragePermissionRequest(activity: Activity, permissionCode: String) {
		ActivityCompat.requestPermissions(
			activity, arrayOf(permissionCode),
			ACCESS_COARSE_LOCATION
		)
	}

	private fun showStoragePermissionExplanation(activity: Activity, permissionCode: String, reason: String) {
		val builder = AlertDialog.Builder(activity)
		builder.setTitle("Permission is required")
		builder.setCancelable(true)
		builder.setMessage(reason)
		builder.setPositiveButton("Accept") { dialogInterface, i ->
			dispatchStoragePermissionRequest(
				activity,
				permissionCode
			)
		}
		builder.show()
	}

	interface Callback {
		fun onPermissionAlreadyGranted()
		fun onRequestSent()
	}

}
