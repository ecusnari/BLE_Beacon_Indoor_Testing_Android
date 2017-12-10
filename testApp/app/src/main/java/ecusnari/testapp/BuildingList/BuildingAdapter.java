package ecusnari.testapp.BuildingList;

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

public class BuildingAdapter extends RecyclerView.Adapter<BuildingViewHolder>{

    private static final String TAG = "BuildingAdapter";
    private List<BuildingRow> buildingList;
    private BuildingListActivity parentActivity;


    public BuildingAdapter(List<BuildingRow> buildingList, BuildingListActivity ba){
        this.buildingList = buildingList;
        parentActivity = ba;
    }

    @Override
    public BuildingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.d(TAG, "onCreateViewHolder: MAKING NEW");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.building_row, parent, false);

        itemView.setOnClickListener(parentActivity);
        return new BuildingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BuildingViewHolder holder, int position) {
        BuildingRow buildingRow = buildingList.get(position);
        Log.d(TAG, "onBindViewHolder: captured buildingList item, pos: "+String.valueOf(position)+" -> "+buildingRow.getBuildingName().toString());


        holder.buildingName.setText(buildingRow.getBuildingName());

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return buildingList.size();
    }
}























