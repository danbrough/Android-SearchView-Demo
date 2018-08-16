package danbroid.searchviewdemo.demo1

import android.app.SearchManager
import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.os.AsyncTask
import android.os.Bundle
import android.provider.BaseColumns
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.CursorAdapter
import danbroid.searchviewdemo.BaseActivity
import danbroid.searchviewdemo.R


private val log by lazy {
  org.slf4j.LoggerFactory.getLogger(Demo1Activity::class.java)
}

class Demo1Activity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    addNote(
        "Demo1 features a search-view with a custom suggestion layout.\n" +
            "Type in a couple of characters to initiate a search"
    )
  }

  override fun configureSearchMenu(menuItem: MenuItem) {

    val searchView = SearchView(this).apply {
      setIconifiedByDefault(true)
    }

    menuItem.actionView = searchView

    searchView.suggestionsAdapter = CustomSuggestionsAdapter(this)

    searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
      override fun onSuggestionSelect(position: Int): Boolean {
        return false
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


        //return true to cancel the default behaviour which will submit an Intent.ACTION_SEARCH
        //at the activity
        return true
      }
    })

    searchView.setOnQueryTextListener(
        object : SearchView.OnQueryTextListener {

          var search: SearchTask? = null

          override fun onQueryTextSubmit(query: String?): Boolean {
            log.trace("onQueryTextSubmit() $query")
            return false
          }

          override fun onQueryTextChange(query: String): Boolean {
            log.trace("onQueryTextChange() $query")
            if (query.length < 3) return true

            search?.cancel(true)

            search = SearchTask {
              searchView.suggestionsAdapter.swapCursor(it)
            }

            return false
          }
        })

    //hack to close the search menu when the back button is pressed
    searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
      if (!hasFocus) {
        log.error("invalidating options menu")
        invalidateOptionsMenu()
      }
    }
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
        SearchManager.SUGGEST_COLUMN_TEXT_2
    ))
    for (n in 1..20)
      cursor.addRow(arrayOf(
          n,
          "Title: $n",
          "Subtitle for $n"
      ))
    return cursor
  }

  override fun onPostExecute(result: Cursor) {
    log.trace("finished search")
    resultsReceiver(result)
  }
}

class CustomSuggestionsAdapter(context: Context) : CursorAdapter(context, null, 0) {

  val inflater by lazy {
    LayoutInflater.from(context)
  }

  override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View =
      inflater.inflate(R.layout.search_dropdown, parent, false)


  override fun bindView(view: View, context: Context, cursor: Cursor) {
    val title = cursor.getString(1)
    val subTitle = cursor.getString(2)
    val text1 = view.findViewById<TextView>(android.R.id.text1)
    val text2 = view.findViewById<TextView>(android.R.id.text2)

    text1.text = title
    text2.text = subTitle
    text2.visibility = if (TextUtils.isEmpty(subTitle)) View.GONE else View.VISIBLE

    val icon = view.findViewById<ImageView>(android.R.id.icon1)
    icon.setImageResource(R.drawable.ic_sentiment_satisfied)
    icon.visibility = View.VISIBLE
  }

}

