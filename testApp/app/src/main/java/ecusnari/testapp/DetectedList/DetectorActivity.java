package ecusnari.testapp.DetectedList;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ecusnari.testapp.BuildingList.BuildingListActivity;
import ecusnari.testapp.R;
import ecusnari.testapp.Test.TestActivity;

/**
 * Created by Ericas on 16.11.2017.
 */

public class DetectorActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAG = "DetectorActivity";

    private int user_id;
    private int test_loc_id;
    private String beaconsStoredString;

    private Button buttonStartTestActivity;
    private TextView numberBeaconSignals;
    private TextView numberUniqueBeacons;
    private TextView loc_Id_TextView;

    //Recycler setup
    private List<DetectorRow> detectorList = new ArrayList<>();
    private DetectorAdapter detectedAdapter;
    private RecyclerView detectorListRecycler;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detector_list_activity);

        bindViews();
        bindButtons();
        bindRecyclerView();

    }

    private void bindRecyclerView(){
        //Recycler
        detectorListRecycler = (RecyclerView) findViewById(R.id.detectorListRecycler);
        detectorList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        detectorListRecycler.setLayoutManager(linearLayoutManager);

        Log.d(TAG, "****Creating detectedAdapter for recycler list****");
        detectedAdapter = new DetectorAdapter(detectorList, this);
        Log.d(TAG, "onResume: positionList content passed to new adapter, size "+detectorList.size()+"\n");
        detectorListRecycler.setAdapter(detectedAdapter);
        Log.d(TAG,"onResume: refresh adapter");

    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        getIntentData();
        checkIntentData();
        loc_Id_TextView.setText(String.valueOf(test_loc_id));
    }

    private void getIntentData(){
        intent = getIntent();

        if(intent.hasExtra("BEACONS")){
            beaconsStoredString = intent.getStringExtra("BEACONS");
            parseJsonString();
        }
        if(intent.hasExtra("USER_ID")) {
            user_id = intent.getIntExtra("USER_ID", -1);
            Log.d(TAG, "getIntentData: user_id --> " + user_id);
        }

        if(intent.hasExtra("TEST_LOC_ID")) {
            test_loc_id = intent.getIntExtra("TEST_LOC_ID", -1);
            Log.d(TAG, "getIntentData: loc_id --> " + test_loc_id);
        }
    }

    private void checkIntentData(){
        if(user_id <= 0 || test_loc_id <= 0){
            Log.d(TAG, "checkIntentData: user_id: " + user_id + ", test_loc_id: " + test_loc_id);
            alertTestFailed();
        }else{
            //buttonStartTestActivity.setOnClickListener(new StartOnClickListener());
        }
    }

    private void parseJsonString(){
        try{
            JSONObject json = new JSONObject(beaconsStoredString);
            JSONArray beaconsJson = json.getJSONArray("beacon_scanned");

            for(int i = 0; i < beaconsJson.length();i++){
                detectorList.add(new DetectorRow(String.valueOf(i+1), beaconsJson.getJSONObject(i).getString("major"), beaconsJson.getJSONObject(i).getString("minor"), beaconsJson.getJSONObject(i).getString("RSSI")));
            }
            numberBeaconSignals.setText(String.valueOf(detectorList.size()));
            getNumberUniqueBeacons();
            detectedAdapter.notifyDataSetChanged();
        }catch(Exception e){
            e.printStackTrace();
            Log.d(TAG, "parsJsonString: error parsing the JsonString");
            alertTestFailed();
        }

    }

    private void getNumberUniqueBeacons(){
        List<String> uniqueBeacons = new ArrayList<>();
         for(DetectorRow beacon: detectorList){
            if(!uniqueBeacons.contains(beacon.getMinorText()))
                uniqueBeacons.add(beacon.getMinorText());
        }

        numberUniqueBeacons.setText(String.valueOf(uniqueBeacons.size()));
    }

    private void alertTestFailed(){
        AlertDialog.Builder noDataDialog = new AlertDialog.Builder(this);
        noDataDialog.setTitle("Test Failed");
        noDataDialog.setMessage("Test data was not captured and stored. An error happened.\n Contact davids@iit.edu for help.");
        AlertDialog dialog = noDataDialog.create();
        dialog.show();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();

    }

    @Override
    public void onClick(View view) {
        int pos = detectorListRecycler.getChildLayoutPosition(view);
        DetectorRow dr = detectorList.get(pos);
        Log.d(TAG, "Beacon clicked" + dr.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void bindViews() {

        buttonStartTestActivity = (Button) findViewById(R.id.buttonNewTest);
        numberBeaconSignals = (TextView) findViewById(R.id.numBeaconSignalsView);
        numberUniqueBeacons = (TextView) findViewById(R.id.numberBeacons);
        loc_Id_TextView = (TextView) findViewById(R.id.testLocIDText);
            }

    private void bindButtons() {
        buttonStartTestActivity.setOnClickListener(new StartOnClickListener());
    }

    public void startTestingOver() {

        Intent BuildingActivityIntent = new Intent(DetectorActivity.this, BuildingListActivity.class);
        BuildingActivityIntent.putExtra("USER_ID", user_id);
        BuildingActivityIntent.putExtra("TEST_POS_LIST", intent.getParcelableArrayListExtra("TEST_POS_LIST"));
        startActivity(BuildingActivityIntent);
        finish();
    }


    private class StartOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId()==R.id.buttonNewTest){
                startTestingOver();
            }
        }



    }

}
