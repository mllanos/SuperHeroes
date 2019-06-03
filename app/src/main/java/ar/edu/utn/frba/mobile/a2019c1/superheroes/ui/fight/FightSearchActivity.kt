package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.fight

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.SessionsService
import kotlinx.android.synthetic.main.activity_fight_search.*

class FightSearchActivity : AppCompatActivity(){

	private val sessionService by lazy { SessionsService(this) }


	override fun onResume(){
		super.onResume()
		val openDialog = Dialog(this)
		openDialog.setContentView(R.layout.activity_fight_search)
		val button: Button =  openDialog.findViewById(R.id.btn_go_back)
		openDialog.setCancelable(false)
		button.setOnClickListener{
			this.goBack()
		}
	}

	private fun goBack(){
		super.onBackPressed()
	}

	override fun onCreate(savedInstanceState: Bundle?){
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_fight_search)

		sessionService.getLoggedUser()?.let { user ->
			this.title = this.getString(R.string.welcome, user.nickname)
		}
	}


}
