package ljankai.Day8;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ResonantCollinearity {
    public static void main(String[] args) {
        String filename = "./src/main/java/ljankai/Day8/data";
        List<String> matrix = readFile(filename);

        if (matrix.isEmpty()) {
            System.err.println("Error: Input matrix is empty or file could not be read.");
            return;
        }

        System.out.println("Part One: " + partOne(matrix));
        System.out.println("Part Two: " + partTwo(matrix));
    }

    private static List<String> readFile(String filename) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) lines.add(line);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return lines;
    }

    public static int partOne(List<String> grid) {
        Map<Character, List<int[]>> antennas = extractAntennaPositions(grid);
        Set<String> antinodes = new HashSet<>();
        int rows = grid.size();
        int cols = grid.get(0).length();

        for (List<int[]> positions : antennas.values()) {
            int n = positions.size();
            for (int a = 0; a < n; a++) {
                for (int b = a + 1; b < n; b++) {
                    int[] pos1 = positions.get(a);
                    int[] pos2 = positions.get(b);

                    int dx = pos2[0] - pos1[0];
                    int dy = pos2[1] - pos1[1];
                    int[] antinode1 = {pos1[0] - dx, pos1[1] - dy};
                    int[] antinode2 = {pos2[0] + dx, pos2[1] + dy};

                    if (isValid(antinode1, rows, cols)) {
                        antinodes.add(antinode1[0] + "," + antinode1[1]);
                    }
                    if (isValid(antinode2, rows, cols)) {
                        antinodes.add(antinode2[0] + "," + antinode2[1]);
                    }
                }
            }
        }
        return antinodes.size();
    }

    public static int partTwo(List<String> grid) {
        Map<Character, List<int[]>> antennas = extractAntennaPositions(grid);
        Set<String> antinodes = new HashSet<>();
        int rows = grid.size();
        int cols = grid.get(0).length();

        for (List<int[]> positions : antennas.values()) {
            int n = positions.size();
            for (int[] pos : positions) {
                antinodes.add(pos[0] + "," + pos[1]);
            }

            for (int a = 0; a < n; a++) {
                for (int b = a + 1; b < n; b++) {
                    int[] pos1 = positions.get(a);
                    int[] pos2 = positions.get(b);

                    int dx = pos2[0] - pos1[0];
                    int dy = pos2[1] - pos1[1];
                    int gcd = gcd(Math.abs(dx), Math.abs(dy));
                    dx /= gcd;
                    dy /= gcd;

                    // Lépési egység kiszámolása
                    antinodes.addAll(extrapolateAntinodes(pos1, -dx, -dy, rows, cols));
                    antinodes.addAll(extrapolateAntinodes(pos2, dx, dy, rows, cols));
                }
            }
        }

        return antinodes.size();
    }

    private static Map<Character, List<int[]>> extractAntennaPositions(List<String> grid) {
        Map<Character, List<int[]>> antennas = new HashMap<>();
        int rows = grid.size();
        int cols = grid.get(0).length();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char c = grid.get(i).charAt(j);
                if (Character.isLetterOrDigit(c)) {
                    antennas.putIfAbsent(c, new ArrayList<>());
                    antennas.get(c).add(new int[]{i, j});
                }
            }
        }
        return antennas;
    }
     //LNKO
    private static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    private static boolean isValid(int[] point, int rows, int cols) {
        return point[0] >= 0 && point[0] < rows && point[1] >= 0 && point[1] < cols;
    }

    private static Set<String> extrapolateAntinodes(int[] start, int dx, int dy, int rows, int cols) {
        Set<String> antinodes = new HashSet<>();
        int[] current = start.clone();
        while (isValid(current, rows, cols)) {
            antinodes.add(current[0] + "," + current[1]);
            current[0] += dx;
            current[1] += dy;
        }
        return antinodes;
    }
}
