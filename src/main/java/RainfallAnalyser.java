import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class RainfallAnalyser {

    static int totalDays = 0;
    static double averageRainfall;
    static double totalRainfall = 0;
    static double[][] minMax = {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}};

    public static void main(String[] args) {
        int index = 0;
        int currentMonth = 0;
        try {
            Scanner scanner = new Scanner(new File("src/main/resources/MountSheridanStationCNS.csv"));
            scanner.nextLine();
            scanner.useDelimiter(",");
            while(scanner.hasNext()) {
                totalDays += 1;
                String data = scanner.next();
                if(index == 3) {
                    currentMonth = Integer.parseInt(data) - 1;
                } else if(index == 5) {
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
                } else if (index == 6) {
                    index = 0;
                    continue;
                }
                index++;
            }
            scanner.close();
            averageRainfall = totalRainfall/totalDays;
            System.out.println(averageRainfall);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
