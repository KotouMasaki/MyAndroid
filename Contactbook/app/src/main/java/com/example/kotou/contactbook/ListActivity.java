package com.example.kotou.contactbook;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;
import static com.example.kotou.contactbook.DBContract.DBEntry;

public class ListActivity extends AppCompatActivity {

    private AddressOpenHelper helper = null;
    ListAdapter sc_adapter;

    /**
     * アクティビティの初期化処理
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Button returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * アクティビティの再開処理
     */
    @Override
    protected void onResume() {
        super.onResume();

        // データを一覧表示
        onShow();
    }

    /**
     * データを一覧表示
     */
    protected void onShow() {

        // データベースヘルパーを準備
        helper = new AddressOpenHelper(this);

        // データベースを検索する項目を定義
        String[] cols = {DBEntry._ID, DBEntry.COLUMN_NAME_ADDRESS, DBEntry.COLUMN_NAME_NAME };

        // 読み込みモードでデータベースをオープン
        try (SQLiteDatabase db = helper.getReadableDatabase()){

            // データベースを検索
            Cursor cursor = db.query(DBEntry.TABLE_NAME, cols, null,
                    null, null, null, null, null);

            // 検索結果から取得する項目を定義
            String[] from = {DBEntry.COLUMN_NAME_NAME};

            // データを設定するレイアウトのフィールドを定義
            int[] to = {R.id.title};

            // ListViewの1行分のレイアウト(row_main.xml)と検索結果を関連付け
            sc_adapter = new ListAdapter(
                    this, R.layout.list_row, cursor, from, to,0);

            // activity_list.xmlに定義したListViewオブジェクトを取得
            ListView list = findViewById(R.id.List);

            // ListViewにアダプターを設定
            list.setAdapter(sc_adapter);

            // リストの項目をクリックしたときの処理
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView av, View view, int position, long id) {

                    //　クリックされた行のデータを取得
                    Cursor cursor = (Cursor)av.getItemAtPosition(position);

                    // テキスト登録画面 Activity へのインテントを作成
                    Intent intent  = new Intent(ListActivity.this, com.example.kotou.contactbook.RegisterActivity.class);

                    intent.putExtra(DBEntry._ID, cursor.getInt(0));
                    intent.putExtra(DBEntry.COLUMN_NAME_ADDRESS, cursor.getString(1));
                    intent.putExtra(DBEntry.COLUMN_NAME_NAME, cursor.getString(2));

                    // アクティビティを起動
                    startActivity(intent);
                }
            });
        }
    }

    /**
     * 削除ボタン　タップ時に呼び出されるメソッド
     */
    public void btnDel_onClick(View view){

        // MainListAdapterで設定されたリスト内の位置を取得
        int pos = (Integer)view.getTag();

        // アダプターから、_idの値を取得
        int id = ((Cursor) sc_adapter.getItem(pos)).getInt(0);

        // データを削除
        try (SQLiteDatabase db = helper.getWritableDatabase()) {
            db.delete(DBEntry.TABLE_NAME, DBEntry._ID+" = ?", new String[] {String.valueOf(id)});
        }

        // データを一覧表示
        onShow();
    }

    /**
     * 登録ボタンタップ時に呼び出されるメソッド
     */
    public void fab_reg_onClick(View view) {

        // テキスト登録画面 Activity へのインテントを作成
        Intent intent  = new Intent(ListActivity.this, com.example.kotou.contactbook.RegisterActivity.class);

        // アクティビティを起動
        startActivity(intent);
    }
}