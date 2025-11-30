package org.example.model;

public class CompanyStatistics {
    private final String companyName;
    private final int employeeCount;
    private final double averageSalary;
    private final String highestPaidFullName;

    public CompanyStatistics(String companyName, int employeeCount, double averageSalary, String highestPaidFullName) {
        this.companyName = companyName;
        this.employeeCount = employeeCount;
        this.averageSalary = averageSalary;
        this.highestPaidFullName = highestPaidFullName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public int getEmployeeCount() {
        return employeeCount;
    }

    public double getAverageSalary() {
        return averageSalary;
    }

    public String getHighestPaidFullName() {
        return highestPaidFullName;
    }

    @Override
    public String toString() {
        return "Firma: " + companyName +
                ", Pracowników: " + employeeCount +
                ", Średnie wynagrodzenie: " + averageSalary +
                ", Najlepiej opłacany: " + highestPaidFullName;
    }

}
