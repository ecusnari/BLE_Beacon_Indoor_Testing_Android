package ecusnari.testapp.Login_MainActivity;

import android.os.Parcel;
import android.os.Parcelable;

import static java.lang.System.out;


/**
 * Created by Ericas on 30.10.2017.
 */

public class TestPosition implements Parcelable{
    private String building_name;
    private int building_id;
    private int test_loc_floor_id;
    private String test_loc_id;

    public TestPosition() {
        this.building_name = "";
        this.building_id = -1;
        this.test_loc_floor_id = -1;
        this.test_loc_id = "";
    }

    public TestPosition(String building_name, int building_id, int test_loc_floor_id, String test_loc_id) {
        this.building_name = building_name;
        this.building_id = building_id;
        this.test_loc_floor_id = test_loc_floor_id;
        this.test_loc_id = test_loc_id;
    }

    public String getBuilding_name() {
        return building_name;
    }

    public void setBuilding_name(String building_name) {
        this.building_name = building_name;
    }

    public int getBuilding_id() {
        return building_id;
    }

    public void setBuilding_id(int building_id) {
        this.building_id = building_id;
    }

    public int getTest_loc_floor_id() {
        return test_loc_floor_id;
    }

    public void setTest_loc_floor_id(int test_loc_floor_id) {
        this.test_loc_floor_id = test_loc_floor_id;
    }

    public String getTest_loc_id() {
        return test_loc_id;
    }

    public void setTest_loc_id(String test_loc_id) {
        this.test_loc_id = test_loc_id;
    }

    @Override
    public String toString() {
        return "TestPosition{" +
                "building_name='" + building_name + '\'' +
                ", building_id=" + building_id +
                ", test_loc_floor_id=" + test_loc_floor_id +
                ", test_loc_id='" + test_loc_id + '\'' +
                '}';
    }

    public TestPosition(Parcel in){
        String[] data = new String[4];

        in.readStringArray(data);
        this.building_name = data[0];
        this.building_id = Integer.valueOf(data[1]);
        this.test_loc_floor_id = Integer.valueOf(data[2]);
        this.test_loc_id = data[3];

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeStringArray(new String[] {
                this.building_name,
                String.valueOf(this.building_id),
                String.valueOf(this.test_loc_floor_id),
                this.test_loc_id
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<TestPosition>(){
        public TestPosition createFromParcel(Parcel in){
            return new TestPosition(in);
        }
        public TestPosition[] newArray(int size){
            return new TestPosition[size];
        }
    };
}
