package ecusnari.testapp;

/**
 * Created by Ericas on 15.11.2017.
 */

public class BeaconData {

    private int major;
    private int minor;
    private int RSSI;
    private String UUID;

    public BeaconData() {
        RSSI = 0;
        minor = 0;
    }
    public BeaconData(int major, int minor, int RSSI, String UUID){
        this.major = major;
        this.minor = minor;
        this.RSSI = RSSI;
        this.UUID = UUID;
    }

    public int getRSSI() {
        return RSSI;
    }

    public void setRSSI(int RSSI) {
        this.RSSI = RSSI;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }
}
