package rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.summary;

import lombok.Data;

@Data
public class MonthlyData {
    private int reservationsCount;
    private double totalProfit;

    public MonthlyData(int reservationsCount, double totalProfit) {
        this.reservationsCount = reservationsCount;
        this.totalProfit = totalProfit;
    }
}