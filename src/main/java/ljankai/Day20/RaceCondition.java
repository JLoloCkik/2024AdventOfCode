package ljankai.Day20;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class RaceCondition {

    public static final char START = 'S';
    public static final char END = 'E';
    public static final char WALL = '#';
    public static final int[][] DIRECTIONS = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    public static void main(String[] args) throws Exception {
        String filePath = "./src/main/java/ljankai/Day20/data";
        List<List<Character>> grid = readGridFromFile(filePath);

        int startI = -1, startj = -1, endI = -1, endJ = -1;
        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < grid.size(); j++) {
                char cell = grid.get(i).get(j);
                if (cell == START) {
                    startI = i;
                    startj = j;
                } else if (cell == END) {
                    endI = i;
                    endJ = j;
                }
            }
        }

        List<int[]> path = findPath(grid, startI, startj, endI, endJ);
        int og = path.size() - 1;

        Map<String, Integer> times = new HashMap<>();
        for (int t = 0; t < path.size(); t++) {
            int[] coord = path.get(t);
            times.put(coord[0] + "," + coord[1], og - t);
        }

        Map<String, Integer> saved = new HashMap<>();
        for (int t = 0; t < path.size(); t++) {
            int[] coord = path.get(t);
            int i = coord[0];
            int j = coord[1];

            for (int[] di1 : DIRECTIONS) {
                for (int[] di2 : DIRECTIONS) {
                    int ii = i + di1[0] + di2[0];
                    int jj = j + di1[1] + di2[1];

                    if (inGrid(grid, ii, jj) && grid.get(ii).get(jj) != WALL) {
                        String key = ii + "," + jj;
                        if (times.containsKey(key)) {
                            int rem_t = times.get(key);
                            saved.put(i + "," + j + "," + ii + "," + jj, og - (t + rem_t + 2));
                        }
                    }
                }
            }
        }

        int ans = 0;
        for (int v : saved.values()) {
            if (v >= 100) {
                ans++;
            }
        }
        System.out.println(ans);
    }
    private static List<List<Character>> readGridFromFile(String filePath) throws IOException {
        List<List<Character>> grid = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<Character> row = new ArrayList<>();
                for (char c : line.toCharArray()) row.add(c);
                grid.add(row);
            }
        }
        return grid;
    }

    private static List<int[]> findPath(List<List<Character>> grid, int si, int sj, int ei, int ej) {
        Queue<List<int[]>> queue = new LinkedList<>();
        queue.offer(Arrays.asList(new int[]{si, sj}));
        Set<String> visited = new HashSet<>();

        while (!queue.isEmpty()) {
            List<int[]> path = queue.poll();
            int[] current = path.get(path.size() - 1);
            int i = current[0], j = current[1];
            String currentKey = i + "," + j;

            if (i == ei && j == ej) return path;

            if (visited.contains(currentKey)) continue;

            visited.add(currentKey);

            for (int[] dir : DIRECTIONS) {
                int ii = i + dir[0], jj = j + dir[1];
                if (inGrid(grid, ii, jj) && grid.get(ii).get(jj) != WALL) {
                    List<int[]> newPath = new ArrayList<>(path);
                    newPath.add(new int[]{ii, jj});
                    queue.offer(newPath);
                }
            }
        }
        return null;
    }

    private static boolean inGrid(List<List<Character>> grid, int i, int j) {
        return i >= 0 && i < grid.size() && j >= 0 && j < grid.get(0).size();
    }
}