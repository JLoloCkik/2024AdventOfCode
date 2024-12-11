package ljankai.Day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class HoofIt {
    private static final int[][] dd = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    private static int n;

    public static void main(String[] args) throws IOException {
        List<String> grid = new ArrayList<>();
        grid = Files.readAllLines(Paths.get("./src/main/java/ljankai/Day10/data"));

        n = grid.size();

        System.out.println("Part One: " + partOne(grid));
        System.out.println("Part Two: " + partTwo(grid));

    }
//    Part One
    private static int partOne(List<String> matrix) {
        List<int[]> trailheads = searchZero(matrix);
        List<Integer> scores = new ArrayList<>();

        for (int[] trailhead : trailheads) {
            boolean[][] visited = new boolean[matrix.size()][matrix.get(0).length()];
            int score = dfs(matrix, trailhead[0], trailhead[1], visited, 0);
            scores.add(score);
        }

        System.out.println("Trailhead scores: " + scores);

        int totalScore = scores.stream().mapToInt(Integer::intValue).sum();

        return totalScore;
    }

    private static int dfs(List<String> matrix, int x, int y, boolean[][] visited, int currentHeight) {
        if (x < 0 || y < 0 || x >= matrix.size() || y >= matrix.get(0).length()) {
            return 0;
        }
        if (visited[x][y] || Character.getNumericValue(matrix.get(x).charAt(y)) != currentHeight) {
            return 0;
        }

        visited[x][y] = true;

        if (currentHeight == 9) {
            return 1;
        }

        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};
        int count = 0;

        for (int i = 0; i < 4; i++) {
            count += dfs(matrix, x + dx[i], y + dy[i], visited, currentHeight + 1);
        }

        return count;
    }

    private static List<int[]> searchZero(List<String> matrix) {
        List<int[]> trailheads = new ArrayList<>();
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).length(); j++) {
                if (matrix.get(i).charAt(j) == '0') {
                    trailheads.add(new int[]{i, j});
                }
            }
        }
        return trailheads;
    }

    //Part Two
    private static int partTwo(List<String> grid) {
        int result = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grid.get(i).charAt(j) == '0') {
                    result += rating(i, j, grid);
                }
            }
        }
        return result;
    }

    private static boolean inGrid(int i, int j ){
        return (0 <= i && i < n) && (0 <= j && j < n);
    }

    private static int rating(int i, int j, List<String> grid) {
        if (grid.get(i).charAt(j) == '9') {
            return 1;
        }

        int ans = 0;
        for (int[] d : dd) {
            int ii = i + d[0], jj = j + d[1];
            if (!inGrid(ii, jj)) {
                continue;
            }

            if (Character.getNumericValue(grid.get(ii).charAt(jj)) == Character.getNumericValue(grid.get(i).charAt(j)) + 1) {
                ans += rating(ii, jj, grid);
            }
        }

        return ans;
    }

}
