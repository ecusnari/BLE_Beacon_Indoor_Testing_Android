package ecusnari.testapp.BuildingList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ecusnari.testapp.FloorList.FloorListActivity;
import ecusnari.testapp.Login_MainActivity.MainActivity;
import ecusnari.testapp.Login_MainActivity.TestPosition;
import ecusnari.testapp.R;

/**
 * Created by Ericas on 19.10.2017.
 */

public class BuildingListActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "BuildingListActivity";

    private List<BuildingRow> buildingList = new ArrayList<>();
    private List<BuildingRow> buildingLoadList;
    private BuildingAdapter bAdapter;
    private RecyclerView buildingListRecycler;
    private ArrayList<TestPosition> testPositionsList = new ArrayList<TestPosition>();
    private int user_id;
    private Intent intent;

    private boolean running;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.building_list);
    }


    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();

        //setup recyclerView for building list
        buildingListRecycler = (RecyclerView) findViewById(R.id.buildingListRecycler);

        intent = getIntent();
        if(intent.hasExtra("USER_ID")){
            user_id = intent.getIntExtra("USER_ID",-1);
            Log.d(TAG, "onResume: passed extraUsername -> " + user_id);

        }

        if(intent.hasExtra("TEST_POS_LIST")){
            Log.d(TAG, "onResume, got the passed the testPositionLists to BuildingListActivity");
            testPositionsList = intent.getParcelableArrayListExtra("TEST_POS_LIST");
            Log.d(TAG, "onResume: received from past activity "+testPositionsList.size()+" test positions");
        }



        //flag used for making onResume wait for MyAsyncTask to finish
        running = true;
        new MyAsyncTask().execute();

        while(running){
            Log.d(TAG, "onResume.running");

        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        buildingListRecycler.setLayoutManager(linearLayoutManager);

        bAdapter = new BuildingAdapter(buildingList, this);
        Log.d(TAG, "onResume: buildingList content passed to new adapter, size "+buildingList.size()+"\n");
        buildingListRecycler.setAdapter(bAdapter);
        Log.d(TAG,"onResume: refresh adapter");




    }


    class MyAsyncTask extends AsyncTask<String, Integer, List<BuildingRow>>{

        @Override
        protected List<BuildingRow> doInBackground(String... strings) {

            try{

                buildingList = new ArrayList<>();
                loadBuildings();

                for(BuildingRow br:buildingLoadList){

                    Log.d(TAG, "doInBackground: buildingLoadList has " + buildingLoadList.size() + " elements");
                    buildingList.add(br);

                }
                Log.d(TAG, "doInBackground: buildingList has " + buildingList.size() + " elements");



            }catch(Exception e){
                e.printStackTrace();
            }

            running = false;
            return buildingList;
        }

        @Override
        protected void onPostExecute(List<BuildingRow> buildingRows) {
            super.onPostExecute(buildingRows);
            running = false;

        }
    }

    @Override
    public void onClick(View view) {
        int pos = buildingListRecycler.getChildLayoutPosition(view);
        BuildingRow br = buildingList.get(pos);

        Log.d(TAG, "Building clicked -> "+ br.toString());

        Intent FloorIntent = new Intent(BuildingListActivity.this, FloorListActivity.class);
        FloorIntent.putExtra("Building",br.getBuildingName());
        FloorIntent.putExtra("USER_ID", user_id);
        Log.d(TAG, "From BuildingActivity Pasisng user_id: " + user_id);
        FloorIntent.putExtra("TEST_POS_LIST",intent.getParcelableArrayListExtra("TEST_POS_LIST"));

        startActivity(FloorIntent);

    }
    public void loadBuildings(){
        Log.d(TAG, "loadBuildings: Loading Buildings from previous activity passed list of test positions");

        buildingLoadList = new ArrayList<BuildingRow>();
        ArrayList<String> extractBuildings = new ArrayList<>();

        //get list of testPositions downloaded in MainActivity at user Autenthication

        Log.d(TAG, "loadBuildings: testPositionsList has "+testPositionsList.size());
        for(TestPosition tP:testPositionsList){
            if(extractBuildings.contains(tP.getBuilding_name())){
                Log.d(TAG, "loadBuildings: " + tP.getBuilding_name() + " already in the list f buildings");
            }else{
                extractBuildings.add(tP.getBuilding_name());
            }
        }

        Log.d(TAG, "loadBuildings: extractBuildings has " + extractBuildings.size());

        for(String b:extractBuildings){
            BuildingRow br = new BuildingRow(b);
            buildingLoadList.add(br);
        }

        Log.d(TAG, "loadBuildings: buildingLoadList has " + buildingList.size());
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
        Intent returnToLogin = new Intent(BuildingListActivity.this, MainActivity.class);
        startActivity(returnToLogin);
        finish();
    }
}































