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
import android.graphics.LightingColorFilter
import android.widget.ImageButton
import android.widget.TextView

private const val TIMER_LENGTH = 30000L
private const val COUNTDOWN_INTERVAL = 1000L

class BundleFragment : Fragment() {

	private val sessionService by lazy { SessionsService(context!!) }
	private val greenColorFilter = LightingColorFilter(-0x000, 0x0000FF00)
	private val redColorFilter = LightingColorFilter(-0x000, 0x00FF0000)

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_bundle, container, false)
		val button = view!!.btn_get_card
		val text = view!!.txt_bundle
		val now = Date().time
		val currTimer = sessionService.getCurrentTimer()
		val remainingTime = currTimer - now

		button.setOnClickListener {
			sessionService.storeTimer(Date().time + TIMER_LENGTH)
			this.getCards()
			startCountdown(TIMER_LENGTH, button,text, R.string.text_open_bundle)
		}

		if (remainingTime > 0) {
			button.isEnabled = false
			startCountdown(remainingTime, button,text, R.string.text_open_bundle_unavailable)
			button.setImageResource(R.drawable.sobre_bn)
		} else {
			button.setImageResource(R.drawable.sobre)
			text.setText(R.string.text_open_bundle)
		}

		return view
	}

	private fun startCountdown(length: Long, button: ImageButton, text:TextView, onFinishText: Int) {
		button.isEnabled = false
		button.setImageResource(R.drawable.sobre_bn)
		object : CountDownTimer(length, COUNTDOWN_INTERVAL) {
			override fun onTick(millisUntilFinished: Long) {
				text.setText(getResources().getString(R.string.text_open_bundle_unavailable )+"${millisUntilFinished / COUNTDOWN_INTERVAL} s")

			}

			override fun onFinish() {
				button.isEnabled = true
				text.setText(onFinishText)
				button.setImageResource(R.drawable.sobre)
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
