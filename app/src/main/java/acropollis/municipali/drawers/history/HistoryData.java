package acropollis.municipali.drawers.history;

import lombok.Data;

@Data
public class HistoryData {
    private double rate;
    private long date;

    public HistoryData(double rate, long date) {
        this.rate = rate;
        this.date = date;
    }
}