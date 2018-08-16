package danbroid.searchviewdemo

import android.content.SearchRecentSuggestionsProvider

class CheeseSuggestionsProvider : SearchRecentSuggestionsProvider() {

  companion object {
    val AUTHORITY = CheeseSuggestionsProvider::class.java.name
    val MODE = SearchRecentSuggestionsProvider.DATABASE_MODE_2LINES or SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES
  }

  init {
    setupSuggestions(AUTHORITY, MODE)
  }
}