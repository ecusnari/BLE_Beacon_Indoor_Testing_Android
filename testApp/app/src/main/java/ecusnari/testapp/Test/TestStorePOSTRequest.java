package ecusnari.testapp.Test;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ecusnari.testapp.Login_MainActivity.MainActivity;

/**
 * Created by Ericas on 10.12.2017.
 */

public class TestStorePOSTRequest extends AsyncTask<String, Void, String>{

    private final String TAG =  "TestStorePOSTRequest";
    private final String TOKEN = "1234";

    private TestActivity ta;
    private JSONObject postJson;
    boolean data_stored = false;

    private final String URL = "https://api.iitrtclab.com/management/storeTestData?pass="+TOKEN;

    public TestStorePOSTRequest(TestActivity ta, JSONObject postJson){
        this.ta=ta;
        this.postJson = postJson;
    }


    @Override
    protected String doInBackground(String... strings) {

        StringBuilder sb = new StringBuilder();


        try{
            Log.d(TAG, "postJson to be POSTed --> " + postJson.toString());

            java.net.URL url = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");
            connection.setRequestProperty("Host", "api.iitrtclab.com");
            //connection.connect();



            OutputStream jsonPostStream = connection.getOutputStream();
            jsonPostStream.write(postJson.toString().getBytes());
            Log.d(TAG, "outputStream: --> " + postJson.toString());
            jsonPostStream.flush();
            jsonPostStream.close();

            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            if(connection.getResponseCode() == 400){
                return null;
            }
            while ((line = reader.readLine())!=null){
                sb.append(line).append('\n');
            }
            connection.disconnect();
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
            ta.alertTestFailed();
            return;
        }else if(s.equals("")){
            ta.alertTestFailed();
            return;
        }



        try{
            Log.d(TAG, "onPostExecute: s content -> \n " + s);

            JSONObject objectExtracted = new JSONObject(s);

            Log.d(TAG, "onPostExecute: extraacted objects -> " + objectExtracted.length() );

            try{

                data_stored = objectExtracted.getBoolean("data_stored");
                if(data_stored == false){
                    Log.d(TAG,"onPostExecute: data storage failed");
                    ta.alertTestFailed();
                }else{
                    Log.d(TAG,"onPostExecute: data storage succeded");
                    ta.testStorageResponse();
                }
            }catch(Exception e) {
                e.printStackTrace();
                ta.alertTestFailed();
                return;
            }

        }catch (Exception e){
            e.printStackTrace();
            ta.testStorageResponse();
            return;
        }
    }

    @Override
    protected void onCancelled() {
        if(data_stored){
            ta.testStorageResponse();
        }else
            ta.alertTestFailed();
        super.onCancelled();
    }

}
