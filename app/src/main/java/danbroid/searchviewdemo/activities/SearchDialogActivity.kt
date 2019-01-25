package danbroid.searchviewdemo.activities

import android.os.Bundle
import android.view.MenuItem
import danbroid.searchviewdemo.BaseActivity
import danbroid.searchviewdemo.R


class SearchDialogActivity : BaseActivity() {

  override fun configureSearchMenu(menuItem: MenuItem) {
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean =
      when (item.itemId) {
        R.id.action_search -> {
          onSearchRequested()
          true
        }
        else -> super.onOptionsItemSelected(item)
      }

  override fun startSearch(initialQuery: String?, selectInitialQuery: Boolean, appSearchData: Bundle?, globalSearch: Boolean) {
    log.trace("startSearch() initalQuery: $initialQuery selectInitialQuery: $selectInitialQuery appSearchData: $appSearchData globalSearch: $globalSearch")
    super.startSearch(initialQuery, selectInitialQuery, appSearchData, globalSearch)
  }

}

private val log =
    org.slf4j.LoggerFactory.getLogger(SearchDialogActivity::class.java)