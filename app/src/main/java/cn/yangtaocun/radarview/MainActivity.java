package cn.yangtaocun.radarview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private RadarView mRadarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRadarView = (RadarView) findViewById(R.id.radarview);
        mRadarView.setStartAngle();
        mRadarView.startAnge();

    }
}
