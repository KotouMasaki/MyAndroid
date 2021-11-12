package com.example.kotou.contactbook;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class ScheduleActivity extends AppCompatActivity {

    /**
     * アクティビティの初期化
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_schedule);

        Button returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText userEdit = findViewById(R.id.mail_account);
                String user = userEdit.getText().toString();
                EditText passwordEdit = findViewById(R.id.mail_password);
                String password = passwordEdit.getText().toString();
                asyncTask a=new asyncTask();
                a.execute(
                        user,
                        password,
                        "今週の学習スケジュール");
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

            try {
                SQLiteDatabase database;
                SQLiteOpenHelper helper = new AddressOpenHelper(ScheduleActivity.this);
                database = helper.getReadableDatabase();

                Cursor cursor = database.query("EmailAddress",
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

                //EditTextで書いたスケジュールをStringで受け取る
                EditText monEdit = findViewById(R.id.start_mon);
                String sta_mon = monEdit.getText().toString();
                EditText monTEdit = findViewById(R.id.time_mon);
                String time_mon = monTEdit.getText().toString();
                EditText tueEdit = findViewById(R.id.start_tue);
                String sta_tue = tueEdit.getText().toString();
                EditText tueTEdit = findViewById(R.id.time_tue);
                String time_tue = tueTEdit.getText().toString();
                EditText wedEdit = findViewById(R.id.start_wed);
                String sta_wed = wedEdit.getText().toString();
                EditText wedTEdit = findViewById(R.id.time_wed);
                String time_wed = wedTEdit.getText().toString();
                EditText thuEdit = findViewById(R.id.start_thu);
                String sta_thu = thuEdit.getText().toString();
                EditText thuTEdit = findViewById(R.id.time_thu);
                String time_thu = thuTEdit.getText().toString();
                EditText friEdit = findViewById(R.id.start_fri);
                String sta_fri = friEdit.getText().toString();
                EditText friTEdit = findViewById(R.id.time_fri);
                String time_fri = friTEdit.getText().toString();
                EditText satEdit = findViewById(R.id.start_sat);
                String sta_sat = satEdit.getText().toString();
                EditText satTEdit = findViewById(R.id.time_sat);
                String time_sat = satTEdit.getText().toString();
                EditText sunEdit = findViewById(R.id.start_sun);
                String sta_sun = sunEdit.getText().toString();
                EditText sunTEdit = findViewById(R.id.time_sun);
                String time_sun = sunTEdit.getText().toString();

                msg.setFrom(new javax.mail.internet.InternetAddress(account + "@gmail.com"));
                msg.setRecipients(javax.mail.Message.RecipientType.TO, addList);
                msg.setSubject(title);
                msg.setText("月曜日\r\n" +
                                "勉強開始時間:" +  sta_mon + "\r\n" +
                        "勉強時間:" + time_mon + "\r\n" +
                        "火曜日\r\n" +
                        "勉強開始時間:" +  sta_tue + "\r\n" +
                        "勉強時間:" + time_tue + "\r\n" +
                        "水曜日\r\n" +
                        "勉強開始時間:" +  sta_wed + "\r\n" +
                        "勉強時間:" + time_wed + "\r\n" +
                        "木曜日\r\n" +
                        "勉強開始時間:" +  sta_thu + "\r\n" +
                        "勉強時間:" + time_thu + "\r\n" +
                        "金曜日\r\n" +
                        "勉強開始時間:" +  sta_fri + "\r\n" +
                        "勉強時間:" + time_fri + "\r\n" +
                        "土曜日\r\n" +
                        "勉強開始時間:" +  sta_sat + "\r\n" +
                        "勉強時間:" + time_sat + "\r\n" +
                        "日曜日\r\n" +
                        "勉強開始時間:" +  sta_sun + "\r\n" +
                        "勉強時間:" + time_sun
                );

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
            Toast.makeText(ScheduleActivity.this,(String)obj,Toast.LENGTH_LONG).show();
        }
    }
}