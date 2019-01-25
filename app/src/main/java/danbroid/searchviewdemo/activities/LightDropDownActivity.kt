package danbroid.searchviewdemo.activities

import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import danbroid.searchviewdemo.BaseActivity


private val log by lazy {
  org.slf4j.LoggerFactory.getLogger(LightDropDownActivity::class.java)
}

class LightDropDownActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    addButton("Add a suggestion") {

      val suggestion = cheeseData.randomCheese()

      Toast.makeText(this, "Adding a new suggestion: $suggestion", Toast.LENGTH_SHORT).show()

      suggestions.saveRecentQuery(
          suggestion,
          "(LightDropDownActivity)"
      )
    }

    addButton("Clear suggestion history") {
      suggestions.clearHistory()
    }
  }

  override fun configureSearchMenu(menuItem: MenuItem) {

    //Note that we are using the activity as the context rather than the themedContext

    SearchView(this).apply {
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
