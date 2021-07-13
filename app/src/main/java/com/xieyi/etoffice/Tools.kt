package com.xieyi.etoffice

object Tools {
    private const val TAG = "Tools"

    fun dataChange(_data: String?, _insert: String?): String {
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

    fun dataGetYear(_data: String): Int {
        if (_data.isEmpty()) return 0
        val str = _data.substring(0, 4)
        return str.toInt()
    }

    fun dataGetMonth(_data: String): Int {
        if (_data.isEmpty()) return 0
        val str = _data.substring(4, 6)
        return str.toInt()
    }

    fun dataGetDay(_data: String): Int {
        if (_data.isEmpty()) return 0
        val str = _data.substring(6, 8)
        return str.toInt()
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
}
