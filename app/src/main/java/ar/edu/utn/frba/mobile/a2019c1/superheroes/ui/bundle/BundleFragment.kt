package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.bundle

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.SessionsService
import kotlinx.android.synthetic.main.fragment_bundle.view.*
import java.util.*
import android.graphics.LightingColorFilter
import androidx.work.Data
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.NotificationHelper
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.NotifyWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit


private const val TIMER_LENGTH = 30000L
private const val COUNTDOWN_INTERVAL = 1000L

class BundleFragment : Fragment() {

	private val sessionService by lazy { SessionsService(context!!) }
	private val greenColorFilter = LightingColorFilter(-0x000, 0x0000FF00)
	private val redColorFilter = LightingColorFilter(-0x000, 0x00FF0000)

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_bundle, container, false)
		val button = view!!.btn_get_card
		val now = Date().time
		val currTimer = sessionService.getCurrentTimer()
		val remainingTime = currTimer - now
		val buttonText = context!!.getString(R.string.get_card)

		button.setOnClickListener {
			val newTime = Date().time + TIMER_LENGTH
			sessionService.storeTimer(newTime)
			this.getCards()
			startCountdown(TIMER_LENGTH, button, buttonText)


			//we set a tag to be able to cancel all work of this type if needed
			val workTag = "notificationWork"

			//store DBEventID to pass it to the PendingIntent and open the appropriate event page on notification click
			val inputData = Data.Builder().putInt(workTag, 1).build()
			// we then retrieve it inside the NotifyWorker with:
			// final int DBEventID = getInputData().getInt(DBEventIDTag, ERROR_VALUE);

			val notificationWork = OneTimeWorkRequest.Builder(NotifyWorker::class.java)
				.setInitialDelay(TIMER_LENGTH, TimeUnit.MILLISECONDS)
				.setInputData(inputData)
				.addTag(workTag)
				.build()

			WorkManager.getInstance().enqueue(notificationWork)
		}

		if (remainingTime > 0) {
			button.isEnabled = false
			startCountdown(remainingTime, button, buttonText)
			button.background.colorFilter = redColorFilter
		} else {
			button.background.colorFilter = greenColorFilter
		}

		return view
	}

	private fun startCountdown(length: Long, button: Button, onFinishText: String) {
		button.isEnabled = false
		button.background.colorFilter = redColorFilter
		object : CountDownTimer(length, COUNTDOWN_INTERVAL) {
			override fun onTick(millisUntilFinished: Long) {
				button.text = "${millisUntilFinished / COUNTDOWN_INTERVAL}"
			}

			override fun onFinish() {
				button.isEnabled = true
				button.text = onFinishText
				button.background.colorFilter = greenColorFilter
			}
		}.start()
	}

	private fun getCards() {
		val intent = Intent(context!!, ShowCards::class.java)
		startActivity(intent)
	}

	companion object {
		@JvmStatic
		fun newInstance() = BundleFragment()
	}

}
