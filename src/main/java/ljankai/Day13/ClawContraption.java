package ljankai.Day13;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Arrays;

public class ClawContraption {
    public static void main(String[] args) {
        String filename = "./src/main/java/ljankai/Day13/data";
        ClawContraption task = new ClawContraption();

        System.out.println("Part 1 solution: " + task.part1(filename));
        System.out.println("Part 2 solution: " + task.part2(filename));
    }

    public long part1(String filename) {
        return solve(filename, 0);
    }

    public long part2(String filename) {
        return solve(filename, 10_000_000_000_000L);
    }

    private long solve(String filename, long offset) {
        return dayStream(filename)
                .map(ClawContraption::parseData)
                .map(data -> data.withPrize(data.prizeX() + offset, data.prizeY() + offset))
                .mapToLong(Data::fewestTokens)
                .sum();
    }

    private Stream<String> dayStream(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            return Arrays.stream(reader.lines().collect(Collectors.joining("\n")).split("\n\n"));
        } catch (IOException e) {
            e.printStackTrace();
            return Stream.empty();
        }
    }

    private static Data parseData(String machineData) {
        String[] tokens = machineData.lines().toArray(String[]::new);
        long[] values = Arrays.stream(tokens)
                .flatMap(line -> Arrays.stream(line.replaceAll("[^0-9-]+", " ").trim().split(" ")))
                .mapToLong(Long::parseLong)
                .toArray();

        return new Data(values[0], values[1], values[2], values[3], values[4], values[5]);
    }

    public record Data(long aX, long aY, long bX, long bY, long prizeX, long prizeY) {
        public Data withPrize(long prizeX, long prizeY) {
            return new Data(aX, aY, bX, bY, prizeX, prizeY);
        }

        public long fewestTokens() {
            long denominator = bX * aY - bY * aX;

            long b = (prizeX * aY - prizeY * aX) / denominator;
            long remX = prizeX - b * bX;
            long numerator = (aX == 0) ? prizeY : remX;
            long divisor = (aX == 0) ? aY : aX;

            if (numerator % divisor != 0) return 0;

            long a = numerator / divisor;
            return (a * aY + b * bY == prizeY) ? 3 * a + b : 0;
        }
    }
}
