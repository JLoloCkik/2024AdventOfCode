package ljankai.Day7;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.lang.Long.parseLong;

public class BridgeRepair {
    public static void main(String[] args) {
        String filename = "./src/main/java/ljankai/Day7/data";
        List<Line> equations = readFile(filename);
        System.out.println("Part 1 Calibration Result: " + calculateCalibrationResult(equations, false));
        System.out.println("Part 2 Calibration Result: " + calculateCalibrationResult(equations, true));
    }

    public record Line(long target, List<Long> numbers) {}

    private static List<Line> readFile(String filename) {
        List<Line> equations = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                long testValue = Long.parseLong(parts[0].trim());
                List<Long> numList = Arrays.stream(parts[1].trim().split(" "))
                        .map(Long::parseLong)
                        .toList();
                equations.add(new Line(testValue, numList));
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return equations;
    }

    private static long calculateCalibrationResult(List<Line> equations, boolean inclConcat) {
        long total = 0;

        for (Line line : equations) {
            boolean canBeTrue = canProduceTarget(line.target, line.numbers, 0, line.numbers.getFirst(), inclConcat);
            if (canBeTrue) {
                total += line.target;
            }
        }
        return total;
    }

    private static boolean canProduceTarget(long target, List<Long> numbers, int index, long current, boolean inclConcat) {
        index++;
        if (index >= numbers.size()) {
            return current == target;
        }

        long nextNum = numbers.get(index);
        boolean result = canProduceTarget(target, numbers, index, current + nextNum, inclConcat) ||
                canProduceTarget(target, numbers, index, current * nextNum, inclConcat);

        if (inclConcat) {
            long concatValue = parseLong(current + "" + nextNum);
            result = result || canProduceTarget(target, numbers, index, concatValue, inclConcat);
        }

        return result;
    }
}
