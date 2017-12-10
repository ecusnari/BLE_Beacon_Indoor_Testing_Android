package ecusnari.testapp.PositionList;

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

public class PositionAdapter extends RecyclerView.Adapter<PositionViewHolder>{

    private static final String TAG = "PositionAdapter";
    private List<PositionRow> positionList;
    private PositionListActivity parentActivity;


    public PositionAdapter(List<PositionRow> positionList, PositionListActivity ba){
        this.positionList = positionList;
        parentActivity = ba;
    }

    @Override
    public PositionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.d(TAG, "onCreateViewHolder: MAKING NEW");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.position_row, parent, false);

        itemView.setOnClickListener(parentActivity);
        return new PositionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PositionViewHolder holder, int position) {
        PositionRow positionRow = positionList.get(position);
        Log.d(TAG, "onBindViewHolder: captured positionList item, pos: "+String.valueOf(position)+" -> "+positionRow.getPositionID().toString());


        holder.positionID.setText(positionRow.getPositionID());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return positionList.size();
    }
}























