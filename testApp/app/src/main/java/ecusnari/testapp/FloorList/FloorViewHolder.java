package ecusnari.testapp.FloorList;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import ecusnari.testapp.R;

/**
 * Created by Ericas on 19.10.2017.
 */

public class FloorViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "FloorViewHolder";
    public TextView floorName;

    public FloorViewHolder(View view){
        super(view);

        floorName = (TextView) view.findViewById(R.id.floorN);
        Log.d(TAG, "created holder -> " + floorName.getText().toString());


    }


}
