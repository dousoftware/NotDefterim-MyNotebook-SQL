package com.dousoftware.notdefterim;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String>baslikArray;
    ArrayList<Integer>idArray;
    ArrayAdapter arrayAdapter;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        baslikArray = new ArrayList<String>();
        idArray = new ArrayList<Integer>();

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, baslikArray);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, NotDuzenle.class);
                //listview'deki nota tıklayınca eski not gelecek
                intent.putExtra("baslikId", idArray.get(position));
                intent.putExtra("info","eski");
                startActivity(intent);

            }


        });

        getData();
        //FAB oluştur, tıklama ver, not düzenleme sayfasına gitsin.
        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NotDuzenle.class);
                intent.putExtra("info", "yeni"); //yeni not oluşturmayı kontrol
                startActivity(intent);
            }
        });


    }

    public void getData(){
        //veri çekme
        try {
            database = this.openOrCreateDatabase("Notlar", MODE_PRIVATE, null);
            Cursor cursor = database.rawQuery("SELECT * FROM notlar", null); //sorgu
            int baslikIx = cursor.getColumnIndex("baslik");
            int idX = cursor.getColumnIndex("id");

            while (cursor.moveToNext()){
                baslikArray.add(cursor.getString(baslikIx));
                idArray.add(cursor.getInt(idX));

            }
            arrayAdapter.notifyDataSetChanged();
            cursor.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //menü çağırdım
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.ana_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        int id = item.getItemId();
        if (id == R.id.tum_notlari_sil){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(getString(R.string.alert_dialog_baslik));
            builder.setMessage(getString(R.string.alert_dialog_gosterilecek_mesaj));
            builder.setCancelable(true);

            builder.setPositiveButton(getString(R.string.olumlu_cevap), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    database.execSQL("DELETE FROM Notlar"); //Evet butonunda yapılacak işlem

                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton(getString(R.string.olumsuz_cevap), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel(); //hayır butonunda yapılacak işlem
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }else {
            if(id == R.id.hak){
                Intent intent = new Intent(MainActivity.this, Hakkimda.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
