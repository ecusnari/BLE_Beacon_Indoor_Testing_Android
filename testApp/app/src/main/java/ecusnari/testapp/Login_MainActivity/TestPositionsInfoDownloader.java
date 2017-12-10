package ecusnari.testapp.Login_MainActivity;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Ericas on 30.10.2017.
 */

public class TestPositionsInfoDownloader extends AsyncTask<String, Void, String>{
    private final String TAG =  "TestPositionsInfoDwlr";

    private MainActivity ma;
    private String username;
    private String password;

    private final String URL = "https://api.iitrtclab.com/management/getTestPoints?";//username=testIIT&pass=1234

    public TestPositionsInfoDownloader(MainActivity ma, String username, String password){
        this.ma=ma;
        //passsed from the MainActicity, after user authenticated succesfully
        this.username = username;//already trimmed
        this.password = password;

    }


    @Override
    protected String doInBackground(String... strings) {


        String urlToUse = URL + "username="+username+"&pass="+password;
        StringBuilder sb = new StringBuilder();

        try{
            java.net.URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            if(conn.getResponseCode() == 400){
                return null;
            }
            while ((line = reader.readLine())!=null){
                sb.append(line).append('\n');
            }

        }catch(Exception e){
            e.printStackTrace();
            Log.d(TAG,"doInBackground: "+ e.getMessage());
            return null;
        }

        Log.d(TAG, "JSON returned content ==> "+sb.toString());
        return sb.toString();

    }

    @Override
    protected void onPostExecute(String s) {
        if(s == null){
            Toast.makeText(ma,"Test Positions Information Unavailable", Toast.LENGTH_LONG).show();
            ma.setTestPositionInfoList(null);
            return;
        }else if(s.equals("")){
            Toast.makeText(ma, "No Data Provided for this User", Toast.LENGTH_LONG).show();
            ma.setTestPositionInfoList(null);
            return;
        }

        ArrayList<TestPosition> testPositionList = new ArrayList<>();

        try{
            Log.d(TAG, "onPostExecute: s content -> \n " + s);

            JSONArray main = new JSONArray(s);
            Log.d(TAG, "onPostExecute: extraacted TestPositions -> " + main.length() );

            try{
                for(int i = 0; i< main.length();i++){
                    JSONObject object = (JSONObject)main.get(i);
                    TestPosition tPos = new TestPosition();
                    tPos.setBuilding_name(object.getString("bu_name"));
                    tPos.setBuilding_id(object.getInt("bu_id"));
                    tPos.setTest_loc_floor_id(object.getInt("test_loc_floor_id"));
                    tPos.setTest_loc_id(object.getString("test_loc_id"));
                    Log.d(TAG, "onPostExecute, got record nr."+i+", \n\t"+tPos.toString());

                    testPositionList.add(tPos);

                }

            }catch(Exception e) {
                e.printStackTrace();
                ma.setTestPositionInfoList(null);
                return;
            }

        }catch (Exception e){
            e.printStackTrace();
            ma.setTestPositionInfoList(null);
            return;
        }


        ma.setTestPositionInfoList(testPositionList);


    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        ma.setTestPositionInfoList(null);
    }
}















