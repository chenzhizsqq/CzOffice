package com.xieyi.etoffice.jsonData

class EtOfficeUserInfo {
    /*
 status 共仕様.処理結果ステータスを参照
result
    userid      ユーザー識別ID
    usercode    社員コード
    username    氏名
    userkana    カナ
    mail        メールアドレス
    phone       話番号
message 処理結果メッセージ
     */
    companion object {
        var userid:String = ""
        var usercode:String = ""
        var username:String = ""
        var userkana:String = ""
        var mail:String = ""
        var phone:String = ""
    }
}