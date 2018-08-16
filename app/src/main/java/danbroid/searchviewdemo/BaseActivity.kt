package danbroid.searchviewdemo

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity.*
import java.util.*

private val log by lazy {
  org.slf4j.LoggerFactory.getLogger(BaseActivity::class.java)
}

abstract class BaseActivity : AppCompatActivity() {

  private var menu: Menu? = null

  protected val cheeseData: CheeseData by lazy {
    CheeseData.getInstance(this)
  }

  protected val themedContext: Context
    get() = supportActionBar!!.themedContext


  protected val suggestions: SearchRecentSuggestions by lazy {
    SearchRecentSuggestions(
        this,
        CheeseSuggestionsProvider.AUTHORITY,
        CheeseSuggestionsProvider.MODE
    )
  }

  protected val searchManager: SearchManager by lazy {
    getSystemService(Context.SEARCH_SERVICE) as SearchManager
  }


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity)
    setSupportActionBar(toolbar)
    supportActionBar!!.setDisplayHomeAsUpEnabled(!isTaskRoot)
  }


  fun addButton(title: String, onClick: () -> Unit) {
    content_container.addView(
        Button(this).apply {

          text = title

          layoutParams = ViewGroup.LayoutParams(
              ViewGroup.LayoutParams.WRAP_CONTENT,
              ViewGroup.LayoutParams.WRAP_CONTENT)

          setOnClickListener {
            onClick()
          }
        }
    )
  }

  fun addNote(text: String) {
    content_container.addView(
        TextView(this).apply {
          this.text = text

          layoutParams = ViewGroup.LayoutParams(
              ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.WRAP_CONTENT
          )
        }
    )
  }


  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)

    val extras = intent.extras ?: return

    StringBuilder("onNewIntent():\n").apply {
      for (key in extras.keySet()) {
        append("${key} => ${extras[key]}\n")
      }
    }.toString().also {
      log.debug(it)
      Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
    }

    if (extras.containsKey(SearchManager.USER_QUERY)) {
      log.debug("saving user query to recent suggestions")
      suggestions.saveRecentQuery(extras[SearchManager.USER_QUERY]!!.toString(), "Saved at ${Date()}")
    }

    closeSearchView()
  }

  protected fun closeSearchView() {
    menu?.findItem(R.id.action_search)?.let {
      if (it.isActionViewExpanded) {
        it.collapseActionView()
        it.actionView.clearFocus()
      }
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.menu, menu)
    this.menu = menu
    menu.findItem(R.id.action_search)?.let {
      configureSearchMenu(it)
    }
    return true
  }

  protected abstract fun configureSearchMenu(menuItem: MenuItem)

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    return when (item.itemId) {
      R.id.action_about -> onAbout()
      android.R.id.home -> {
        onBackPressed(); true
      }

      else -> super.onOptionsItemSelected(item)
    }
  }

  protected fun onAbout(): Boolean {
    Snackbar.make(content_container, "On About", Snackbar.LENGTH_SHORT).show()
    return true
  }
}
