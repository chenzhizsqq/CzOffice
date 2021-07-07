package com.xieyi.etoffice.enum

enum class ResultType {
    SUCCESS,            // 成功
    FAILED_API,         // APIエラー
    FAILED_AUTH,        // 認証エラー
    FAILED_NETWORK,     // ネットワーク接続不可
    FAILED_OTHER        // その他エラー
}