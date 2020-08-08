import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class RainfallAnalyser {

    // Total days/rows in CSV
    static int totalDays = 0;

    // Reference array of month strings for beautifying output
    static String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    // minMax is an array of each month (Jan(0) - Dec(11)) where index 0 is min and 1 is max rainfall values
    static double[][] minMax = new double[12][2];

    public static void main(String[] args) {
        // Element of row in file
        int columnIndex = 0;
        // Current month of active row
        int currentMonth = 0;
        // Current year of active row
        int currentYear = 0;

        try {
            // Open file and skip first line, set csv delimiter
            Scanner scanner = new Scanner(new File("src/main/resources/MountSheridanStationCNS.csv"));
            scanner.nextLine();
            scanner.useDelimiter(",");

            // While there is a next element in the file, parse each required column
            while(scanner.hasNext()) {
                totalDays += 1;
                String data = scanner.next();
                if(columnIndex == 2) {
                    // Wait until year changes before printing current lowest and highest values
                    int year = Integer.parseInt(data);
                    if(year != currentYear) {
                        if(currentYear != 0) {
                            for (int month = 0; month < MONTHS.length; month++) {
                                if(minMax[month][0] == 0.0) {
                                    minMax[month][0] = minMax[month][1];
                                }
                                System.out.println(fixedLengthString(MONTHS[month], 10) + "| Min: " + fixedLengthString(Double.toString(minMax[month][0]), 5) + " | Max: " + fixedLengthString(Double.toString(minMax[month][1]), 5));
                            }
                        }
                        currentYear = year;
                        System.out.println("\n" + currentYear + ": ");
                        for (int month = 0; month < MONTHS.length; month++) {
                            minMax[month] = new double[]{0.0, 0.0};
                        }
                    }
                } else if(columnIndex == 3) {
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
                        } else if (dailyRainfall < monthlyMinRainfall && dailyRainfall > 0.0 || monthlyMinRainfall == 0.0) {
                            minMax[currentMonth][0] = dailyRainfall;
                        }
                    }
                //If this is the last element in the row, reset column index counter
                } else if (columnIndex == 6) {
                    columnIndex = 0;
                    continue;
                }
                columnIndex++;
            }
            for (int month = 0; month < MONTHS.length; month++) {
                System.out.println(fixedLengthString(MONTHS[month], 10) + "| Min: " + fixedLengthString(Double.toString(minMax[month][0]), 5) + " | Max: " + fixedLengthString(Double.toString(minMax[month][1]), 5));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String fixedLengthString(String string, int length) {
        return String.format("%1$-"+length+ "s", string);
    }
}
