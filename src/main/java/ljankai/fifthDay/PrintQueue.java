package ljankai.fifthDay;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PrintQueue  {
    public static void main(String[] args) {
        String filename = "./src/main/java/ljankai/fifthDay/data";
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Hiba történt a fájl beolvasása közben: " + e.getMessage());
        }


        partOne(lines);
    }

    private static void partOne(List<String> lines) {
        List<String> rules = new ArrayList<>();
        List<List<Integer>> updates = new ArrayList<>();

        boolean isRules = true;

        for (String line : lines) {
            if (line.isEmpty()) {
                isRules = false;
                continue;
            }
            if (isRules) {
                rules.add(line);
            } else {
                List<Integer> update = new ArrayList<>();
                for (String num : line.split(",")) {
                    update.add(Integer.parseInt(num));
                }
                updates.add(update);
            }
        }

        List<int[]> parsedRules = new ArrayList<>();
        for (String rule : rules) {
            String[] parts = rule.split("\\|");
            parsedRules.add(new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])});
        }


        int middleSum = 0;
        int partSum = 0;

        for (List<Integer> update : updates) {
            if (isCorrectOrder(update, parsedRules)) {
                middleSum += getMiddleElement(update);
            } else {
                List<Integer> correctedUpdate = reorderUpdate(update, parsedRules);
                partSum += getMiddleElement(correctedUpdate);
            }
        }

        System.out.println("Jó sorrend: " + middleSum);
        System.out.println("Rossz sorrend: " + partSum);
    }

    private static boolean isCorrectOrder(List<Integer> update, List<int[]> rules) {
        for (int[] rule : rules) {
            int a = rule[0], b = rule[1];
            if (update.contains(a) && update.contains(b)) {
                if (update.indexOf(a) > update.indexOf(b)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static int getMiddleElement(List<Integer> update) {
        return update.get(update.size() / 2);
    }

    private static List<Integer> reorderUpdate(List<Integer> update, List<int[]> rules) {
        List<Integer> correctedUpdate = new ArrayList<>(update);
        boolean sorted = false;

        while (!sorted) {
            sorted = true;
            for (int[] rule : rules) {
                int a = rule[0], b = rule[1];
                if (correctedUpdate.contains(a) && correctedUpdate.contains(b)) {
                    if (correctedUpdate.indexOf(a) > correctedUpdate.indexOf(b)) {
                        correctedUpdate.remove(Integer.valueOf(a));
                        correctedUpdate.add(correctedUpdate.indexOf(b), a);
                        sorted = false;
                    }
                }
            }
        }
        return correctedUpdate;
    }
}
