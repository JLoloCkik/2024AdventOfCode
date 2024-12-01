package ljankai.firstDay;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class HistorianHysteria {
    public static void main(String[] args) {
        String fileName = "./src/main/java/ljankai/firstDay/data";
        ArrayList<Integer> leftSide = new ArrayList<>();
        ArrayList<Integer> rightSide = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+"); // Soron belüli értékek elválasztása
                if (parts.length == 2) { // Ellenőrizzük, hogy pontosan két szám van-e
                    leftSide.add(Integer.parseInt(parts[0])); // Bal oldali szám
                    rightSide.add(Integer.parseInt(parts[1])); // Jobb oldali szám
                }
            }
        } catch (IOException e) {
            System.err.println("Hiba a fájl beolvasása közben: " + e.getMessage());
        }
        partOne(leftSide,rightSide);
        partTwo(leftSide,rightSide);
    }
    public static void partOne(ArrayList<Integer> leftSide, ArrayList<Integer> rightSide) {
        int sum = 0;

        Collections.sort(leftSide);
        Collections.sort(rightSide);

        for (int i = 0; i < leftSide.size(); i++) {
            sum += Math.abs(leftSide.get(i) - rightSide.get(i));
        }

        System.out.println(sum);
    }
    public static void partTwo(ArrayList<Integer> leftSide, ArrayList<Integer> rightSide) {
        int sum = 0;
        int product = 0;

        for (int i = 0; i < leftSide.size(); i++) {
            int count = Collections.frequency(rightSide, leftSide.get(i));
            product = leftSide.get(i);
            sum += product * count;
        }

        System.out.println(sum);
    }
}