package com.xieyi.etoffice

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
}
