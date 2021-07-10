package com.xieyi.etoffice.jsonData

class EtOfficeLogin {
    companion object {
        var token:String = ""     //ログイン成功した場合、tokenを戻ります。
        var tenantid:String = ""  //会社識別ID
        var hpid:String = ""      //WEBサイト識別ID
        var userid:String = ""      //ユーザー識別ID
        var usercode:String = ""    //社員コード
        var username:String = ""    //氏名
        var userkana:String = ""    //カナ
        var mail:String = ""        //メールアドレス
        var phone:String = ""       //話番号
    }
}