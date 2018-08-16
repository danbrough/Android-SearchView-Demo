package danbroid.searchviewdemo

import android.app.SearchManager
import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.provider.BaseColumns
import android.text.TextUtils
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

class CheeseData(context: Context) {

  val cheeses: Array<String> by lazy {
    val cheeseList = mutableListOf<String>()
    val reader = BufferedReader(InputStreamReader(context.assets.open("cheeselist.txt")))
    try {
      reader.forEachLine {
        if (!TextUtils.isEmpty(it))
          cheeseList.add(it)
      }
    } finally {
      reader.close()
    }
    cheeseList.toTypedArray();
  }

  /**
  new String[]{BaseColumns._ID, "substr(" + Tables.BusStop.STOP_CODE + ",4) AS " +
  SearchManager.SUGGEST_COLUMN_TEXT_2,
  Tables.BusStop.STOP_NAME + " AS " + SearchManager.SUGGEST_COLUMN_TEXT_1,
  "'" + ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BusApp.class.getPackage().getName
  () + "/"
  + danbroid.busapp.R.mipmap.ic_launcher + "' AS " + SearchManager.SUGGEST_COLUMN_ICON_1,
  Tables.BusStop._ID + " AS " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID
   */
  fun newCheeseCursor(): Cursor =
      MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_2, SearchManager.SUGGEST_COLUMN_TEXT_1)).apply {
        var n = 0
        for (cheese in cheeses)
          addRow(arrayOf(n++, "$cheese is my favourite", cheese))
      }

  private val RANDOM = Random(System.currentTimeMillis())

  fun randomCheese(): String = cheeses[RANDOM.nextInt(cheeses.size)]

  companion object {
    @Volatile
    private var instance: CheeseData? = null

    fun getInstance(context: Context) = instance ?: synchronized(this) {
      instance ?: CheeseData(context).also {
        instance = it
      }
    }
  }

/*
  class SeachResultsAdapter(val ctx1: Context, val ctx2: Context = ctx1) : CursorAdapter(ctx1, null, false) {


    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View =
        LayoutInflater.from(ctx2).inflate(
            //android.R.layout.simple_list_item_2,
            R.layout.search_dropdown,
            parent,
            false
        )

    override fun bindView(view: View, context: Context, cursor: Cursor) {
      Log.i("TEST", "text1 = ${cursor.getString(1)} text2 = ${cursor.getString(2)}")
      view.findViewById<TextView>(android.R.id.text2).text = cursor.getString(1)
      view.findViewById<TextView>(android.R.id.text1).text = cursor.getString(2)
    }

  }*/


}



