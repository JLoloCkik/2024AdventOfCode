package ljankai.Day13;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Arrays;

public class  ClawContraption {
    public static void main(String[] args) {
        String filename = "./src/main/java/ljankai/Day13/data";
        ClawContraption task = new ClawContraption();

        System.out.println("Part 1 solution: " + task.part1(filename));

        System.out.println("Part 2 solution: " + task.part2(filename));
    }


    public Object part1(String filename) {
        return solve(filename, 0);
    }

    public Object part2(String filename) {
        return solve(filename, 10_000_000_000_000L);
    }
    private long solve(String filename, long offset) {
        return dayStream(filename)
                .map(s -> readString(s, "Button A: X+%n, Y+%n\nButton B: X+%n, Y+%n\nPrize: X=%n, Y=%n", Data.class))
                .map(m -> m.withPrize(m.prizeX() + offset, m.prizeY() + offset))
                .mapToLong(Data::fewestTokens)
                .sum();
    }

    private Stream<String> dayStream(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            return Arrays.stream(reader
                    .lines()
                    .collect(Collectors.joining("\n"))
                    .split("\n\n"));
        } catch (IOException e) {
            e.printStackTrace();
            return Stream.empty();
        }
    }

    public static Data readString(String machineData, String regex, Class<Data> dataClass) {
        String[] tokens = machineData.split("\n");

        long aX = Long.parseLong(tokens[0].split(":")[1].split(",")[0].trim().replaceAll("[^0-9-]", ""));
        long aY = Long.parseLong(tokens[0].split(":")[1].split(",")[1].trim().replaceAll("[^0-9-]", ""));
        long bX = Long.parseLong(tokens[1].split(":")[1].split(",")[0].trim().replaceAll("[^0-9-]", ""));
        long bY = Long.parseLong(tokens[1].split(":")[1].split(",")[1].trim().replaceAll("[^0-9-]", ""));
        long prizeX = Long.parseLong(tokens[2].split(":")[1].split(",")[0].trim().replaceAll("[^0-9-]", ""));
        long prizeY = Long.parseLong(tokens[2].split(":")[1].split(",")[1].trim().replaceAll("[^0-9-]", ""));

        return new Data(aX, aY, bX, bY, prizeX, prizeY);
    }


    public record Data(long aX, long aY, long bX, long bY, long prizeX, long prizeY) {

        public Data withPrize(long prizeX, long prizeY) {
            return new Data(aX, aY, bX, bY, prizeX, prizeY);
        }

        public long fewestTokens() {
            long numerator = prizeX * aY - prizeY * aX;
            long denominator = bX * aY - bY * aX;

            if (denominator == 0) return 0;

            long b = numerator / denominator;
            long remX = prizeX - b * bX;
            long l = aX == 0 ? prizeY : remX;
            long r = aX == 0 ? aY : aX;

            if (l % r != 0) return 0;

            long a = l / r;
            return (a * aY + b * bY == prizeY) ? 3 * a + b : 0;
        }
    }
}
