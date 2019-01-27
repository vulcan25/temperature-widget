package com.example.jonny.widgetworld

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import org.json.JSONObject
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import java.text.DateFormat
import java.util.*
import android.provider.SyncStateContract.Helpers.update
import kotlin.text.Typography.quote


/**
 * Implementation of App Widget functionality.
 */
class Statusr : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {

        GetData.widget_text(object: GetData.WidgetTextCallback{
            override fun onTextLoaded(widgetText: String) {
                // There may be multiple widgets active, so update all of them
                for (appWidgetId in appWidgetIds) {
                    updateAppWidget(context, widgetText, appWidgetManager, appWidgetId)
                }
            }
        })

    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    fun handle_the_response(){}

    companion object {

        internal fun updateAppWidget(context: Context, widgetText: String,  appWidgetManager: AppWidgetManager,
                                     appWidgetId: Int) {

            //Retrieve the time
            val timeString = DateFormat.getTimeInstance(DateFormat.SHORT).format(Date())

            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.statusr)

            views.setTextViewText(R.id.appwidget_text, widgetText)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)

            views.setTextViewText(R.id.last_update, timeString)

            //Create an Intent with the AppWidgetManager.ACTION_APPWIDGET_UPDATE action//

            val intentUpdate = Intent(context, Statusr::class.java)
            intentUpdate.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE

            //Update the current widget instance only, by creating an array that contains the widget’s unique ID//

            val idArray = intArrayOf(appWidgetId)
            intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray)

            //Wrap the intent as a PendingIntent, using PendingIntent.getBroadcast()//

            val pendingUpdate = PendingIntent.getBroadcast(
                    context, appWidgetId, intentUpdate,
                    PendingIntent.FLAG_UPDATE_CURRENT)
            views.setOnClickPendingIntent(R.id.appwidget_text, pendingUpdate)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)

            }
        }
    }
