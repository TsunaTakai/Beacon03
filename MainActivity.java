package jp.co.hcs.ttakai.beacon03;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void changelable(View view) {
        TextView tv = (TextView)findViewById(R.id.mytextView);
        tv.setText("Changed!");
    }
}
