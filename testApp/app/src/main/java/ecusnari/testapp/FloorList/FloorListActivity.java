package ecusnari.testapp.FloorList;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ecusnari.testapp.Login_MainActivity.TestPosition;
import ecusnari.testapp.PositionList.PositionListActivity;
import ecusnari.testapp.R;

/**
 * Created by Ericas on 19.10.2017.
 */

public class FloorListActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "FloorListActivity";

    private List<FloorRow> floorList = new ArrayList<>();
    private List<FloorRow> floorLoadList;
    private FloorAdapter bAdapter;
    private RecyclerView floorListRecycler;

    private ArrayList<TestPosition> testPositionsList = new ArrayList<TestPosition>();
    private int user_id;
    private String building;
    private Intent intent;

    private TextView buildingTextView;

    private boolean running;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floor_list);


    }


    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();

        buildingTextView = (TextView) findViewById(R.id.buildingFloorActivityText);

        //setup recyclerView for floor list
        floorListRecycler = (RecyclerView) findViewById(R.id.floorListRecycler);

        intent = getIntent();
        if(intent.hasExtra("USER_ID")){
            user_id = intent.getIntExtra("USER_ID",-1);
            Log.d(TAG, "onResume: passed extraUsername -> " + user_id);

        }

        if(intent.hasExtra("Building")){
            building = intent.getStringExtra("Building");
            buildingTextView.setText(building);
        }

        if(intent.hasExtra("TEST_POS_LIST")){
            Log.d(TAG, "onResume, got the passed the testPositionLists to FloorListActivity");
            testPositionsList = intent.getParcelableArrayListExtra("TEST_POS_LIST");
            Log.d(TAG, "onResume: received from past activity "+testPositionsList.size()+" test positions");
        }

        //flag to check when Asynk is done
        running = true;

        new MyAsyncTask().execute();

        while(running){
            Log.d(TAG, "onResume.running");
        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        floorListRecycler.setLayoutManager(linearLayoutManager);

        Collections.sort(floorList, new Comparator<FloorRow>() {
            @Override
            public int compare(FloorRow floorRow, FloorRow t1) {
                return floorRow.getFloorName().compareTo(t1.getFloorName());
            }
        });
        bAdapter = new FloorAdapter(floorList, this);
        Log.d(TAG, "onResume: floorList content passed to new adapter, size "+floorList.size()+"\n");
        floorListRecycler.setAdapter(bAdapter);
        Log.d(TAG,"onResume: refresh adapter");



    }

    class MyAsyncTask extends AsyncTask<String, Integer, List<FloorRow>>{

        @Override
        protected List<FloorRow> doInBackground(String... strings) {

            try{

                floorList = new ArrayList<>();
                loadFloors();

                for(FloorRow br:floorLoadList){

                    Log.d(TAG, "doInBackground: floorLoadList has " + floorLoadList.size() + " elements");
                    floorList.add(br);

                }
                Log.d(TAG, "doInBackground: floorList has " + floorList.size() + " elements");



            }catch(Exception e){
                e.printStackTrace();
            }

            running = false;
            return floorList;
        }

        @Override
        protected void onPostExecute(List<FloorRow> floorRows) {
            super.onPostExecute(floorRows);
            running = false;
        }
    }

    @Override
    public void onClick(View view) {
        int pos = floorListRecycler.getChildLayoutPosition(view);
        FloorRow br = floorList.get(pos);
        startPositionListActivity(br);
    }

    private void startPositionListActivity(FloorRow br){
        Intent PositionListIntent = new Intent(FloorListActivity.this, PositionListActivity.class);
        PositionListIntent.putExtra("Building",intent.getStringExtra("Building"));
        PositionListIntent.putExtra("Floor", br.getFloorName());
        PositionListIntent.putExtra("USER_ID", user_id);
        Log.d(TAG, "From FloorActivity Pasisng user_id: " + user_id);
        PositionListIntent.putExtra("TEST_POS_LIST",intent.getParcelableArrayListExtra("TEST_POS_LIST"));
        startActivity(PositionListIntent);
    }



    private void loadFloors(){
        Log.d(TAG, "loadFloors: Loading Floors from dummy db");

        floorLoadList = new ArrayList<FloorRow>();
        ArrayList<String> extractFloors = new ArrayList<>();

        Log.d(TAG, "loadFloors: testPositionsList has "+testPositionsList.size());
        for(TestPosition tP:testPositionsList){
            if(extractFloors.contains(String.valueOf(tP.getTest_loc_floor_id()))){
                Log.d(TAG, "loadFloors: " + tP.getTest_loc_floor_id() + " already in the list f buildings");
            }else if(tP.getBuilding_name().equals(building)&& !extractFloors.contains(String.valueOf(tP.getTest_loc_floor_id()))){
                Log.d(TAG, "loadFloors: added to extract floors Building " + tP.getBuilding_name() + "("+building+") floor " + tP.getTest_loc_floor_id());
                extractFloors.add(String.valueOf(tP.getTest_loc_floor_id()));
            }
        }



        for(String b:extractFloors){
            FloorRow br = new FloorRow(b);
            floorLoadList.add(br);
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































