package com.example.kotou.contactbook;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    /**
     * アクティビティの初期化
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button sendButton = findViewById(R.id.schedule_button);
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplication(), ScheduleActivity.class);
                    startActivity(intent);
                }
            });

        Button mailButton = findViewById(R.id.mail_button);
            mailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplication(),MailActivity.class);
                    startActivity(intent);
                }
            });

        Button listButton = findViewById(R.id.list_button);
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), ListActivity.class);
                startActivity(intent);
            }
        });
    }
}