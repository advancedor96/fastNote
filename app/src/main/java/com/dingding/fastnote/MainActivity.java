package com.dingding.fastnote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    SharedPreferences pref;
    String content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
    }

    private void initView() {
        editText = findViewById(R.id.editText);
        pref = getSharedPreferences("fastNote",MODE_PRIVATE);
        content = pref.getString("content", "");
        editText.requestFocus();
        editText.setText(content);
        editText.setSelection(content.length());

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pref.edit().putString("content", charSequence.toString()).commit();
                Log.d("xd", "已儲存");
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        String content = editText.getText().toString();
        pref.edit().putString("content", content).commit();

        Toast.makeText(this, "已儲存", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        content = pref.getString("content", "");
        editText.requestFocus();
        editText.setText(content);
        editText.setSelection(content.length());
        Log.d("xd", "onResume: ");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.selectAll){
            editText.selectAll();
            return true;
        }else if(item.getItemId() == R.id.copy){

            int start = editText.getSelectionStart();
            int end = editText.getSelectionEnd();
            String selectedContent = editText.getText().toString().substring(start, end);
            if(selectedContent.length() == 0){
                Toast.makeText(this, "沒有選取文字，複製失效", Toast.LENGTH_SHORT).show();
            }else{
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("text label", selectedContent);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "複製到剪貼簿", Toast.LENGTH_SHORT).show();
            }
            return true;

        }else if(item.getItemId() == R.id.clearall){

            AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(MainActivity.this);
            myAlertBuilder.setTitle("清除全部?");// .setMessage("清除全部?");
            myAlertBuilder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editText.setText("");
                    pref.edit().putString("content", "").commit();
                }
            });
            myAlertBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            myAlertBuilder.show();

            return true;

        }else if(item.getItemId() == R.id.save){
            String content = editText.getText().toString();
            pref.edit().putString("content", content).commit();

            Toast.makeText(this, "已儲存", Toast.LENGTH_SHORT).show();

            content = pref.getString("content", "");
            editText.setText(content);
            editText.setSelection(content.length());
            
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
