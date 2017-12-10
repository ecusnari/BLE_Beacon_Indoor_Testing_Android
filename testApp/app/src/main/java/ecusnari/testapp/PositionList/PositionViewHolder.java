package ecusnari.testapp.PositionList;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import ecusnari.testapp.R;

/**
 * Created by Ericas on 19.10.2017.
 */

public class PositionViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "PositionViewHolder";
    public TextView positionID;

    public PositionViewHolder(View view){
        super(view);

        positionID = (TextView) view.findViewById(R.id.test_loc_id_View);
        Log.d(TAG, "created holder -> " + positionID.getText().toString());


    }


}
