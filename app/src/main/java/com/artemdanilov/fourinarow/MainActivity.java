package com.artemdanilov.fourinarow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity {
    private int difficulty = 5;
    private Context context;
String TAG="DEBUG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setContentView(R.layout.activity_main);

            context = this;

            Button newGame = (Button) findViewById(R.id.game_button);
            Button settings = (Button) findViewById(R.id.settings_button);

            newGame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, StartActivity.class);
                    intent.putExtra("DIFFICULTY", difficulty);
                    startActivity(intent);
                }
            });

            final AlertDialog.Builder diffDialog = new AlertDialog.Builder(this);
            diffDialog.setTitle("Difficulty");
            diffDialog.setMessage("Choose difficulty");
            diffDialog.setNegativeButton("Easy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    difficulty = 2;
                    dialog.cancel();
                }
            });
            diffDialog.setNeutralButton("Medium", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    difficulty = 5;
                    dialog.cancel();
                }
            });
            diffDialog.setPositiveButton("Hard", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    difficulty = 7;
                    dialog.cancel();
                }
            });
            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    diffDialog.create().show();
                }
            });
        }catch (Exception e){
            Log.i(TAG,e.toString());
        }
    }
}
