package ecusnari.testapp.BuildingList;

/**
 * Created by Ericas on 19.10.2017.
 */

public class BuildingRow {

    private String buildingName;

    private static int ctr = 1;

    //debug purposes
    public BuildingRow(String buildingName){
        this.buildingName = buildingName;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    @Override
    public String toString() {
        return "Building " + buildingName + "\n";
    }
}
