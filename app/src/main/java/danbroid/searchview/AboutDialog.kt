package danbroid.searchview

import android.content.pm.PackageManager
import android.text.Html
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

/**
 * Function to display the about dialog
 */

fun MainActivity.showAboutDialog() {
  val builder = AlertDialog.Builder(supportActionBar!!.themedContext,
      R.style.DialogTheme)
  builder.setPositiveButton(android.R.string.ok, null)
  var title = getString(R.string.app_name) + " version: "


  try {
    val packageInfo = packageManager.getPackageInfo(packageName, 0)
    title += " " + packageInfo.versionName
  } catch (e: PackageManager.NameNotFoundException) {
    //Handle exception
  }

  builder.setTitle(title)

  builder.setIcon(R.drawable.ic_launcher)
  val aboutMessage = SpannableString(Html.fromHtml(getString(R.string
      .msg_about)))
  builder.setMessage(aboutMessage)

  val messageText = builder.show().findViewById<TextView>(android.R.id.message)
  messageText!!.movementMethod = LinkMovementMethod.getInstance()
}