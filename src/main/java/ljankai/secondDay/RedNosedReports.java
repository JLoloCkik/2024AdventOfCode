package ljankai.secondDay;

import java.io.*;

public class RedNosedReports {
    public static void main(String[] args) {
        // A fájl elérési útvonala
        int safe = 0;
        int count = 0;
        String filePath = "./src/main/java/ljankai/secondDay/data";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                // A sort feldaraboljuk számokká és tömbbé alakítjuk
                int[] row = parseLineToIntArray(line);
                // Meghívjuk a metódust az adott sor feldolgozására
                safe = safe + partOne(row);
                count = count + partTwo(row);
            }

        } catch (IOException e) {
            System.out.println("Hiba történt a fájl olvasása közben: " + e.getMessage());
        }
        System.out.println(safe);
        System.out.println(count);

    }


    public static int partOne(int[] row) {
        boolean isSafe = true;
        boolean isIncreasing = row[1] > row[0];
        int lastNum = row[0];


        for (int i = 1; i < row.length; i++) {
            int diff = row[i] - lastNum;

            if (Math.abs(diff) < 1 || Math.abs(diff) > 3) {
                isSafe = false;
                break;
            }

            // Ellenőrizzük a növekvő vagy csökkenő mintát
            if ((isIncreasing && diff < 0) || (!isIncreasing && diff > 0)) {
                isSafe = false;
                break;
            }

            lastNum = row[i]; //
        }

        if (isSafe) {
            return 1;
        } else {
            return 0;
        }
    }
    private static int partTwo(int[] row){
        int safeCount = 0;

        for (int removeIndex = 0; removeIndex < row.length; removeIndex++) {
            int[] newRow = new int[row.length - 1];

            int newIndex = 0;
            for (int i = 0; i < row.length; i++) {
                if (i != removeIndex) {
                    newRow[newIndex++] = row[i];
                }
            }

            if (isSafe(newRow)) {
                safeCount++;
                break;
            }
        }

        return safeCount;
    }



    private static boolean isSafe(int[] row) {
        boolean isSafe = true;
        boolean isIncreasing = row[1] > row[0]; // Növekvő-e a sor?
        int lastNum = row[0];

        for (int i = 1; i < row.length; i++) {
            int diff = row[i] - lastNum;


            if (Math.abs(diff) < 1 || Math.abs(diff) > 3) {
                isSafe = false;
                break;
            }

            if ((isIncreasing && diff < 0) || (!isIncreasing && diff > 0)) {
                isSafe = false;
                break;
            }

            lastNum = row[i];
        }

        return isSafe;
    }

    private static int[] parseLineToIntArray (String line){
        String[] parts = line.split("\\s+"); // Szóközökkel elválasztva
        int[] row = new int[parts.length];

        for (int i = 0; i < parts.length; i++) {
            row[i] = Integer.parseInt(parts[i]);
        }
        return row;
    }
}
