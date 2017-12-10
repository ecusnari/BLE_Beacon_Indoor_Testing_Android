package ecusnari.testapp.PositionList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ecusnari.testapp.Login_MainActivity.TestPosition;
import ecusnari.testapp.R;
import ecusnari.testapp.Test.TestActivity;



/**
 * Created by Ericas on 19.10.2017.
 */

public class PositionListActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "PositionListActivity";

    private List<PositionRow> positionList = new ArrayList<>();
    private List<PositionRow> positionLoadList;
    private PositionAdapter positionListAdapter;
    private RecyclerView positionListRecycler;

    private TextView buildingTextView;
    private TextView floorTextView;

    private int user_id;
    private int test_loc_id;
    private String building;
    private String floor;
    private List<TestPosition> testPositionsList = new ArrayList<>();

    private boolean running;

    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.position_list);


    }


    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();


        //setup recyclerView for position list
        positionListRecycler = (RecyclerView) findViewById(R.id.positionListRecycler);
        bindViews();
        //bindRecycler();
        getIntentData();

        running = true;

        new MyAsyncTask().execute();

        while(running){
            Log.d(TAG, "onResume.running");
        }



    }

    private void getIntentData(){
        intent = getIntent();
        if(intent.hasExtra("USER_ID")){
            user_id = intent.getIntExtra("USER_ID",-1);
            Log.d(TAG, "onResume: passed extraUserId -> " + user_id);

        }

        if(intent.hasExtra("Building")){
            building = intent.getStringExtra("Building");
            buildingTextView.setText(building);
        }

        if(intent.hasExtra("Floor")){
            floor = intent.getStringExtra("Floor");
            floorTextView.setText(floor);
        }

        if(intent.hasExtra("TEST_POS_LIST")){
            Log.d(TAG, "onResume, got the passed the testPositionLists to FloorListActivity");
            testPositionsList = intent.getParcelableArrayListExtra("TEST_POS_LIST");
            Log.d(TAG, "onResume: received from Floor Activity "+testPositionsList.size()+" test positions");
        }
    }

    private void bindViews(){
        buildingTextView = (TextView) findViewById(R.id.buildingPositionText);
        floorTextView = (TextView) findViewById(R.id.floorPositionText);
    }

    private void bindRecycler(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        positionListRecycler.setLayoutManager(linearLayoutManager);

        positionListAdapter = new PositionAdapter(positionList, this);
        Log.d(TAG, "onResume: positionList content passed to new adapter, size "+positionList.size()+"\n");
        positionListRecycler.setAdapter(positionListAdapter);
        Log.d(TAG,"onResume: refresh adapter");
    }

    class MyAsyncTask extends AsyncTask<String, Integer, List<PositionRow>>{

        @Override
        protected List<PositionRow> doInBackground(String... strings) {

            try{

                positionList = new ArrayList<>();
                loadPositions();

                Log.d(TAG, "doInBackground: positionLoadList has " + positionLoadList.size() + " elements");
                for(PositionRow br:positionLoadList){
                    positionList.add(br);
                }
                bindRecycler();
                Log.d(TAG, "doInBackground: positionList has " + positionList.size() + " elements");



            }catch(Exception e){
                e.printStackTrace();
            }

            running = false;
            return positionList;
        }

        @Override
        protected void onPostExecute(List<PositionRow> positionRows) {
            super.onPostExecute(positionRows);
            running = false;
        }
    }

    @Override
    public void onClick(View view) {
        int pos = positionListRecycler.getChildLayoutPosition(view);
        PositionRow br = positionList.get(pos);

        Log.d(TAG, "Position clicked -> "+ br.toString());


        Intent TestActivityIntent = new Intent(PositionListActivity.this, TestActivity.class);
        TestActivityIntent.putExtra("USER_ID", user_id);
        Log.d(TAG, "From PositionListActivity Pasisng user_id: " + user_id);
        TestActivityIntent.putExtra("TEST_LOC_ID",Integer.valueOf(br.getPositionID()));
        TestActivityIntent.putExtra("TEST_POS_LIST",intent.getParcelableArrayListExtra("TEST_POS_LIST"));

        startActivity(TestActivityIntent);
    }

    private void loadPositions(){
        Log.d(TAG, "loadPositions: Loading Positions");

        positionLoadList = new ArrayList<PositionRow>();
        ArrayList<String> extractPositions = new ArrayList<>();

        Log.d(TAG, "loadPositions: testPositionsList has "+testPositionsList.size());
        for(TestPosition tP:testPositionsList){
            Log.d(TAG, "Building: " + tP.getBuilding_name() + ", floor: " + tP.getTest_loc_floor_id() + ", posID " + tP.getTest_loc_id());
            Log.d(TAG, "Versus -> building: " + building + ", floor: " + floor );

            if((tP.getBuilding_name().equals(building))&& (tP.getTest_loc_floor_id() == Integer.valueOf(floor)) && (!extractPositions.contains(String.valueOf(tP.getTest_loc_id())))){
                Log.d(TAG, "loadFloors: added to extract floors Building " + tP.getBuilding_name() + "("+building+") floor "+floor + "loc_id " + tP.getTest_loc_id());
                extractPositions.add(String.valueOf(tP.getTest_loc_id()));
            }
        }

        for(String b:extractPositions){
            PositionRow br = new PositionRow(b);
            positionLoadList.add(br);
        }

    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed");
        //super.onBackPressed();
        finish();
    }
}































