package ecusnari.testapp.PositionList;

/**
 * Created by Ericas on 19.10.2017.
 */

public class PositionRow {

    private String positionID;



    private static int ctr = 1;

    //debug purposes
    public PositionRow(String positionID){
        this.positionID = positionID;
    }

    public String getPositionID() {
        return positionID;
    }

    public void setPositionID(String positionID) {
        this.positionID = positionID;
    }



    @Override
    public String toString() {
        return "PositionRow{" +
                "positionID='" + positionID + '\'' + '}';
    }
}
