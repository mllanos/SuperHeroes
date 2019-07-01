package ar.edu.utn.frba.mobile.a2019c1.superheroes.services

import android.content.Context
import com.android.volley.Response.success
import androidx.annotation.NonNull
import androidx.work.Worker
import androidx.work.WorkerParameters


class NotifyWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

	//needed to send notifications
	private lateinit var helper: NotificationHelper

	override fun doWork(): Result {
		// Method to trigger an instant notification

		//Send a notification
		val notificationId = 1

		helper = NotificationHelper(applicationContext)
		helper.notify(
			notificationId, helper.getNotification1("Next Bundle Available!", "Get your new bundle now."))

		return Result.success()
		// (Returning RETRY tells WorkManager to try this task again
		// later; FAILURE says not to try again.)
	}
}

