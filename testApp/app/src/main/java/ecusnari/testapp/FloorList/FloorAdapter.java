package ecusnari.testapp.FloorList;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ecusnari.testapp.R;

/**
 * Created by Ericas on 19.10.2017.
 */

public class FloorAdapter extends RecyclerView.Adapter<FloorViewHolder>{

    private static final String TAG = "FloorAdapter";
    private List<FloorRow> floorList;
    private FloorListActivity parentActivity;


    public FloorAdapter(List<FloorRow> floorList, FloorListActivity ba){
        this.floorList = floorList;
        parentActivity = ba;
    }

    @Override
    public FloorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.d(TAG, "onCreateViewHolder: MAKING NEW");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.floor_row, parent, false);

        itemView.setOnClickListener(parentActivity);
        return new FloorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FloorViewHolder holder, int position) {
        FloorRow floorRow = floorList.get(position);
        Log.d(TAG, "onBindViewHolder: captured floorList item, pos: "+String.valueOf(position)+" -> "+floorRow.getFloorName().toString());


        holder.floorName.setText(floorRow.getFloorName());

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return floorList.size();
    }
}























