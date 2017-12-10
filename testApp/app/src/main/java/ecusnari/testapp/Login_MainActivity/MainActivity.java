package ecusnari.testapp.Login_MainActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import ecusnari.testapp.BluetoothChecker;
import ecusnari.testapp.BuildingList.BuildingListActivity;
import ecusnari.testapp.R;

import static java.lang.Thread.sleep;



public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final String TOKEN = "1234";

    ArrayList<TestPosition> testPositionArrayList = new ArrayList<>();
    private boolean testPositionsAreDownloading = false;
    private boolean loginPositionsState = true;
    BluetoothChecker bluetooth;

    //Views
    private EditText usernameT ;
    private EditText pssdT ;
    private Button loginB;
    private Button signUpButton;

    //Set string for credential
    private String username_text;
    private String pssd_text;
    private int user_id_retrieved;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_page);

        //Bluetooth turn on
        bluetooth=new BluetoothChecker(getApplicationContext());
        bluetooth.enableBluetooth();

    }


    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();

        BindViews();
        loginB.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                processLogin(usernameT, pssdT);
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                alertUnimplementedFeature();
            }
        });
    }

    public void BindViews(){
        usernameT = (EditText) findViewById(R.id.username);
        pssdT = (EditText) findViewById(R.id.password);
        loginB = (Button) findViewById(R.id.login_button);
        signUpButton = (Button) findViewById(R.id.signup_button);
    }

    public boolean checkIfUsernameOrPssdEmpty(String username_text, String pssd_text){
        if(username_text.isEmpty() || username_text.length()==0 || username_text.equals("")||pssd_text.isEmpty() || pssd_text.length()==0 || pssd_text.equals("")){
            return true;
        }
        return false;
    }

    public void getUsernameAndPssdFromInput(EditText username, EditText password){
        username_text = username.getText().toString().trim();
        pssd_text = password.getText().toString().trim();
    }


    public void authenticateUser(String username, String password){
        Log.d(TAG, "DownloadUserLoginResponse: starting to download Json for retrieving test positions");
        LoginInfoDownloader downloader = new LoginInfoDownloader(this, username, password);
        downloader.execute();

    }

    public void startBuildingListActivity(){
        Intent intentBuildingListAct = new Intent(MainActivity.this, BuildingListActivity.class);
        intentBuildingListAct.putExtra("USER_ID",user_id_retrieved);
        Log.d(TAG, "From MainActivity Pasisng user_id: " + user_id_retrieved);
        intentBuildingListAct.putExtra("TEST_POS_LIST", testPositionArrayList);

        startActivity(intentBuildingListAct);
        finish();
        Log.d(TAG, "Going to BuildingListActivity");
    }

    public void alertErrorDownloadingPositionInfo(){
        AlertDialog.Builder noDataDialog = new AlertDialog.Builder(this);
        noDataDialog.setTitle("No Data for this User");
        noDataDialog.setMessage("Data cannot be accessed/loaded for this username. Contact davids@iit.edu for help.");
        AlertDialog dialog = noDataDialog.create();
        dialog.show();
    }

    private void alertUnimplementedFeature(){
        AlertDialog.Builder noDataDialog = new AlertDialog.Builder(this);
        noDataDialog.setTitle("Feature Not Implemented");
        noDataDialog.setMessage("This feature is not implemented yet.");
        AlertDialog dialog = noDataDialog.create();
        dialog.show();
    };

    public void alertDialogueErrorAuthentication(){
        AlertDialog.Builder noDataDialog = new AlertDialog.Builder(this);
        noDataDialog.setTitle("Authentication Failed");
        noDataDialog.setMessage("Wrong username or password. Try again or contact davids@iit.edu for help.");
        AlertDialog dialog = noDataDialog.create();
        dialog.show();
    }

    public void processLogin(EditText username, EditText password){
        Log.d(TAG, "processLogin");

        getUsernameAndPssdFromInput(username, password);
        if(checkIfUsernameOrPssdEmpty(username_text, pssd_text)){
            Toast.makeText(MainActivity.this, "Enter a Username and Password", Toast.LENGTH_SHORT).show();
            return;
        }else {
            authenticateUser(username_text, pssd_text);
        }
    }

    public void setTestPositionInfoList(ArrayList<TestPosition> testPositions){
        Log.d(TAG, "setTestPositionInfoList");

        if(testPositions==null || testPositions.isEmpty()){
            Log.d(TAG, "setTestPositionInfoList: returned list is null");
            alertErrorDownloadingPositionInfo();

        }else{
            Log.d(TAG, "setTestPositionInfoList: array of test positions non-null");
            testPositionArrayList.addAll(testPositions);
            Log.d(TAG, "setTestPositionInfoList, testPositionArrayList got " + testPositionArrayList.size() + " elements.");
            startBuildingListActivity();
        }
    }

    public void DownloadTestPosInfoList(String username, String token){

        Log.d(TAG, "DownloadTestPosInfoList: starting to download Json for retrieving test positions");
        TestPositionsInfoDownloader downloader = new TestPositionsInfoDownloader(this, username, token);
        downloader.execute();

    }



    public void setAuthenticationResponse(boolean authenticated, String reason, int user_id){

        if(authenticated == true && user_id != 0 && reason == null){
            Log.d(TAG, "setAuthenticationResponce: authentication success for user_id " + user_id);
            this.user_id_retrieved = user_id;
            DownloadTestPosInfoList(username_text, TOKEN);
        }else if (authenticated == false && reason!=null){
            Log.d(TAG, "setAuthenticationResponce: authentication failed with the reason: " + reason);
            alertDialogueErrorAuthentication();
        }else{
            Log.d(TAG, "setAuthenticationResponce: authentication failed for unknown reason");

        }
    }


}
