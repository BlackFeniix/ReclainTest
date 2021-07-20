package com.hito.nikolay.reclaintest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.onesignal.OSNotificationOpenedResult
import com.onesignal.OneSignal
import org.json.JSONObject


private const val ONESIGNAL_APP_ID = "33181dc1-398b-4eba-98d5-901e96c8e62e"
private var appOpenFromNotification = false

class MainLoadingActivity : AppCompatActivity() {

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        appOpenFromNotification = savedInstanceState["appOpenFromNotification"] as Boolean
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putBoolean("appOpenFromNotification", appOpenFromNotification)
    }

    override fun onResume() {
        super.onResume()

        if (appOpenFromNotification)
            startActivity(Intent(this, WebViewActivity::class.java))
        else
            startActivity(Intent(this, GameActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = ProgressBar.VISIBLE

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        // OneSignal Initialization
        OneSignal.setAppId(ONESIGNAL_APP_ID)
        OneSignal.setNotificationOpenedHandler(NewNotificationHandler(this))
        OneSignal.initWithContext(this)

    }
}

class NewNotificationHandler(private val context: Context) : OneSignal.OSNotificationOpenedHandler {

    override fun notificationOpened(result: OSNotificationOpenedResult?) {
        val data: JSONObject? = result?.notification?.additionalData
        if (data!=null)
        {
            val activityData = data.optString("activity_data")
            if (activityData == "webView")
            {
                val intent = Intent(context, WebViewActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(context, intent, null)
                appOpenFromNotification = true
            }
        }
    }
}
