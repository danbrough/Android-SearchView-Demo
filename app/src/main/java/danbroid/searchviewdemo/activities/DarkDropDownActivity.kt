package danbroid.searchviewdemo.activities

import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import danbroid.searchviewdemo.BaseActivity

private val log by lazy {
  org.slf4j.LoggerFactory.getLogger(DarkDropDownActivity::class.java)
}

class DarkDropDownActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)


    addNote(
        "This activity uses the CheeseRecentSuggestions provider to provide search suggestions.\n" +
            "You can add new suggestions to its database using the button below."
    )

    addButton("Add a suggestion") {

      val suggestion = cheeseData.randomCheese()

      Toast.makeText(this, "Adding a new suggestion: $suggestion", Toast.LENGTH_SHORT).show()

      suggestions.saveRecentQuery(
          suggestion,
          "Love this one (DarkDropDownActivity)"
      )
    }

    addButton("Clear suggestion history") {
      suggestions.clearHistory()
    }
  }

  override fun configureSearchMenu(menuItem: MenuItem) {

    val searchView = object : SearchView(themedContext) {

      override fun dispatchKeyEventPreIme(event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_BACK &&
            event.action == KeyEvent.ACTION_UP) {
          log.trace("triggering action view collapse..")
          onActionViewCollapsed()
          clearFocus()
        }
        return super.dispatchKeyEventPreIme(event)
      }

    }.apply {
      setIconifiedByDefault(true)
      setSearchableInfo(searchManager.getSearchableInfo(componentName))
    }

    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String): Boolean {
        log.trace("onQueryTextSubmit() $query")
        return false
      }

      override fun onQueryTextChange(query: String): Boolean {
        log.trace("onQueryTextChange() $query")
        return false
      }
    })

    menuItem.actionView = searchView
  }


}
