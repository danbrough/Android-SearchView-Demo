package danbroid.searchviewdemo.demo3

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import danbroid.searchviewdemo.BaseActivity


private val log by lazy {
  org.slf4j.LoggerFactory.getLogger(Demo3Activity::class.java)
}

class Demo3Activity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    addButton("Add a suggestion") {

      val suggestion = cheeseData.randomCheese()

      Toast.makeText(this, "Adding a new suggestion: $suggestion", Toast.LENGTH_SHORT).show()

      suggestions.saveRecentQuery(
          suggestion,
          "Love this one (demo3)"
      )
    }

    addButton("Clear suggestion history") {
      suggestions.clearHistory()
    }
  }

  override fun configureSearchMenu(menuItem: MenuItem) {

    //Note that we are using the activity as the context rather than the themedContext
    val searchView = SearchView(this).apply {
      setIconifiedByDefault(true)
      setSearchableInfo(searchManager.getSearchableInfo(componentName))
      isSubmitButtonEnabled = true
    }

    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {
        return false
      }

      override fun onQueryTextChange(newText: String): Boolean {
        searchView.isSubmitButtonEnabled = newText.length > 2
        return false
      }

    })


    //val autoComplete: SearchView.SearchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text)


    menuItem.actionView = searchView
  }


}
