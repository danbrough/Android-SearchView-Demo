package danbroid.searchviewdemo.activities

import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import danbroid.searchviewdemo.BaseActivity


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
          "(DarkDropDownActivity)"
      )
    }

    addButton("Clear suggestion history") {
      suggestions.clearHistory()
    }
  }

  override fun configureSearchMenu(menuItem: MenuItem) {

    //Creating the search view with the themedContext to create a dark search view

    SearchView(themedContext).apply {
      setIconifiedByDefault(true)
      setSearchableInfo(searchManager.getSearchableInfo(componentName))
      isSubmitButtonEnabled = true

      setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
          log.trace("onQueryTextSubmit() $query")
          return false
        }

        override fun onQueryTextChange(newText: String): Boolean {
          isSubmitButtonEnabled = newText.length > 2
          return false
        }

      })
    }.also {
      menuItem.actionView = it
    }

  }

}

private val log =
    org.slf4j.LoggerFactory.getLogger(DarkDropDownActivity::class.java)
