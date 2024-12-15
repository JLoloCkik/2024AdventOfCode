package ljankai.Day11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class StoneCalculator {
    private final Map<Pair, Long> memo = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String filename = "./src/main/java/ljankai/Day11/data";
        List<String> lines = Files.readAllLines(Paths.get(filename));
        System.out.println("Initial arrangement: " + lines);

        StoneCalculator calculator = new StoneCalculator();

        System.out.println("After 25 blinks: " + calculator.calculateStones(25, lines));
        System.out.println("After 75 blinks: " + calculator.calculateStones(75, lines));
    }

    public long calculateStones(int totalBlinks, List<String> dayStream) {
        return dayStream.stream()
                .flatMap(this::splitAndStream)
                .mapToLong(stone -> countStones(stone, totalBlinks))
                .sum();
    }

    private Stream<Long> splitAndStream(String line) {
        return Arrays.stream(line.split("\\s+")).map(String::trim).map(Long::parseLong);
    }

    private long countStones(long stone, int blinksLeft) {
        if (blinksLeft == 0) {
            return 1;
        }

        Pair key = new Pair(stone, blinksLeft);
        if (memo.containsKey(key)) {
            return memo.get(key);
        }


        long result = calculateAmount(stone, blinksLeft);
        memo.put(key, result);
        return result;
    }

    private long calculateAmount(long stone, int blinksLeft) {
        if (stone != 0) {
            long digits = (long) Math.log10(Math.abs(stone)) + 1;
            if (digits % 2 == 0) {
                long pow = (long) Math.pow(10, digits / 2);
                return countStones(stone / pow, blinksLeft - 1) + countStones(stone % pow, blinksLeft - 1);
            }
            return countStones(stone * 2024, blinksLeft - 1);
        }
        return countStones(1L, blinksLeft - 1);
    }

    record Pair(long key, int value){};
}
