package com.example.kotou.contactbook;

import android.provider.BaseColumns;

// データベースのテーブル名・項目名を定義
public final class DBContract {

    // 誤ってインスタンス化しないようにコンストラクタをプライベート宣言
    private DBContract() {}

    // テーブルの内容を定義
    public static class DBEntry implements BaseColumns {
        // BaseColumns インターフェースを実装することで、内部クラスは_IDを継承できる
        public static final String TABLE_NAME           = "EmailAddress";
        public static final String COLUMN_NAME_ADDRESS    = "address";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_UPDATE   = "up_date";
    }
}
