package com.example.flappyghost;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.flappyghost.R;

public class MainActivity extends Activity {

    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TextView scoreBox=findViewById(R.id.textView);

        setContentView(R.layout.activity_main);

        gameView=findViewById(R.id.gameView);
        Button pause=findViewById(R.id.pauseBtn);
        CheckBox check=findViewById(R.id.debugBox);

        gameView.addTextBox(scoreBox);

        pause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                gameView.pause();
            }
        });

        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                gameView.setDebug(isChecked);
            }
        });

    }


}
