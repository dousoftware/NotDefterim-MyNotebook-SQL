package com.dousoftware.notdefterim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.EditText;


public class NotDuzenle extends AppCompatActivity {

    SQLiteDatabase database;

    private EditText baslik_text;
    private EditText not_text;
    int baslikId=1;
    String info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_duzenle);
        baslik_text = findViewById(R.id.baslik_text);
        not_text = findViewById(R.id.not_text);
        Intent intent = getIntent();

        database = this.openOrCreateDatabase("Notlar", MODE_PRIVATE, null);



        //yeni not oluştur'da boş gelecek
        info = intent.getStringExtra("info");
        if (info.matches("yeni")){
            baslik_text.setText("");
            not_text.setText("");
        }else{
            baslikId = intent.getIntExtra("baslikId", 1);
            try {
                Cursor cursor = database.rawQuery("SELECT * FROM notlar WHERE id=?",new String[]{String.valueOf(baslikId)});
                int baslikIx = cursor.getColumnIndex("baslik");
                int notIx = cursor.getColumnIndex("notum");

                while (cursor.moveToNext()){
                    baslik_text.setText(cursor.getString(baslikIx));
                    not_text.setText(cursor.getString(notIx));

                }
                cursor.close();
            }catch (Exception e){

            }
        }

    }


    //notu kaydetmek için kaydet metodu oluşturdum
    public void kaydet(){
        String baslik = baslik_text.getText().toString();
        String notum = not_text.getText().toString();
        if(TextUtils.isEmpty(baslik)){
            baslik_text.setError(getString(R.string.baslik_uyari));
            return;
        }
        try {
            database = this.openOrCreateDatabase("Notlar", MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS notlar (id INTEGER PRIMARY KEY, baslik VARCHAR, notum VARCHAR)");

            String sqlString = "INSERT INTO notlar(baslik, notum) VALUES (?, ?)";
            SQLiteStatement sqLiteStatement = database.compileStatement(sqlString);
            sqLiteStatement.bindString(1, baslik);
            sqLiteStatement.bindString(2, notum);
            sqLiteStatement.execute();
        }catch (Exception e){

        }
        Intent intent = new Intent(NotDuzenle.this, MainActivity.class); //kaydettikten sonra MainActivity'ye geri dönsün
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        //finish();
    }

    public void sil(int id){
        database.execSQL("DELETE FROM notlar WHERE id="+id); //id'ye göre silsin
        Intent intent = new Intent(NotDuzenle.this, MainActivity.class); //sildikten sonra MainActivity'e geri dönsün
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    public void guncelle(int id){
        database.execSQL("UPDATE Notlar SET baslik='"+baslik_text.getText()+"',notum='"+not_text.getText()+"' WHERE id='"+id+"'");
        Intent intent = new Intent(NotDuzenle.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        // Çalışmasını istediğiniz kodu buraya yazacağız
        //geri gelme tuşuna basılacak işlem
        Intent intent = new Intent(NotDuzenle.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        //return;
        super.onBackPressed();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Bu Activity'de hangi menüyü kullanacağımızı belirliyoruz.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.ekle_sil_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Kullanıcı hangi Item'ı seçerse ne işlem yapacağımızı yazıyoruz.

        switch (item.getItemId()){
            case R.id.not_kaydet:
                if (info.matches("yeni")){
                    kaydet();
                }else{
                    guncelle(baslikId);
                }
                return true;
            case R.id.not_sil:
                sil(baslikId);
                return true;
        }

        /*int id = item.getItemId();

        Log.e("D0 bir id var mı:", ""+baslikId);
        if (!info.matches("yeni")){
                Log.e("D0 baslik boyutu:", ""+baslik_text.getText().length());
                Log.e("D0_Güncelle id:", ""+baslikId);
                guncelle(baslikId);
            }else {
                kaydet();
            }
        if (id == R.id.not_sil){
            sil(baslikId);
        }*/


        return super.onOptionsItemSelected(item);
    }

}
