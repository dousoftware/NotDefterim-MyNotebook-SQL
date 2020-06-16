package com.dousoftware.notdefterim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class Hakkimda extends AppCompatActivity {
    Button git;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hakkimda);
        git=(Button)findViewById(R.id.sitemegit);
        git.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri link = Uri.parse("http://www.dousoftware.com/");
                Intent intent = new Intent(Intent.ACTION_VIEW,link);
                startActivity(intent);

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }
}
