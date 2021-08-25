package com.xieyi.etoffice

class Config {
    companion object {
        const val appName = "EtOffice"
        const val ApiUrl = "https://ssl.ethp.net/EthpJson.aspx"
        // 連打判断間隔msec
        const val DOUBLE_CLICK_INTERVAL = 500L

        const val EtOfficeUser = "EtOfficeUser"
        const val LoginUrl = "https://ssl.ethp.net/EthpJson.aspx"
        const val Device = "android"
        /**
         * 程序测试，强行添加的代码
         */
        const val isTest = true

        /**
         * 程序开发，强行添加的代码
         */
        const val isCode = true

        const val tenantid="3"

        const val hpid="6"
    }

}