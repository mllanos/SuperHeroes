package ar.edu.utn.frba.mobile.a2019c1.superheroes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R
import ar.edu.utn.frba.mobile.a2019c1.superheroes.domain.Card
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.linearlayout_teamcard.view.*


class UserTeamAdapter : RecyclerView.Adapter<UserTeamAdapter.ViewHolder>() {

	private var teamCards = emptyList<Card>()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LayoutInflater
		.from(parent.context).inflate(R.layout.linearlayout_teamcard, parent, false)
		.let { view ->
			ViewHolder(view)
		}

	override fun onBindViewHolder(holder: UserTeamAdapter.ViewHolder, position: Int) {
		val card = teamCards[position]
		val imageView = holder.containerView.imageview_teamcard
		Picasso.get()
			.load(card.thumbnail)
			.resize(200, 200)
			.centerCrop()
			.placeholder(R.drawable.ic_launcher_foreground)
			.error(R.drawable.ic_launcher_foreground)
			.into(imageView)
	}

	override fun getItemId(position: Int) = position.toLong()

	override fun getItemCount() = teamCards.size

	override fun getItemViewType(position: Int) = position

	fun addTeamCards(_cards: List<Card>) {
		teamCards = _cards
		notifyDataSetChanged()
	}

	fun setPlaceHolders() {
		val placeHolders = listOf(
			Card(0, "", "", "/", 0),
			Card(0, "", "", "/", 0),
			Card(0, "", "", "/", 0),
			Card(0, "", "", "/", 0)
		)
		teamCards = placeHolders
		notifyDataSetChanged()
	}

	inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer

}
