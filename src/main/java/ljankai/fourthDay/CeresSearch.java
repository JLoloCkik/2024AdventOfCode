package ljankai.fourthDay;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CeresSearch {
    public static void main(String[] args) {
        String filename = "./src/main/java/ljankai/fourthDay/data";
        List<String> grid = readGridFromFile(filename);

        System.out.println("Part 1: " + part1(grid));
        System.out.println("Part 2: " + part2(grid));
    }

    private static List<String> readGridFromFile(String filename) {
        List<String> grid = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                grid.add(line);
            }
        } catch (IOException e) {
            System.err.println("Hiba történt a fájl beolvasása közben: " + e.getMessage());
        }
        return grid;
    }

    public static int part1(List<String> grid) {
        String target = "XMAS";
        int occurrences = 0;
        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < grid.get(i).length(); j++) {
                occurrences += searchWord(grid, target, i, j);
            }
        }
        return occurrences;
    }

    private static int searchWord(List<String> grid, String target, int row, int col) {
        int count = 0;

        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        int len = target.length();

        for (int dir = 0; dir < 8; dir++) {
            int r = row, c = col;
            int k;
            for (k = 0; k < len; k++) {
                if (r < 0 || r >= grid.size() || c < 0 || c >= grid.get(r).length() || grid.get(r).charAt(c) != target.charAt(k)) {
                    break;
                }
                r += directions[dir][0];
                c += directions[dir][1];
            }
            if (k == len) {
                count++;
            }
        }
        return count;
    }

    public static int part2(List<String> grid) {
        int count = 0;

        int[][] X_DIRS = {{1, 1}, {2, 2}, {0, 2}, {2, 0}};
        String[] PATTERN = {"MASSM", "MASMS", "SAMSM", "SAMMS"};

        for (int r = 0; r < grid.size(); r++) {
            for (int c = 0; c < grid.get(0).length(); c++) {
                if (checkPatterns(grid, r, c, X_DIRS, PATTERN)) {
                    count++;
                }
            }
        }
        return count;
    }

    private static boolean checkPatterns(List<String> grid, int row, int character, int[][] X_DIRS, String[] PATTERN) {
        if (!(0 <= row + 2 && row + 2 < grid.size() && 0 <= character + 2 && character + 2 < grid.get(0).length())) {
            return false;
        }

        for (String p : PATTERN) {
            if (grid.get(row).charAt(character) == p.charAt(0) &&
                    grid.get(row + X_DIRS[0][0]).charAt(character + X_DIRS[0][1]) == p.charAt(1) &&
                    grid.get(row + X_DIRS[1][0]).charAt(character + X_DIRS[1][1]) == p.charAt(2) &&
                    grid.get(row + X_DIRS[2][0]).charAt(character + X_DIRS[2][1]) == p.charAt(3) &&
                    grid.get(row + X_DIRS[3][0]).charAt(character + X_DIRS[3][1]) == p.charAt(4)) {
                return true;
            }
        }
        return false;
    }
}
