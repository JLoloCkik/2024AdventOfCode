package ljankai.Day12;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PartTwo {
    public static void main(String[] args) throws IOException {
        String filename = "./src/main/java/ljankai/Day12/data";
        List<String> matrix = readFile(filename);
        PartTwo task = new PartTwo();

        long totalCost = task.calculateTotalCost(matrix);
        System.out.println("Total cost of fencing: " + totalCost);
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

    public long calculateTotalCost(List<String> lines) {
        int rows = lines.size();
        char[][] grid = new char[rows][];
        for (int i = 0; i < rows; i++) {
            grid[i] = lines.get(i).toCharArray();
        }

        Set<String> visited = new HashSet<>();
        long totalCost = 0;

        // Process each cell to find connected regions
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (!visited.contains(i + "," + j)) {
                    char plant = grid[i][j];
                    List<int[]> regionCells = new ArrayList<>();
                    exploreRegion(grid, i, j, plant, visited, regionCells);

                    long area = regionCells.size();
                    long sides = calculateSides(grid, regionCells);
                    long cost = area * sides;

                    totalCost += cost;
                }
            }
        }

        return totalCost;
    }

    private void exploreRegion(char[][] grid, int i, int j, char plant, Set<String> visited, List<int[]> regionCells) {
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[i].length || visited.contains(i + "," + j) || grid[i][j] != plant) {
            return;
        }

        visited.add(i + "," + j);
        regionCells.add(new int[] {i, j});

        exploreRegion(grid, i - 1, j, plant, visited, regionCells);
        exploreRegion(grid, i + 1, j, plant, visited, regionCells);
        exploreRegion(grid, i, j - 1, plant, visited, regionCells);
        exploreRegion(grid, i, j + 1, plant, visited, regionCells);
    }

    private long calculateSides(char[][] grid, List<int[]> regionCells) {
        long sides = 0;

        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

        for (int[] cell : regionCells) {
            int i = cell[0], j = cell[1];

            for (int[] dir : directions) {
                int di = dir[0], dj = dir[1];
                int ni = i + di, nj = j + dj;

                if (ni < 0 || ni >= grid.length || nj < 0 || nj >= grid[i].length || grid[ni][nj] != grid[i][j]) {
                    sides++;
                }
            }
        }
        return sides;
    }
}
