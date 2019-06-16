package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.cards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_show_single_card.*

class ShowSingleCard : AppCompatActivity() {


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_show_single_card)
		initializeViews()
		buttonClose()


	}


	private fun initializeViews() {
		//val id = intent.getStringExtra("id")
		val name = intent.getStringExtra("name")
		val description = intent.getStringExtra("description")
		val power = intent.getStringExtra("power")
		val thumbnail = intent.getStringExtra("thumbnail")


		findViewById<TextView>(R.id.textView_showsinglecard_name).apply {
			text = name
		}
		findViewById<TextView>(R.id.textView_showsinglecard_description).apply {
			text = description
		}
		findViewById<TextView>(R.id.textView_showsinglecard_power).apply {
			text = "Power: $power"
		}

		val imageViewPhoto = findViewById<ImageView>(R.id.imageview_showsinglecard_photo)

		Picasso.get()
			.load(thumbnail)
			.resize(200, 200)
			.centerCrop()
			.placeholder(R.drawable.ic_superhero)
			.error(R.drawable.ic_superhero_notfound)
			.into(imageViewPhoto)
	}

	private fun buttonClose() {
		button_showsinglecard_back.setOnClickListener{
			finish()
		}
	}
}
