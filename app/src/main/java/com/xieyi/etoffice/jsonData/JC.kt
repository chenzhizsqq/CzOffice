package com.xieyi.etoffice.jsonData

/**
 * Json Center
 */
class JC {
    companion object {
        //EtOfficeLogin ログイン処理、ユーザー情報取得
        var pEtOfficeLogin = EtOfficeLogin()

        //EtOfficeUserInfo ユーザー情報取得
        var pEtOfficeUserInfo = EtOfficeUserInfo()

        //EtOfficeGetUserStatus ユーザー最新勤務状態の一覧取得
        var pEtOfficeGetUserStatus = EtOfficeGetUserStatus()

        //EtOfficeGetUserLocation   ユーザー勤務場所一覧取得
        var pEtOfficeGetUserLocation = EtOfficeGetUserLocation()

        //EtOfficeSetUserLocation   ユーザー勤務場所設定
        var pEtOfficeSetUserLocation = EtOfficeSetUserLocation()

        //EtOfficeSetTenant   起動会社設定登録
        var pEtOfficeSetTenant = EtOfficeSetTenant()

        //EtOfficeGetTenant   所属会社一覧
        var pEtOfficeGetTenant = EtOfficeGetTenant()

        //EtOfficeGetStuffList   社員一覧取得
        var pEtOfficeGetStuffList = EtOfficeGetStuffList()

        //EtOfficeGetStatusList   出勤状態一覧取得
        var pEtOfficeGetStatusList = EtOfficeGetStatusList()

        //EtOfficeSetMessage   最新メッセージ一覧set
        var pEtOfficeSetMessage = EtOfficeSetMessage()

        //EtOfficeSetUserStatus ユーザー勤務状態の設定
        var pEtOfficeSetUserStatus = EtOfficeSetUserStatus()

        //EtOfficeGetMessage   最新メッセージ一覧取得
        var pEtOfficeGetMessage = EtOfficeGetMessage()

        //EtOfficeGetReportList
        var pEtOfficeGetReportList = EtOfficeGetReportList()

        //EtOfficeSetApprovalJsk    勤務実績承認
        var pEtOfficeSetApprovalJsk = EtOfficeSetApprovalJsk()
    }
}