package danbroid.searchview

import android.app.SearchManager
import android.content.Context
import android.provider.SearchRecentSuggestions
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView


private val log by lazy {
  org.slf4j.LoggerFactory.getLogger(SearchViewSupport::class.java)
}

class SearchViewSupport(val activity: AppCompatActivity) {
  lateinit var searchView: SearchView

  private var suggestions: SearchRecentSuggestions = SearchRecentSuggestions(activity, SuggestionProvider.AUTHORITY,
      SuggestionProvider.MODE)

  private val searchManager: SearchManager
    get() = activity.getSystemService(Context.SEARCH_SERVICE) as SearchManager

  private lateinit var searchItem: MenuItem



  fun createSearchView(menu: Menu) {

    searchItem = menu.add(android.R.string.search_go)
    searchItem.setIcon(R.drawable.ic_search)
    searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS or MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW)

    searchView = SearchView(activity.supportActionBar!!.themedContext).apply {
      setSearchableInfo(searchManager.getSearchableInfo(activity.componentName))
      isSubmitButtonEnabled = false
      setIconifiedByDefault(true)
      maxWidth = 1000
    }

    searchItem.actionView = searchView
    menu.add(0, R.id.menu_about, 0, R.string.lbl_about)

    val searchAutoComplete: SearchView.SearchAutoComplete = searchView
        .findViewById(androidx.appcompat.R.id.search_src_text)

    // Collapse the search menu when the user hits the back key
    searchAutoComplete.setOnFocusChangeListener { v, hasFocus ->
      log.trace("onFocusChange(): $hasFocus")
      if (!hasFocus){
        log.debug("lost focus .. closing search view") //TODO
      }
    }

    try {
      // This sets the cursor
      // resource ID to 0 or @null
      // which will make it visible
      // on white background
      val mCursorDrawableRes = TextView::class.java.getDeclaredField("mCursorDrawableRes")
      mCursorDrawableRes.setAccessible(true)
      mCursorDrawableRes.set(searchAutoComplete, 0)

    } catch (e: Exception) {
    }


  }


  fun addAnotherSuggestion() {
    val query = SuggestionProvider.generateRandomSuggestion()
    suggestions.saveRecentQuery(query, "is my kind of nice cheese")
  }

  fun clearSuggestions() = suggestions.clearHistory()
}