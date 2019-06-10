package ar.edu.utn.frba.mobile.a2019c1.superheroes.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R.*
import ar.edu.utn.frba.mobile.a2019c1.superheroes.domain.Card
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.linearlayout_card.view.*

class CardsAdapter(private val cards: List<Card>, private val context: Context) : RecyclerView.Adapter<CardsAdapter.ViewHolder>() {
	override fun onBindViewHolder(holder: CardsAdapter.ViewHolder, position: Int) {
		val card = cards[position]
		holder.itemView.textview_card_name.text = card.name
		holder.itemView.textview_card_power.text = card.power.toString()

		Picasso.get()
			.load(card.thumbnail)
			.resize(200, 200)
			.centerCrop()
			.placeholder(drawable.ic_launcher_foreground)
			.error(drawable.ic_launcher_foreground)
			.into(holder.itemView.imageview_card)
	}

	fun getItem(position: Int) = cards[position]


	override fun getItemId(position: Int) = position.toLong()

	override fun getItemCount() = cards.size

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(parent.context)
			.inflate(R.layout.linearlayout_card, parent, false)
		return ViewHolder(view)
	}


	inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer
	}



