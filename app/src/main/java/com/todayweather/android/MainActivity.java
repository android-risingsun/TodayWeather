package com.todayweather.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  Log.d("MainActivity","new onCreate execute");
        Button btn = (Button) findViewById(R.id.button1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              Toast.makeText(MainActivity.this,"i am toast",Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onClick: mainactivity中的按钮被点击");
                Intent intent = new Intent("com.todayweather.android.ACTION_START");
                intent.addCategory("com.todayweather.android.My");
                intent.putExtra("extra_data","我是从首页来的！哈哈");
                startActivityForResult(intent,100);



//                //----打开系统前置摄像头--------/
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.putExtra("android.intent.extras.CAMERA_FACING",1);
//                startActivityForResult(intent,1);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_item:
                Toast.makeText(MainActivity.this,"add",Toast.LENGTH_SHORT).show();
                break;
            case R.id.remove_item:
                Toast.makeText(MainActivity.this,"remove",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 100:
                if(requestCode==RESULT_OK){
                    String data_return = data.getStringExtra("data_return");
                    Log.i(TAG, "onActivityResult: data_return"+data_return);
                }
                break;
            default:
                break;
        }
    }
}
