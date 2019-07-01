package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.bundle

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.SessionsService
import kotlinx.android.synthetic.main.fragment_bundle.view.*
import java.util.*
import androidx.work.Data
import android.widget.ImageButton
import android.widget.TextView
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.NotifyWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit


private const val TIMER_LENGTH = 30000L
private const val COUNTDOWN_INTERVAL = 1000L

class BundleFragment : Fragment() {

	private val sessionService by lazy { SessionsService(context!!) }
	private var countdownTimer: CountDownTimer? = null

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_bundle, container, false)
		val button = view!!.btn_get_card
		val text = view!!.txt_bundle
		val now = Date().time
		val currTimer = sessionService.getCurrentTimer()
		val remainingTime = currTimer - now
		val buttonText = view.txt_bundle//context!!.getString(R.string.get_card)
		val tickText = resources.getString(R.string.text_open_bundle_unavailable)
		val finishText =  resources.getString(R.string.text_open_bundle)

		button.setOnClickListener {
			val newTime = Date().time + TIMER_LENGTH
			sessionService.storeTimer(newTime)
			this.getCards()
			startCountdown(TIMER_LENGTH, button, buttonText, tickText, finishText)


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
			startCountdown(remainingTime, button, text, tickText, finishText)
			button.setImageResource(R.drawable.sobre_bn)
		} else {
			button.setImageResource(R.drawable.sobre)
			text.text = finishText
		}

		return view
	}

	override fun onDestroy() {
		super.onDestroy()
		countdownTimer?.cancel()
	}

	private fun startCountdown(length: Long, imageButton: ImageButton, textView:TextView, tickText: String, finishText: String) {
		imageButton.isEnabled = false
		imageButton.setImageResource(R.drawable.sobre_bn)
		countdownTimer = object : CountDownTimer(length, COUNTDOWN_INTERVAL) {
			override fun onTick(millisUntilFinished: Long) {
				textView.text =  "$tickText ${millisUntilFinished / COUNTDOWN_INTERVAL} s"
			}

			override fun onFinish() {
				imageButton.isEnabled = true
				textView.text = finishText
				imageButton.setImageResource(R.drawable.sobre)
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
