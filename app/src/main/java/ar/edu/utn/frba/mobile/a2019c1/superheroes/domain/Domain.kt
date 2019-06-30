package ar.edu.utn.frba.mobile.a2019c1.superheroes.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class User(val id: Int, val nickname: String)

data class Card(val id: Int, val name: String, val description: String, val thumbnail: String, val power: Int)

data class Team(val id: Int, val superheroes: List<Card>, val totalPower: Int)

@Parcelize
data class FightResult(val id: Int, val winner: String, val opponent: Opponent) : Parcelable

@Parcelize
data class Opponent(val id :Int, val nickname: String, val team_id: Int) : Parcelable
