package danbroid.searchviewdemo.demo5

import android.view.MenuItem
import android.view.View
import danbroid.searchviewdemo.BaseActivity
import danbroid.searchviewdemo.R
import kotlinx.android.synthetic.main.activity.*


private val log by lazy {
  org.slf4j.LoggerFactory.getLogger(Demo5Activity::class.java)
}

class Demo5Activity : BaseActivity() {

  override fun configureSearchMenu(menuItem: MenuItem) {
  }

  override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
    R.id.action_search -> {
      floating_search_view.visibility = View.VISIBLE
      toolbar.visibility = View.GONE
      true
    }
    else -> super.onOptionsItemSelected(item)
  }


}