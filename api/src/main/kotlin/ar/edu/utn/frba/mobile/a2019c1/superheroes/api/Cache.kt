package ar.edu.utn.frba.mobile.a2019c1.superheroes.api

import com.google.gson.Gson
import net.spy.memcached.MemcachedClient
import org.springframework.stereotype.Service

@Service
class StorageService(private val memcachedClient: MemcachedClient, private val gson: Gson) {

    fun storeUser(user: User) {
        val key = "users:${user.id}"
        val json = gson.toJson(user)
        memcachedClient.set(key, 0, json)
    }

}
