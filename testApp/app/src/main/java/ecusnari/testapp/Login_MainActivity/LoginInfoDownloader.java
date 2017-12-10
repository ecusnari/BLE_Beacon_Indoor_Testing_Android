package ecusnari.testapp.Login_MainActivity;

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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Ericas on 30.10.2017.
 */

public class LoginInfoDownloader extends AsyncTask<String, Void, String>{
    private final String TAG =  "LoginInfoDownloader";
    private final String TOKEN = "1234";

    private MainActivity ma;
    private String username;
    private String password;

    private final String URL = "https://api.iitrtclab.com/management/login?pass="+TOKEN;

    public LoginInfoDownloader(MainActivity ma, String username, String password){
        this.ma=ma;

        //passsed from the MainActicity, after user clicked "Log in"
        this.username = username;//already trimmed
        this.password = password;
    }


    @Override
    protected String doInBackground(String... strings) {

        StringBuilder sb = new StringBuilder();


        try{
            JSONObject postJson = new JSONObject();
            postJson.put("username",username);
            postJson.put("password", SHA256());
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
            ma.alertDialogueErrorAuthentication();
            return;
        }else if(s.equals("")){
            ma.alertDialogueErrorAuthentication();
            return;
        }

        int user_id = -1;
        String reason = null;
        boolean authenticated = false;

        try{
            Log.d(TAG, "onPostExecute: s content -> \n " + s);

            JSONObject objectExtracted = new JSONObject(s);

            Log.d(TAG, "onPostExecute: extraacted objects -> " + objectExtracted.length() );

            try{

                authenticated = objectExtracted.getBoolean("authenticated");
                if(authenticated == false){
                    Log.d(TAG,"onPostExecute: authentication failed");
                    reason = objectExtracted.getString("reason");
                    ma.setAuthenticationResponse(authenticated,reason,user_id);
                }else{
                    Log.d(TAG,"onPostExecute: authentication succeded");
                    user_id = objectExtracted.getInt("user_id");
                    ma.setAuthenticationResponse(authenticated,reason,user_id);
                }
                Log.d(TAG, "onPostExecute, got record nr.");


            }catch(Exception e) {
                e.printStackTrace();
                ma.setAuthenticationResponse(authenticated,reason,user_id);
                return;
            }

        }catch (Exception e){
            e.printStackTrace();
            ma.setAuthenticationResponse(authenticated,reason,user_id);
            return;
        }

        Log.d(TAG, "onPostExecute: set to false here");



    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

    }

    private String SHA256(){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return convertHashToString(hash);
        }catch(Exception e){
            e.printStackTrace();
            return " ";
        }

    }

    private String convertHashToString(byte[] hash){
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i< hash.length;i++){
            hexString.append(Integer.toString((hash[i]&0xff) + 0x100,16).substring(1));
        }

        return hexString.toString();
    }
}















