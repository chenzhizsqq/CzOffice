package com.xieyi.etoffice

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Tools {
    private const val TAG = "Tools"


    fun dateChange(_data: String?, _insert: String?): String {
        var rStr = _data
        val sb = StringBuilder(rStr)
        sb.insert(6, _insert)
        sb.insert(4, _insert)
        rStr = sb.toString()
        return rStr
    }

    fun srcContent(src: String, maxLength: Int,lastStr:String): String {
        var rString = src
        if (rString.length > maxLength) {
            rString = src.substring(0, maxLength)
            rString += lastStr
        }
        return rString
    }

    fun dateGetYear(_data: String): String {
        if (_data.isEmpty()) return "0000"
        return _data.substring(0, 4)
    }

    fun dateGetMonth(_data: String): String {
        if (_data.isEmpty()) return "00"
        return _data.substring(4, 6)
    }

    fun dateGetDay(_data: String): String {
        if (_data.isEmpty()) return "00"
        return _data.substring(6, 8)
    }

    fun dateGetHH(_data: String): String {
        if (_data.isEmpty()) return "00"
        return _data.substring(8, 10)
    }

    fun dateGetMM(_data: String): String {
        if (_data.isEmpty()) return "00"
        return _data.substring(10, 12)
    }

    fun dateGetSS(_data: String): String {
        if (_data.isEmpty()) return "00"
        return _data.substring(12, 14)
    }

    //2021.07.15 16:56:56
    fun allDateTime(_data: String):String {
        var str = ""
        str="${dateGetYear(_data)}.${dateGetMonth(_data)}.${dateGetDay(_data)}" +
                " ${dateGetHH(_data)}:${dateGetMM(_data)}:${dateGetSS(_data)}"

        return str
    }

    //2021.07.15
    fun allDate(_data: String):String {
        var str = ""
        str="${dateGetYear(_data)}.${dateGetMonth(_data)}.${dateGetDay(_data)}"

        return str
    }



    //array -> String Json
    fun jsonArray2String(arrayString: Array<String>): String {
        var r1:String = "["
        for ((index, value) in arrayString.withIndex()) {
            r1 += "\""
            r1 += value
            r1 += "\""
            if (index<arrayString.size-1){
                r1 += ","
            }
        }
        r1+="],"
        return r1
    }



    //long long log
    fun  logE(tag:String, msg:String) {
        var logMessage = msg
        var max_str_length = 2001 - tag.length;
        //大于4000时
        while (logMessage.length > max_str_length) {
            Log.e(tag, logMessage.substring(0, max_str_length))
            logMessage = logMessage.substring(max_str_length)
        }
        //剩余部分
        Log.e(tag, logMessage);
    }

    fun getNow(): String {
        if (android.os.Build.VERSION.SDK_INT >= 24){
            return SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS").format(Date())
        }else{
            val tms = Calendar.getInstance()
            return tms.get(Calendar.YEAR).toString() + "-" +
                    tms.get(Calendar.MONTH).toString() + "-" +
                    tms.get(Calendar.DAY_OF_MONTH).toString() + " " +
                    tms.get(Calendar.HOUR_OF_DAY).toString() + ":" +
                    tms.get(Calendar.MINUTE).toString() +":" +
                    tms.get(Calendar.SECOND).toString() +"." +
                    tms.get(Calendar.MILLISECOND).toString()
        }

    }

    fun getDate(s:String): String {
        if (android.os.Build.VERSION.SDK_INT >= 24){
            val pattern:String ="yyyy"+s+"MM"+s+"dd"
            return SimpleDateFormat(pattern).format(Date())
        }else{
            val tms = Calendar.getInstance()
            return tms.get(Calendar.YEAR).toString() + "-" +
                    tms.get(Calendar.MONTH).toString() + "-" +
                    tms.get(Calendar.DAY_OF_MONTH).toString()
        }

    }

    fun getDate(): String {
        if (android.os.Build.VERSION.SDK_INT >= 24){
            return SimpleDateFormat("yyyyMMdd").format(Date())
        }else{
            val tms = Calendar.getInstance()
            return tms.get(Calendar.YEAR).toString() +
                    tms.get(Calendar.MONTH).toString() +
                    tms.get(Calendar.DAY_OF_MONTH).toString()
        }

    }

    fun showMsg(view: View, msg:String){
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()
    }


    fun msgAlertDialog(
        context:Context
        ,title:String
        ,message:String
        ,buttonMsg:String  ){

        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(buttonMsg) { _, which ->
            }
            .show()
    }

    fun sharedPrePut(key:String,value:String){
        val prefs = EtOfficeApp.context.getSharedPreferences(Config.EtOfficeUser, AppCompatActivity.MODE_PRIVATE)

        val editor = prefs.edit()
        editor.apply() {
            putString(key, value)
        }.apply()
    }

    fun sharedPreGetString(key: String): String {
        val prefs =
            EtOfficeApp.context.getSharedPreferences(Config.EtOfficeUser, Context.MODE_PRIVATE)
        return prefs.getString(key, "")!!
    }

    fun sharedPrePut(key:String,value:Int){
        val prefs = EtOfficeApp.context.getSharedPreferences(Config.EtOfficeUser, AppCompatActivity.MODE_PRIVATE)

        val editor = prefs.edit()
        editor.apply() {
            putInt(key, value)
        }.apply()
    }

    fun sharedPreGetInt(key: String): Int {
        val prefs =
            EtOfficeApp.context.getSharedPreferences(Config.EtOfficeUser, Context.MODE_PRIVATE)
        return prefs.getInt(key, 0)
    }

    fun sharedPreRemove(key: String){
        val pref = EtOfficeApp.context.getSharedPreferences(Config.EtOfficeUser, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = pref.edit()
        editor.remove(key)
        editor.apply()
    }
    /**
     * 时间日期转换
     * @param strDate 字符串yyyyMMddHHmmss
     * @return 字符串yyyy-MM-dd HH:mm:ss
     */
    fun strToDateLong(strDate: String?): String? {
        var date: Date? = Date()
        try {
            //先按照原格式转换为时间
            date = SimpleDateFormat("yyyyMMddHHmmss").parse(strDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        //再将时间转换为对应格式字符串
        return SimpleDateFormat("yyyy.MM.dd hh:mm:ss").format(date)
    }

    /**
     * 確認ダイアログ表示
     *
     * @param context
     * @param message
     * @param yesListener
     * @param noListener
     */
    fun showConfirmDialog(
        context: Context,
        title: String,
        message: String,
        yesListener: DialogInterface.OnClickListener? = null,
        noListener: DialogInterface.OnClickListener? = null
    ) {
        val alertBuilder = AlertDialog.Builder(context)
        alertBuilder.setCancelable(false)

        // タイトル
        val title = SpannableString(context.getString(R.string.confirm))
        title.setSpan(
            AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
            0,
            title.length,
            0
        )
        alertBuilder.setTitle(title)

        // メッセージ
        alertBuilder.setMessage(message)

        // ボタン
        alertBuilder.setPositiveButton(context.getString(R.string.yes), yesListener)
        alertBuilder.setNeutralButton(context.getString(R.string.no), noListener)

        // ダイアログ表示
        val dialog = alertBuilder.create()
        Handler(Looper.getMainLooper()).post {
            dialog.show()
        }
    }
}
