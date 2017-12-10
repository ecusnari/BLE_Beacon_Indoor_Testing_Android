package ecusnari.testapp.Test;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import ecusnari.testapp.BeaconData;
import ecusnari.testapp.BluetoothChecker;
import ecusnari.testapp.Data;
import ecusnari.testapp.DetectedList.DetectorActivity;
import ecusnari.testapp.Json;
import ecusnari.testapp.Login_MainActivity.LoginInfoDownloader;
import ecusnari.testapp.Login_MainActivity.TestPosition;
import ecusnari.testapp.R;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Ericas on 24.10.2017.
 */

public class TestActivity extends AppCompatActivity implements BeaconConsumer{

    private final String TAG = "TestActivity";
    private final String beaconAXAET_Layout = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25";
        /*This signifies that the beacon type will be decoded when an advertisement is found with 0x0215 in bytes 2-3, and a three-part identifier will be pulled out of bytes 4-19,
         bytes 20-21 and bytes 22-23, respectively. A signed power calibration value will be pulled out of byte 24, and a data field will be pulled out of byte 25.
          m - matching byte sequence for this beacon type to parse (exactly one required)
          s - ServiceUuid for this beacon type to parse (optional, only for Gatt-based beacons)
          i - identifier (at least one required, multiple allowed)
          p - power calibration field (exactly one required)...*/
    private final String beaconAXAET_UUID = "da50693-a4e2-4fb1-afcf-c6eb07647825";

    private JSONObject postJson;
    //For bluetooth check and turn on
    BluetoothChecker bluetooth;

    private Intent intent;
    private int user_id;
    private int test_loc_id;
    private ArrayList<TestPosition> testPositionsList = new ArrayList<>();
    //CustomRangeNotifier
    boolean testIsRunning = false;

    boolean detected = false;

    private ArrayList<Integer> rssi;
    private ArrayList<Identifier> major, minor, uuid;
    long testStartTime, tEnd, tDelta;


    ArrayList<BeaconData> listBeaconsDetected = new ArrayList<>();


    //RunTestButton
    String status, model;
    private Chronometer chron;
    boolean created = false;
    boolean testJustStarted = false;
    Data d;

    //For periodic operations during the 10 seconds sniffing
    Handler handlerCheckTestState;
    Runnable runnableForTestStateChecking;


    private TextView testTimeTF;
    private TextView testLocID;
    private Button testButton;
    private Button seeResultsButton;

    //onBeaconServiceConnect
    private BeaconManager beaconManager;

    //Beacon Object
    BeaconData beaconDetected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        //Bluetooth turn on
        bluetooth = new BluetoothChecker(getApplicationContext());
        bluetooth.enableBluetooth();

        //Manager for bluetooth beacons
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(beaconAXAET_Layout));
        beaconManager.bind(this);
        beaconManager.setBackgroundScanPeriod(250);


        bindViews();
        model = Build.MANUFACTURER + "-" + Build.MODEL;
        //TODO
        d=new Data();

        //test start not clicked
        testIsRunning = false;
        //data set with phone mac and number created
        created = false;


        handlerCheckTestState = new Handler();
        //WifiManager to be implemented for storage of data on the server

    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        testButton.setOnClickListener(new StartOnClickListener());

        intent = getIntent();
        if(intent.hasExtra("USER_ID")){
            user_id = intent.getIntExtra("USER_ID",-1);
            Log.d(TAG, "onResume: passed extraUserId -> " + user_id);

        }

        if(intent.hasExtra("TEST_LOC_ID")){
            test_loc_id = intent.getIntExtra("TEST_LOC_ID",-1);
            testLocID.setText(String.valueOf(test_loc_id));
            Log.d(TAG, "onResume: passed extraTestLocId -> " + test_loc_id);
        }

        if(intent.hasExtra("TEST_POS_LIST")){
            Log.d(TAG, "onResume, got the passed the testPositionLists to FloorListActivity");
            testPositionsList = intent.getParcelableArrayListExtra("TEST_POS_LIST");
            Log.d(TAG, "onResume: received from past activity "+testPositionsList.size()+" test positions");
        }

    }

    private class StartOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.buttonRunTest){

                setupTestVariables();
                final int delay = 600; //Do handler every 0.6 seconds

                handlerCheckTestState.postDelayed(runnableForTestStateChecking =new Runnable() {
                    public void run() {

                        if (testJustStarted) {
                            testJustStarted = false;
                        }

                        tEnd = System.currentTimeMillis();
                        tDelta = tEnd - testStartTime;
                        if (Integer.parseInt(chron.getText().toString().split(":")[1]) >= 10 ) {
                            Log.d(TAG, "handlerCheckTestState: test time is up");
                            chron.stop();
                            testIsRunning = false;
                            unbindBeaconManager();
                            storeTestData();
                            handlerCheckTestState.removeCallbacks(runnableForTestStateChecking);

                        }else{
                            handlerCheckTestState.postDelayed(this, delay);
                        }

                    }
                }, delay);
            }
            if(view.getId() == R.id.buttonSeeResults){
                startDetectorActivity();

            }
        }
    }

    public void startDetectorActivity() {

        Intent DetectorActivityIntent = new Intent(TestActivity.this, DetectorActivity.class);
        DetectorActivityIntent.putExtra("TEST_POS_LIST",intent.getParcelableArrayListExtra("TEST_POS_LIST"));
        DetectorActivityIntent.putExtra("BEACONS", postJson.toString());
        DetectorActivityIntent.putExtra("USER_ID", user_id);
        DetectorActivityIntent.putExtra("TEST_LOC_ID", test_loc_id);
        startActivity(DetectorActivityIntent);
        finish();
    }

    private void createTest() {
        if (!created) {
            //create test data set
            Log.d(TAG, "TestApp Cellphone Model: " + model);
            created = true;
        }
    }

    private void bindViews(){

        testTimeTF = (TextView)findViewById(R.id.testActivityTestTime);
        testLocID = (TextView) findViewById(R.id.testPosIDText_TestActivity);
        testButton = (Button) findViewById(R.id.buttonRunTest);
        seeResultsButton = (Button) findViewById(R.id.buttonSeeResults);
        chron = (Chronometer) findViewById(R.id.chronometer);
        status = "Not Completed";
    }

    private void unbindBeaconManager(){
        beaconManager.unbind(this);
    }

    private void storeTestData(){
        Log.d(TAG, "Storing " + listBeaconsDetected.size() + " of beacon signals");
        postJson = listBeaconsToJson();
        if(postJson!=null){
            Log.d(TAG, "beacons stored: -->" + postJson.toString());
            storeDataToServer(postJson);

        }else{
            Log.d(TAG, "storeTestData: error while storing data, JSONObject failed to be made for POST request.");
            alertTestFailed();
        }

    }

    private JSONObject listBeaconsToJson(){
        try{
            JSONArray arrayBeacons = new JSONArray();
            for(BeaconData beacon: listBeaconsDetected){
                JSONObject beaconJson = new JSONObject();
                beaconJson.put("major", beacon.getMajor());
                beaconJson.put("minor", beacon.getMinor());
                beaconJson.put("RSSI", beacon.getRSSI());

                arrayBeacons.put(beaconJson);
            }

            JSONObject jsonResultObject = new JSONObject();
            jsonResultObject.put("user_id", user_id);
            jsonResultObject.put("test_loc_id",test_loc_id);
            jsonResultObject.put("beacon_scanned", arrayBeacons);
            return jsonResultObject;
        }catch(Exception e){
            e.printStackTrace();
            return  null;
        }


    }

    private void storeDataToServer(JSONObject postJson){
        Log.d(TAG, "DownloadUserLoginResponse: starting to download Json for retrieving test positions");
        TestStorePOSTRequest downloader = new TestStorePOSTRequest(this, postJson);
        downloader.execute();
    }

    public void testStorageResponse(){
        seeResultsButton.setOnClickListener(new StartOnClickListener());
        seeResultsButton.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.OrangeSeeResultsButton, null));
    }

    public void alertTestFailed(){
        AlertDialog.Builder noDataDialog = new AlertDialog.Builder(this);
        noDataDialog.setTitle("Test Failed");
        noDataDialog.setMessage("Test data was not captured and stored. An error happened.\n Contact davids@iit.edu for help.");
        AlertDialog dialog = noDataDialog.create();
        dialog.show();
    }
    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier(new CustomRangeNotifier());
        beaconManager.addMonitorNotifier(new CustomMonitorNotifier());

        try {
            beaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
        }

    }

    public void setupTestVariables(){
        createTest();
        chron.setBase(SystemClock.elapsedRealtime());
        chron.start();
        //testTimeTF.set
        testIsRunning = true;
        testJustStarted = true;
        testStartTime = System.currentTimeMillis();
    }



    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed");
        //super.onBackPressed();

        //need to secure from quiting page before test done!
        finish();
    }

    private class CustomMonitorNotifier implements MonitorNotifier {
        @Override
        public void didEnterRegion(Region region) {
            Log.i(TAG, "I just saw a beacon for the testJustStarted time!");
            detected = true;
        }

        @Override
        public void didExitRegion(Region region) {
            Log.i(TAG, "I no longer see a beacon");
            detected = false;
        }

        @Override
        public void didDetermineStateForRegion(int state, Region region) {
            Log.i(TAG, "I have just switched from seeing/not seeing beacons: " + state);
        }
    }

    private class CustomRangeNotifier implements RangeNotifier {
        @Override
        public void didRangeBeaconsInRegion(Collection<Beacon> lastCapturedBeacons, Region region) {
            if (testIsRunning) {
                try {
                    long tEnd = System.currentTimeMillis();
                    long tDelta = tEnd - testStartTime;
                    Log.d(TAG, "Timer: " + String.valueOf(tDelta / 1000));

                    if ((tDelta / 1000.0) >= 10) {
                        Log.d(TAG, "didRangeBeaconsInRegion: test time is up");
                        testIsRunning = false;
                        Log.d(TAG, "didRangeBeaconsInRegion: stopped saving new captured beacons");
                    } else {
                        for (Beacon beacon : lastCapturedBeacons) {
                            Identifier major = beacon.getId2();
                            Identifier minor = beacon.getId3();
                            int RSSI = beacon.getRssi();
                            Identifier uuid = beacon.getId1();
                            BeaconData newBeacon = new BeaconData(Integer.valueOf(major.toString()), Integer.valueOf(minor.toString()), RSSI, uuid.toString());
                            listBeaconsDetected.add(newBeacon);
                            Log.d(TAG, "didRangeBeaconsInRegion:\n Captured beacon " + beacon.toString() + " (major: " + major + ", minor: " + minor + ", RSSI: " + RSSI + ", UUID: "+ uuid+").");

                        }
                    }
                } catch( Exception e) {
                    e.printStackTrace();
                }


            }
        }
    }
}
