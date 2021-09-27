package com.xieyi.etoffice.enum

enum class RecyclerType(val rawValue: Int) {
    HEADER(0),              // ヘッダー
    FOOTER(1),              // フッター
    SECTION(2),             // セクション
    SECTION_HEADER(3),      // セクションヘッダー
    SECTION_FOOTER(4),      // セクションフッター
    ITEM(5),                // セクションアイテム
}