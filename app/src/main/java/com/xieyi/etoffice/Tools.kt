package com.xieyi.etoffice

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
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

    fun srcContent(src: String, maxLength: Int): String {
        var rString = src
        if (rString.length > maxLength) {
            rString = src.substring(0, maxLength)
            rString += "..."
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

    fun changeList(arrayList:ArrayList<String>):String{
        if(arrayList.size==0)return "[]"

        var r = "["
        for (a in arrayList){
            r +="\"$a\","
        }
        r = r.substring(0,r.length-1)
        r +="]"
        return r
    }


    private fun msgAlertDialog(
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
}
