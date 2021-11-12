package com.example.kotou.contactbook;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class MailActivity extends AppCompatActivity {

    /**
     * アクティビティの初期化
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        Button returnButton = findViewById(R.id.return_button);
            returnButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

        Button button=findViewById(R.id.send_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText userEdit = findViewById(R.id.mail_account);
                String user = userEdit.getText().toString();
                EditText passwordEdit = findViewById(R.id.mail_password);
                String password = passwordEdit.getText().toString();
                EditText subEdit = findViewById(R.id.mail_sub);
                String sub_tex = subEdit.getText().toString();
                EditText textEdit = findViewById(R.id.mail_text);
                String tex = textEdit.getText().toString();
                asyncTask a=new asyncTask();
                a.execute(
                        user,
                        password,
                        sub_tex,
                        tex);
            }
        });
    }

    /**
     * メールの構成を作る処理をするクラス
     */
    private class asyncTask extends android.os.AsyncTask{
        protected String account;
        protected String password;
        protected String title;
        protected String text;

        /**
         * メールを送信するメゾット
         * @param obj   入力した文字列を受け取る
         * @return      メール送信の結果を返す
         */
        @Override
        protected Object doInBackground(Object... obj){
            account=(String) obj[0];
            password=(String) obj[1];
            title=(String) obj[2];
            text=(String) obj[3];

            java.util.Properties properties = new java.util.Properties();
            properties.put("mail.smtp.host","smtp.gmail.com");
            properties.put("mail.smtp.auth","true");
            properties.put("mail.smtp.port","465");
            properties.put("mail.smtp.socketFactory.post","465");
            properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");

            final javax.mail.Message msg =
                    new javax.mail.internet.MimeMessage(javax.mail.Session.getDefaultInstance(
                            properties,
                            new javax.mail.Authenticator(){
                @Override
                protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new javax.mail.PasswordAuthentication(account,password);
                }
            }));

            //登録したアドレスを取得して宛先に入れる
            try {
                SQLiteDatabase database;
                SQLiteOpenHelper helper = new AddressOpenHelper(MailActivity.this);
                database = helper.getReadableDatabase();

                Cursor cursor = database.query(
                        "EmailAddress",
                        new String[]{"address"},
                        null,
                        null,
                        null,
                        null,
                        null);
                cursor.moveToFirst();
                String[] mailto = new String[cursor.getCount()];
                for (int i = 0; i < mailto.length; i++){
                    mailto[i] = cursor.getString(0);
                    cursor.moveToNext();
                }
                cursor.close();
                InternetAddress[] addList = new InternetAddress[mailto.length];
                try {
                    for (int i=0; i<mailto.length; i++){
                        addList[i] = new InternetAddress(mailto[i]);
                    }
                } catch (AddressException e) {
                    e.printStackTrace();
                }

                msg.setFrom(new javax.mail.internet.InternetAddress(account + "@gmail.com"));
                msg.setRecipients(javax.mail.Message.RecipientType.TO, addList);
                msg.setSubject(title);
                msg.setText(text);

                javax.mail.Transport.send(msg);

            } catch (Exception e) {
                return (Object)e.toString();
            }
            return (Object)"送信が完了しました";
        }

        /**
         * 送信が出来たかをToastで出力する
         * @param obj   上の処理で起きたエラーメッセージか送信完了を受け取る
         */
        @Override
        protected void onPostExecute(Object obj) {
            //画面にメッセージを表示する
            Toast.makeText(MailActivity.this,(String)obj,Toast.LENGTH_LONG).show();
        }
    }
}