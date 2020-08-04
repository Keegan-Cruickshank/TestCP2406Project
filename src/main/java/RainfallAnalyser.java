import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class RainfallAnalyser {

    // Total days/rows in CSV
    static int totalDays = 0;

    // Average rainfall over entire file
    static double averageRainfall;

    // Total rainfall over entire file
    static double totalRainfall = 0;

    // minMax is an array of each month (Jan(0) - Dec(11)) where index 0 is min and 1 is max rainfall values
    static double[][] minMax = new double[12][2];

    public static void main(String[] args) {
        // Element in each row
        int columnIndex = 0;
        // Current month of active row
        int currentMonth = 0;

        try {
            // Open file and skip first line, set csv delimiter
            Scanner scanner = new Scanner(new File("src/main/resources/MountSheridanStationCNS.csv"));
            scanner.nextLine();
            scanner.useDelimiter(",");

            // While there is a next element in the file, parse each required column
            while(scanner.hasNext()) {
                totalDays += 1;
                String data = scanner.next();
                if(columnIndex == 3) {
                    currentMonth = Integer.parseInt(data) - 1;
                } else if(columnIndex == 5) {
                    /*
                    Ensure element is not empty, then check if this could be a
                    min or max value against the months current lowest.
                    */
                    if (!(data.isEmpty())) {
                        double dailyRainfall = Double.parseDouble(data);
                        double monthlyMinRainfall = minMax[currentMonth][0];
                        double monthlyMaxRainfall = minMax[currentMonth][1];
                        if(dailyRainfall > monthlyMaxRainfall) {
                            minMax[currentMonth][1] = dailyRainfall;
                        } else if (dailyRainfall < monthlyMinRainfall) {
                            minMax[currentMonth][0] = dailyRainfall;
                        }
                        totalRainfall += dailyRainfall;
                    }
                //If this is the last element in the row, reset column index counter
                } else if (columnIndex == 6) {
                    columnIndex = 0;
                    continue;
                }
                columnIndex++;
            }
            scanner.close();
            averageRainfall = totalRainfall/totalDays;
            System.out.println(averageRainfall);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
