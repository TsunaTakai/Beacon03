package jp.co.hcs.ttakai.beacon03;

import android.app.Activity;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

public class MainActivity extends Activity implements BeaconConsumer {

    private BeaconManager beaconManager;
    Region mRegion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //インスタンス化
        beaconManager = BeaconManager.getInstanceForApplication(this);

        String IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_FORMAT));

        Identifier scan_uuid = Identifier.parse("12300101-39FA-4005-860C-09362F6169DA");
        Identifier scan_major = Identifier.parse(Integer.toString(33024));
        Identifier scan_minor = Identifier.parse(Integer.toString(256));
        mRegion = new Region("townbeacon",scan_uuid,scan_major,scan_minor);
    }

    public void changelable(View view) {
        TextView tv = (TextView) findViewById(R.id.mytextView);
        tv.setText("Changed!");
    }

    @Override
    public void onBeaconServiceConnect() {

        try {
            beaconManager.startMonitoringBeaconsInRegion(mRegion);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                // 領域への入場を検知
                try {
                    Log.d("Beacon", "ENTER Region.");
                    beaconManager.startRangingBeaconsInRegion(mRegion);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void didExitRegion(Region region) {
                // 領域からの退場を検知
                try {
                    Log.d("Beacon", "EXIT Region. ");
                    beaconManager.stopRangingBeaconsInRegion(mRegion);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {
                // 領域への入退場のステータス変化を検知
                Log.d("MyActivity", "DetermineState: " + i);
            }
        });
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
