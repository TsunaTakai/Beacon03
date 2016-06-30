package jp.co.hcs.ttakai.beacon03;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

public class MainActivity extends Activity implements BeaconConsumer {

    private BeaconManager beaconManager;
    private Region mRegion;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    public static final String IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }
        //インスタンス化
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_FORMAT));
    }

    public void changelable(View view) {
        TextView tv = (TextView) findViewById(R.id.mytextView);
        tv.setText("Changed!");
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                // 領域への入場を検知
                    Log.d("Beacon", "ENTER Region.");
            }

            @Override
            public void didExitRegion(Region region) {
                // 領域からの退場を検知
                    Log.d("Beacon", "EXIT Region. ");
            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {
                // 領域への入退場のステータス変化を検知
                Log.d("MainActivity", "DetermineState: " + i);
            }
        });
        try {
            Identifier scan_uuid = Identifier.parse("12300101-39FA-4005-860C-09362F6169DA");
            Identifier scan_major = Identifier.parse(Integer.toString(33024));
            Identifier scan_minor = Identifier.parse(Integer.toString(256));
            mRegion = new Region("townbeacon",scan_uuid,scan_major,scan_minor);
            beaconManager.startMonitoringBeaconsInRegion(mRegion);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        beaconManager.bind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        beaconManager.unbind(this);
    }
}