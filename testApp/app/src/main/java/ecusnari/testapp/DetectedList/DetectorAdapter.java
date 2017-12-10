package ecusnari.testapp.DetectedList;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ecusnari.testapp.DetectedList.DetectorActivity;
import ecusnari.testapp.DetectedList.DetectorRow;
import ecusnari.testapp.DetectedList.DetectorViewHolder;
import ecusnari.testapp.PositionList.PositionListActivity;
import ecusnari.testapp.R;

/**
 * Created by Ericas on 19.10.2017.
 */

public class DetectorAdapter extends RecyclerView.Adapter<DetectorViewHolder>{

    private static final String TAG = "DetectorAdapter";
    private List<DetectorRow> detectorList;
    private DetectorActivity parentActivity;


    public DetectorAdapter(List<DetectorRow> detectorList, DetectorActivity pa){
        this.detectorList = detectorList;
        parentActivity = pa;
    }

    @Override
    public DetectorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.d(TAG, "onCreateViewHolder: MAKING NEW");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detector_row, parent, false);

        itemView.setOnClickListener(parentActivity);
        return new DetectorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DetectorViewHolder holder, int position) {
        DetectorRow detectorRow = detectorList.get(position);
        Log.d(TAG, "onBindViewHolder: captured floorList item, pos: "+String.valueOf(position)+" -> "+detectorRow.toString());

        holder.signalNum.setText(detectorRow.getSignalNum());
        holder.majorText.setText(detectorRow.getMajorText());
        holder.minorText.setText(detectorRow.getMinorText());
        holder.rssiText.setText(detectorRow.getRSSI());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return detectorList.size();
    }
}























