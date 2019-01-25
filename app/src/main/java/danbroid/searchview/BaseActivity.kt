package danbroid.searchview

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity.*
import java.util.*


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


  fun addButton(title: CharSequence, onClick: () -> Unit): View =
      Button(this).apply {
        text = title

        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)

        setOnClickListener {
          onClick()
        }
      }.also {
        content_container.addView(it)
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
      val userQuery = extras[SearchManager.USER_QUERY]!!.toString()
      if (extras.containsKey(SearchManager.QUERY)) {
        val query = extras[SearchManager.QUERY]!!.toString()
        if (query == userQuery) {
          val msg = "saving user query: $userQuery to recent suggestions"
          log.debug(msg)
          Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
          suggestions.saveRecentQuery(userQuery, "Saved at ${Date()}")
        }
      }
    }

    closeSearchView()
  }

  /**
   * toolbar.collapseActionView() is enough to close any expanded action view
   * while content_container.requestFocus() moves the focus elsewhere so that
   * no parts of the action bar become highlighted when the search view is closed
   *
   */
  protected fun closeSearchView() {
    log.debug("closeSearchView()")
    toolbar.collapseActionView()
    content_container.requestFocus()
  }

  override fun onBackPressed() {
    super.onBackPressed()
    closeSearchView()
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

private val log =
    org.slf4j.LoggerFactory.getLogger(BaseActivity::class.java)

