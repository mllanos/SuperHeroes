package ar.edu.utn.frba.mobile.a2019c1.superheroes.utils

import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object Permissions {

	const val ACCESS_COARSE_LOCATION = 1

	fun checkForPermissions(fragment: Fragment, permission: String, reason: String, callback: Callback) {
		if (!hasPermissions(fragment, permission)) {
			if (fragment.shouldShowRequestPermissionRationale(permission)) {
				showStoragePermissionExplanation(fragment, permission, reason)
			} else {
				dispatchStoragePermissionRequest(fragment, permission)
			}
		} else {
			callback.onPermissionAlreadyGranted()
		}
	}

	private fun hasPermissions(fragment: Fragment, permissionCode: String) =
		ContextCompat.checkSelfPermission(fragment.context!!, permissionCode) == PackageManager.PERMISSION_GRANTED

	private fun dispatchStoragePermissionRequest(fragment: Fragment, permission: String) =
		fragment.requestPermissions(arrayOf(permission), mapPermissionToCode(permission))

	private fun showStoragePermissionExplanation(fragment: Fragment, permissionCode: String, reason: String) {
		val builder = AlertDialog.Builder(fragment.context!!)
		builder.setTitle("Permission is required")
		builder.setCancelable(true)
		builder.setMessage(reason)
		builder.setPositiveButton("Accept") { _, _ ->
			dispatchStoragePermissionRequest(fragment, permissionCode)
		}
		builder.show()
	}

	private fun mapPermissionToCode(permission: String) =
		mapOf(android.Manifest.permission.ACCESS_COARSE_LOCATION to ACCESS_COARSE_LOCATION).getValue(permission)

	interface Callback {
		fun onPermissionAlreadyGranted()
	}

}
