package com.xieyi.etoffice

class Config {
    companion object {
        const val appName = "EtOffice"
        const val ApiUrl = "https://ssl.ethp.net/EthpJson.aspx"

        // 連打判断間隔msec
        const val DOUBLE_CLICK_INTERVAL = 500L

        const val EtOfficeUser = "EtOfficeUser"
        const val Device = "android"

        //初回登録Frag key
        const val FragKey = "selectFragKey"

        //LOCATION KEY
        const val REQUEST_CODE_LOCATION_KEY = 10008
    }

}