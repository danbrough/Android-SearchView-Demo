package danbroid.searchviewdemo

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import danbroid.searchviewdemo.demo1.Demo1Activity
import danbroid.searchviewdemo.demo2.Demo2Activity
import danbroid.searchviewdemo.demo3.Demo3Activity
import danbroid.searchviewdemo.demo4.Demo4Activity


class MainActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    addButton("Dark Suggestion DropDown") {
      startActivity(Intent(this, Demo2Activity::class.java))
    }

    addButton("Light Suggestion DropDown") {
      startActivity(Intent(this, Demo3Activity::class.java))
    }

    addButton("Custom Suggestion DropDown") {
      startActivity(Intent(this, Demo1Activity::class.java))
    }

    addButton("Demo4") {
      startActivity(Intent(this, Demo4Activity::class.java))
    }
  }

  override fun configureSearchMenu(menuItem: MenuItem) {
    menuItem.isVisible = false
  }
}

