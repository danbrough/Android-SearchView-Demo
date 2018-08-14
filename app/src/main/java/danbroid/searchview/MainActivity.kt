package danbroid.searchview

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.layout.*


private val log by lazy {
  org.slf4j.LoggerFactory.getLogger(MainActivity::class.java)
}

class MainActivity : AppCompatActivity() {

  private lateinit var searchItem: MenuItem

  private val searchViewSupport = SearchViewSupport(this)


  override fun onCreate(state: Bundle?) {
    log.info("onCreate() intent:{}", intent)
    super.onCreate(state)

    setContentView(R.layout.layout)
    setSupportActionBar(toolbar)


    add_suggestion_button.setOnClickListener { searchViewSupport.addAnotherSuggestion() }

    clear_button.setOnClickListener { searchViewSupport.clearSuggestions() }


    submit_enabled_checkbox.setOnCheckedChangeListener { _,
                                                         isChecked ->
      searchViewSupport.searchView.isSubmitButtonEnabled = isChecked
    }

  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    searchViewSupport.createSearchView(menu)

    return super.onCreateOptionsMenu(menu)
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)

    log.info("onNewIntent() :{}", intent)
    invalidateOptionsMenu()

    intent.extras?.let {
      /**
       * USER_QUERY will be set when the user uses the submit button to submit the query
       */
      val msg = "SearchManager.USER_QUERY =  ${it[SearchManager.USER_QUERY]}," +
          "SearchManager.QUERY = ${it[SearchManager.QUERY]}"

      Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
      log.info(msg)
    }

  }


  /**
   * Called when the hardware search button is pressed
   */
  override fun onSearchRequested(): Boolean {
    log.trace("onSearchRequested();")


    // dont show the built-in search dialog
    return false
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.menu_about -> {
        showAboutDialog()
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }
}


