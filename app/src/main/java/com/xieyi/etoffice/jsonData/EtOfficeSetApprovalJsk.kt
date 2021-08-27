package com.xieyi.etoffice.jsonData

import android.util.Log
import com.google.gson.Gson
import com.xieyi.etoffice.Config
import com.xieyi.etoffice.EtOfficeApp
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList


//EtOfficeSetApprovalJsk 勤務実績承認
class EtOfficeSetApprovalJsk {

    data class JsonClass(
        val message: String,
        val result: String,
        val status: Int
    )



}
