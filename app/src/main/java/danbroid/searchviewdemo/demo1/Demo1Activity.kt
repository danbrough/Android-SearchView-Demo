package danbroid.searchviewdemo.demo1

import android.app.SearchManager
import android.content.Intent
import android.database.Cursor
import android.database.MatrixCursor
import android.os.AsyncTask
import android.os.Bundle
import android.provider.BaseColumns
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.SimpleCursorAdapter
import danbroid.searchviewdemo.BaseActivity
import danbroid.searchviewdemo.R


private val log by lazy {
  org.slf4j.LoggerFactory.getLogger(Demo1Activity::class.java)
}

class Demo1Activity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    addNote(
        "Demo1 features a search-view with a custom suggestion layout.\n"+
            "Type in a couple of characters to initiate a search"
    )
  }

  override fun configureSearchMenu(menuItem: MenuItem) {

    val searchView = SearchView(themedContext).apply {
      setIconifiedByDefault(true)
    }

    menuItem.actionView = searchView

    searchView.suggestionsAdapter = SimpleCursorAdapter(
        themedContext,
        R.layout.search_dropdown,
        null,
        arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1, SearchManager.SUGGEST_COLUMN_TEXT_2),
        intArrayOf(android.R.id.text1, android.R.id.text2),
        0
    )

    searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
      override fun onSuggestionSelect(position: Int): Boolean {
        return true
      }

      override fun onSuggestionClick(position: Int): Boolean {
        log.debug("onSuggestionClick() position:$position")

        val info = with(searchView.suggestionsAdapter.cursor) {
          moveToPosition(position)
          "TITLE: ${getString(1)} SUBTITLE: ${getString(2)}"
        }

        //Do something with the selected cheese
        Toast.makeText(this@Demo1Activity, info, Toast.LENGTH_SHORT).show()
        closeSearchView()
        return true
      }
    })

    searchView.setOnQueryTextListener(
        object : SearchView.OnQueryTextListener {

          var search: SearchTask? = null

          override fun onQueryTextSubmit(query: String?): Boolean {
            log.error("onQueryTextSubmit() $query")
            return true
          }

          override fun onQueryTextChange(query: String): Boolean {
            log.trace("onQueryTextChange() $query")
            if (query.length < 3) return true

            search?.cancel(true)

            search = SearchTask {
              searchView.suggestionsAdapter.swapCursor(it)
            }

            return true
          }
        })
  }

}

class SearchTask(val resultsReceiver: (Cursor) -> (Unit)) : AsyncTask<Void, Unit, Cursor>() {

  init {
    execute()
  }

  override fun doInBackground(vararg params: Void?): Cursor {
    log.trace("performing a background search ...")
    Thread.sleep(500)
    val cursor = MatrixCursor(arrayOf(
        BaseColumns._ID,
        SearchManager.SUGGEST_COLUMN_TEXT_1,
        SearchManager.SUGGEST_COLUMN_TEXT_2,
        SearchManager.SUGGEST_COLUMN_INTENT_ACTION
    ))
    for (n in 1..20)
      cursor.addRow(arrayOf(n, "Title: $n", "Subtitle for $n", Intent.ACTION_SEARCH))
    return cursor
  }

  override fun onPostExecute(result: Cursor) {
    log.trace("finished search")
    resultsReceiver(result)
  }
}



