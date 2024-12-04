package ljankai.fourthDay;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CeresSearch {
    public static void main(String[] args) {
        String filename = "./src/main/java/ljankai/fourthDay/data";
        List<String> grid = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                grid.add(line);
            }
        } catch (IOException e) {
            System.err.println("Hiba történt a fájl beolvasása közben: " + e.getMessage());
            return;
        }

        System.out.println("Part 1: " + part1(grid));
        System.out.println("Part 2: " + part2(grid));
    }

    public static int part1(List<String> grid) {
        String target = "XMAS";
        int rows = grid.size();
        int cols = grid.get(0).length();


        char[][] resultGrid = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(resultGrid[i], '.');
            for (int j = 0; j < cols; j++) {
                resultGrid[i][j] = grid.get(i).charAt(j);
            }
        }

        int occurrences = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                occurrences += highlightWord(grid, resultGrid, target, i, j);
            }
        }
        return occurrences;
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

    private static boolean checkPatterns(List<String> grid, int r, int c, int[][] X_DIRS, String[] PATTERN) {
        if (!(0 <= r + 2 && r + 2 < grid.size() && 0 <= c + 2 && c + 2 < grid.get(0).length())) {
            return false;
        }

        for (String p : PATTERN) {
            if (grid.get(r).charAt(c) == p.charAt(0) &&
                    grid.get(r + X_DIRS[0][0]).charAt(c + X_DIRS[0][1]) == p.charAt(1) &&
                    grid.get(r + X_DIRS[1][0]).charAt(c + X_DIRS[1][1]) == p.charAt(2) &&
                    grid.get(r + X_DIRS[2][0]).charAt(c + X_DIRS[2][1]) == p.charAt(3) &&
                    grid.get(r + X_DIRS[3][0]).charAt(c + X_DIRS[3][1]) == p.charAt(4)) {
                return true;
            }
        }
        return false;
    }

    private static int highlightWord(List<String> grid, char[][] resultGrid, String target, int row, int col) {
        int count = 0;
        int[] dRow = {-1, -1, -1, 0, 1, 1, 1, 0};
        int[] dCol = {-1, 0, 1, 1, 1, 0, -1, -1};
        int rows = grid.size();
        int cols = grid.get(0).length();
        int len = target.length();

        for (int dir = 0; dir < 8; dir++) {
            int r = row;
            int c = col;
            int k;

            for (k = 0; k < len; k++) {
                if (r < 0 || r >= rows || c < 0 || c >= cols || grid.get(r).charAt(c) != target.charAt(k)) {
                    break;
                }
                r += dRow[dir];
                c += dCol[dir];
            }

            if (k == len) {
                count++;
                r = row;
                c = col;

                for (k = 0; k < len; k++) {
                    resultGrid[r][c] = grid.get(r).charAt(c);
                    r += dRow[dir];
                    c += dCol[dir];
                }
            }
        }
        return count;
    }
}