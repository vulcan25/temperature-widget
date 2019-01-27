package com.example.jonny.widgetworld

import android.app.Application
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import org.json.JSONObject
import java.lang.reflect.Method

import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val service = ServiceVolley()
        val apiController = APIController(service)

        val path = ""
        val params = JSONObject()

        params.put("some", "data")

        apiController.post(path,params) { response ->
            // parse result
            val text = response.toString()

            val toast = Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT)
            toast.show()
        }


    }
}
data class Example private constructor(val name: String) {
    companion object {
        operator fun invoke(name: String): Example {
            //validateName
            return Example(name)
        }
    }
}
class GetData {

    interface WidgetTextCallback {
        fun onTextLoaded(text: String)
    }
    companion object {
        fun widget_text(callback: WidgetTextCallback) {
            val service = ServiceVolley()
            val apiController = APIController(service)

            val path = ""
            val params = JSONObject()

            params.put("some", "data")

            apiController.post(path, params) { response ->
                val widgetText = response?.get(response.names().getString(0)).toString()
                callback.onTextLoaded(widgetText)
            }
        }
    }

}

// All credits for the following stuff to:
// https://www.varvet.com/blog/kotlin-with-volley/


interface ServiceInterface {
    fun post(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit)
}

class BackendVolley : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    private var _requestQueue: RequestQueue? = null
    val requestQueue: RequestQueue?
        get() {
            if (_requestQueue == null) {
                _requestQueue = Volley.newRequestQueue(applicationContext)
            }
            return _requestQueue
        }

    fun <T> addToRequestQueue(request: Request<T>, tag: String) {
        request.tag = if (TextUtils.isEmpty(tag)) TAG else tag
        requestQueue?.add(request)
    }

    fun <T> addToRequestQueue(request: Request<T>) {
        request.tag = TAG
        requestQueue?.add(request)
    }

    fun cancelPendingRequests(tag: Any) {
        if (requestQueue != null) {
            requestQueue!!.cancelAll(tag)
        }
    }

    companion object {
        private val TAG = BackendVolley::class.java.simpleName
        @get:Synchronized var instance: BackendVolley? = null
            private set
    }
}

class APIController constructor(serviceInjection: ServiceInterface): ServiceInterface {
    private val service: ServiceInterface = serviceInjection

    override fun post(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit) {
        service.post(path, params, completionHandler)
    }
}

class ServiceVolley : ServiceInterface {
    val TAG = ServiceVolley::class.java.simpleName
    val basePath = "https://ser.v25.uk/output"

    override fun post(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit) {
        val jsonObjReq = object : JsonObjectRequest(Method.GET, basePath + path, null,
                Response.Listener<JSONObject> { response ->
                    Log.d(TAG, "/post request OK! Response: $response")
                    completionHandler(response)
                },
                Response.ErrorListener { error ->
                    VolleyLog.e(TAG, "/post request fail! Error: ${error.message}")
                    completionHandler(null)
                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Content-Type", "application/json")
                return headers
            }
        }

        BackendVolley.instance?.addToRequestQueue(jsonObjReq, TAG)
    }
}

