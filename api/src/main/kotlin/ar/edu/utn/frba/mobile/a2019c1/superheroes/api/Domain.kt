package ar.edu.utn.frba.mobile.a2019c1.superheroes.api

class User(val id: Int, val nickname: String)

data class Card(val id: Int, val name: String, val description: String, val thumbnail: String, val power: Int)