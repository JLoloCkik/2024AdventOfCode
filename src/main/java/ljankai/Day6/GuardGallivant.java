package ljankai.Day6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GuardGallivant {
    public static void main(String[] args) {
        String filename = "./src/main/java/ljankai/Day6/data";
        List<String> lines = readFile(filename);

        System.out.println("Part One: " + partOne(lines));
        System.out.println("Valid Positions: " + partTwo(lines).size());
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

    private static int partOne(List<String> lines) {
        int[] start = findStart(lines);
        Set<String> visited = new HashSet<>();
        simulatePath(lines, start[0], start[1], 3, visited);
        return visited.size();
    }

    private static void simulatePath(List<String> lines, int x, int y, int dir, Set<String> visited) {
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int rows = lines.size(), cols = lines.get(0).length();

        while (true) {
            visited.add(x + "," + y);
            int newX = x + directions[dir][0], newY = y + directions[dir][1];
            if (newX < 0 || newX >= rows || newY < 0 || newY >= cols) break;
            if (lines.get(newX).charAt(newY) == '#') dir = (dir + 1) % 4;
            else {
                x = newX;
                y = newY;
            }
        }
    }

    private static List<int[]> partTwo(List<String> lines) {
        int[] start = findStart(lines);
        List<int[]> validPositions = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.getFirst().length(); j++) {
                if (lines.get(i).charAt(j) == '.' && (i != start[0] || j != start[1])) {
                    modifyGrid(lines, i, j, '#');
                    if (doesPathLoop(lines, start[0], start[1], 3)) {
                        validPositions.add(new int[]{i, j});
                    }
                    modifyGrid(lines, i, j, '.');
                }
            }
        }
        return validPositions;
    }

    private static boolean doesPathLoop(List<String> lines, int x, int y, int dir) {
        Set<String> visitedStates = new HashSet<>();
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        while (true) {
            String state = x + "," + y + "," + dir;
            if (visitedStates.contains(state)) return true;
            visitedStates.add(state);

            int newX = x + directions[dir][0], newY = y + directions[dir][1];
            if (newX < 0 || newX >= lines.size() || newY < 0 || newY >= lines.get(0).length()) return false;

            if (lines.get(newX).charAt(newY) == '#') dir = (dir + 1) % 4;
            else {
                x = newX;
                y = newY;
            }
        }
    }

    private static int[] findStart(List<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            int index = lines.get(i).indexOf('^');
            if (index != -1) return new int[]{i, index};
        }
        throw new IllegalStateException("Start position (^) not found!");
    }

    private static void modifyGrid(List<String> lines, int i, int j, char ch) {
        char[] row = lines.get(i).toCharArray();
        row[j] = ch;
        lines.set(i, new String(row));
    }
}
