package danbroid.searchview.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import danbroid.searchview.BaseActivity


class MainActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    addButton("Light Suggestion DropDown") {
      startActivity(Intent(this, LightDropDownActivity::class.java))
    }

    addButton("Dark Suggestion DropDown") {
      startActivity(Intent(this, DarkDropDownActivity::class.java))
    }

    addButton("Custom Suggestion DropDown") {
      startActivity(Intent(this, CustomSuggestionLayoutActivity::class.java))
    }

    addButton("Search Dialog") {
      startActivity(Intent(this, SearchDialogActivity::class.java))
    }

    /*
    addButton("Floating Search") {
      startActivity(Intent(this, FloatingSearchActivity::class.java))
    }*/
  }

  override fun configureSearchMenu(menuItem: MenuItem) {
    menuItem.isVisible = false
  }
}

