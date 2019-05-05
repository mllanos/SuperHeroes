package ar.edu.utn.frba.mobile.a2019c1.superheroes.api

data class User(val id: Int, val nickname: String)

data class Card(val id: Int, val name: String, val description: String, val thumbnail: String, val power: Int)

data class Team(val id: Int, val superheroes: List<Int>)
