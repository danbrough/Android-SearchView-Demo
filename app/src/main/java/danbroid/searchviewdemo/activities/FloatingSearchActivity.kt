package danbroid.searchviewdemo.activities

import android.view.MenuItem
import android.view.View
import danbroid.searchviewdemo.BaseActivity
import danbroid.searchviewdemo.R
import kotlinx.android.synthetic.main.activity.*


class FloatingSearchActivity : BaseActivity() {

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

private val log =
    org.slf4j.LoggerFactory.getLogger(FloatingSearchActivity::class.java)
