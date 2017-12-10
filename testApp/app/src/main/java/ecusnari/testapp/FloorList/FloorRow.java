package ecusnari.testapp.FloorList;

/**
 * Created by Ericas on 19.10.2017.
 */

public class FloorRow {

    private String floorName;

    private static int ctr = 1;

    //debug purposes
    public FloorRow(String floorName){
        this.floorName = floorName;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    @Override
    public String toString() {
        return "Floor " + floorName + "\n";
    }
}
