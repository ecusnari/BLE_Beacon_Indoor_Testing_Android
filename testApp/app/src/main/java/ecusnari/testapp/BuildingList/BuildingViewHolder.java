package ecusnari.testapp.BuildingList;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import ecusnari.testapp.R;

/**
 * Created by Ericas on 19.10.2017.
 */

public class BuildingViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "BuildingViewHolder";
    public TextView buildingName;

    public BuildingViewHolder(View view){
        super(view);

        buildingName = (TextView) view.findViewById(R.id.buildingName);
        Log.d(TAG, "created holder -> " + buildingName.getText().toString());


    }


}
