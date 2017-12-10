package ecusnari.testapp.DetectedList;

/**
 * Created by Ericas on 19.10.2017.
 */

public class DetectorRow {

    private String signalNum;
    private String majorText;
    private String minorText;
    private String RSSI;

    private static int ctr = 1;

    public DetectorRow(String signalNum, String majorText, String minorText, String rssiText) {
        this.signalNum = signalNum + ".";
        this.majorText = majorText;
        this.minorText = minorText;
        this.RSSI = rssiText;
    }

    public String getMajorText() {
        return majorText;
    }

    public void setMajorText(String majorText) {
        this.majorText = majorText;
    }

    public String getMinorText() {
        return minorText;
    }

    public void setMinorText(String minorText) {
        this.minorText = minorText;
    }

    public String getRSSI() {
        return RSSI;
    }

    public void setRSSI(String RSSI) {
        this.RSSI = RSSI;
    }

    public String getSignalNum() {
        return signalNum;
    }

    public void setSignalNum(String signalNum) {
        this.signalNum = signalNum;
    }

    @Override
    public String toString() {
        return "DetectorRow{" +
                "majorText='" + majorText + '\'' +
                ", minorText='" + minorText + '\'' +
                ", RSSI='" + RSSI + '\'' +
                '}';
    }
}
