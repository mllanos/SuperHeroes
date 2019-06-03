package ar.edu.utn.frba.mobile.a2019c1.superheroes.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R.*
import ar.edu.utn.frba.mobile.a2019c1.superheroes.domain.Card
import com.squareup.picasso.Picasso

class CardsAdapter(private val cards: List<Card>, private val context: Context) : BaseAdapter() {

	override fun getItem(position: Int) = cards[position]

	override fun getItemId(position: Int) = position.toLong()

	override fun getCount() = cards.size

	override fun getView(position: Int, _convertView: View?, parent: ViewGroup?): View {
		val card = cards[position]
		val convertView = _convertView ?: LayoutInflater
			.from(context)
			.inflate(layout.linearlayout_card, null)
		val imageView = convertView.findViewById<ImageView>(id.imageview_card)
		val nameTextView = convertView.findViewById<TextView>(id.textview_card_name)
		val powerTextView = convertView.findViewById<TextView>(id.textview_card_power)
		Picasso.get()
			.load(card.thumbnail)
			.resize(200, 200)
			.centerCrop()
			.placeholder(drawable.ic_launcher_foreground)
			.error(drawable.ic_launcher_foreground)
			.into(imageView)
		nameTextView.text = card.name
		powerTextView.text = context.getString(R.string.text_card_power, card.power)
		return convertView
	}

}
