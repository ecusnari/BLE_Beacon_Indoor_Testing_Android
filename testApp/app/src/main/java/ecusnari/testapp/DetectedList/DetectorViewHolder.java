package ecusnari.testapp.DetectedList;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import ecusnari.testapp.R;

/**
 * Created by Ericas on 19.10.2017.
 */

public class DetectorViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "DetectorViewHolder";
    public TextView signalNum;
    public TextView majorText;
    public TextView minorText;
    public TextView rssiText;

    public DetectorViewHolder(View view){
        super(view);

        signalNum = (TextView) view.findViewById(R.id.signalNumber);
        majorText = (TextView) view.findViewById(R.id.majorNum);
        minorText = (TextView) view.findViewById(R.id.minorNum);
        rssiText = (TextView) view.findViewById(R.id.RSSINum);
        Log.d(TAG, "created holder -> major: " + majorText.getText().toString() + ", minor: " + minorText.getText().toString() + "RSSI: " + rssiText.getText());
    }


}
