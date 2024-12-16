package ljankai.Day16;

import java.io.*;
import java.util.*;

public class ReindeerMaze {

    private static final int[][] DIRS = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("./src/main/java/ljankai/Day16/data"));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();

        String D = sb.toString().strip();
        String[] G = D.split("\n");
        int R = G.length;
        int C = G[0].length();

        char[][] grid = createGrid(G, R, C);

        int[] startPos = findPosition(grid, 'S');
        int[] endPos = findPosition(grid, 'E');

        PriorityQueue<int[]> Q = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        Set<String> SEEN = new HashSet<>();
        Map<String, Integer> DIST = new HashMap<>();
        int best = findBestPath(Q, DIST, SEEN, startPos, endPos, R, C, grid);

        Map<String, Integer> DIST2 = new HashMap<>();
        SEEN.clear();
        calculateDistancesFromEnd(Q, DIST2, R, C, grid, endPos);

        Set<String> OK = findOptimalCells(DIST, DIST2, best, R, C);

        System.out.println("Az optimális úton lévő cellák száma: " + OK.size());
    }

    private static char[][] createGrid(String[] G, int R, int C) {
        char[][] grid = new char[R][C];
        for (int r = 0; r < R; r++) {
            grid[r] = G[r].toCharArray();
        }
        return grid;
    }

    private static int[] findPosition(char[][] grid, char target) {
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                if (grid[r][c] == target) {
                    return new int[]{r, c};
                }
            }
        }
        return null;
    }

    private static int findBestPath(PriorityQueue<int[]> Q, Map<String, Integer> DIST, Set<String> SEEN,
                                    int[] startPos, int[] endPos, int R, int C, char[][] grid) {
        int best = -1;
        Q.add(new int[]{0, startPos[0], startPos[1], 1});

        while (!Q.isEmpty()) {
            int[] current = Q.poll();
            int d = current[0], r = current[1], c = current[2], dir = current[3];
            String key = r + "," + c + "," + dir;

            if (DIST.containsKey(key) || SEEN.contains(key)) continue;

            DIST.put(key, d);

            if (r == endPos[0] && c == endPos[1] && best == -1) best = d;
            SEEN.add(key);
            handleMovement(Q, r, c, dir, d, R, C, grid);
        }

        System.out.println("Legjobb távolság: " + best);
        return best;
    }

    private static void handleMovement(PriorityQueue<int[]> Q, int r, int c, int dir, int d, int R, int C, char[][] grid) {
        int dr = DIRS[dir][0];
        int dc = DIRS[dir][1];
        int rr = r + dr, cc = c + dc;

        if (0 <= cc && cc < C && 0 <= rr && rr < R && grid[rr][cc] != '#') Q.add(new int[]{d + 1, rr, cc, dir});

        Q.add(new int[]{d + 1000, r, c, (dir + 1) % 4});
        Q.add(new int[]{d + 1000, r, c, (dir + 3) % 4});
    }

    private static void calculateDistancesFromEnd(PriorityQueue<int[]> Q, Map<String, Integer> DIST2,
                                                  int R, int C, char[][] grid, int[] endPos) {
        for (int dir = 0; dir < 4; dir++) {
            Q.add(new int[]{0, endPos[0], endPos[1], dir});
        }

        while (!Q.isEmpty()) {
            int[] current = Q.poll();
            int d = current[0];
            int r = current[1];
            int c = current[2];
            int dir = current[3];

            String key = r + "," + c + "," + dir;
            if (DIST2.containsKey(key)) {
                continue;
            }
            DIST2.put(key, d);

            int dr = DIRS[(dir + 2) % 4][0];
            int dc = DIRS[(dir + 2) % 4][1];
            int rr = r + dr, cc = c + dc;

            if (0 <= cc && cc < C && 0 <= rr && rr < R && grid[rr][cc] != '#') {
                Q.add(new int[]{d + 1, rr, cc, dir});
            }

            Q.add(new int[]{d + 1000, r, c, (dir + 1) % 4});
            Q.add(new int[]{d + 1000, r, c, (dir + 3) % 4});
        }
    }

    private static Set<String> findOptimalCells(Map<String, Integer> DIST, Map<String, Integer> DIST2,
                                                int best, int R, int C) {
        Set<String> OK = new HashSet<>();
        for (int r = 0; r < R; r++) {
            for (int c = 0; c < C; c++) {
                for (int dir = 0; dir < 4; dir++) {
                    String key = r + "," + c + "," + dir;
                    if (DIST.containsKey(key) && DIST2.containsKey(key) &&
                            DIST.get(key) + DIST2.get(key) == best) {
                        OK.add(r + "," + c);
                    }
                }
            }
        }
        return OK;
    }
}
