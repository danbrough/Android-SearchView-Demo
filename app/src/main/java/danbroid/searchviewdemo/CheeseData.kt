package danbroid.searchviewdemo

import android.content.Context
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


}



