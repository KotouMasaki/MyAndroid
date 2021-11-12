package com.example.kotou.contactbook;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import static com.example.kotou.contactbook.DBContract.DBEntry;

public class RegisterActivity extends AppCompatActivity {
    private int id = 0;
    private EditText editTitle = null;
    private EditText editContents = null;

    /**
     * アクティビティの初期化
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // ビューオブジェクトを取得
        editTitle = findViewById(R.id.edit_address);
        editContents = findViewById(R.id.edit_name);

        // インテントを取得
        Intent intent = getIntent();

        //intentのデータを取得(データがない場合、第２引数の 0 が返る)
        id = intent.getIntExtra(DBEntry._ID,0);
        String title = intent.getStringExtra(DBEntry.COLUMN_NAME_ADDRESS);
        String contents = intent.getStringExtra(DBEntry.COLUMN_NAME_NAME);

        // データ更新の場合
        if (id > 0){
            editTitle.setText(title);
            editContents.setText(contents);
        }
    }

    /**
     * 「登録」ボタン　タップ時に呼び出されるメソッド
     */
    public void btnReg_onClick(View view) {

        // ヘルパーを準備
        AddressOpenHelper helper = new AddressOpenHelper(this);

        // 入力欄に入力されたタイトルとコンテンツを取得
        String address    = editTitle.getText().toString();
        String name = editContents.getText().toString();

        // 書き込みモードでデータベースをオープン
        try (SQLiteDatabase db = helper.getWritableDatabase()) {

            // 入力されたタイトルとコンテンツをContentValuesに設定
            // ContentValuesは、項目名と値をセットで保存できるオブジェクト
            ContentValues cv = new ContentValues();
            cv.put(DBEntry.COLUMN_NAME_ADDRESS, address);
            cv.put(DBEntry.COLUMN_NAME_NAME, name);

            if(id == 0) {
                // データ新規登録
                db.insert(DBEntry.TABLE_NAME, null, cv);
            } else {
                // データ更新
                db.update(DBEntry.TABLE_NAME, cv, DBEntry._ID + " = ?", new String[] {String.valueOf(id)});
            }
        }

        // TextActivityを終了
        finish();
    }

    /**
     * 「キャンセル」ボタン　タップ時に呼び出されるメソッド
     */
    public void btnCancel_onClick(View view) {

        // TextActivityを終了
        finish();
    }

}