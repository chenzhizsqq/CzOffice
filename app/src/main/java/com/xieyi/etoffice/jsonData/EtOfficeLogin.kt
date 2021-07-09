package com.xieyi.etoffice.jsonData

class EtOfficeLogin {
    /*
 status
共仕様.処理結果ステータスを参照
result
    token       ログイン成功した場合、tokenを戻ります。
    tenantid    会社識別ID
    hpid        WEBサイト識別ID
    userid      ユーザー識別ID
    usercode    社員コード
    username    氏名
    userkana    カナ
    mail        メールアドレス
    phone       話番号
message
処理結果
     */
    companion object {
        var token:String = ""
        var tenantid:String = ""
        var hpid:String = ""
        var userid:String = ""
        var usercode:String = ""
        var username:String = ""
        var userkana:String = ""
        var mail:String = ""
        var phone:String = ""
    }
}