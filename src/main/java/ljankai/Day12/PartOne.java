package ljankai.Day12;

import java.io.*;
import java.util.*;

public class PartOne {
    public static void main(String[] args) {
        int p2 = 0;
        String infile = "./src/main/java/ljankai/Day12/data";

        try (BufferedReader br = new BufferedReader(new FileReader(infile))) {
            String[] G = br.lines().toArray(String[]::new);
            int R = G.length, C = G[0].length();

            Set<String> SEEN = new HashSet<>();
            List<int[]> DIRS = Arrays.asList(
                    new int[]{-1, 0},
                    new int[]{0, 1},
                    new int[]{1, 0},
                    new int[]{0, -1}
            );

            for (int r = 0; r < R; r++) {
                for (int c = 0; c < C; c++) {
                    String key = r + "," + c;
                    if (SEEN.contains(key)) continue;

                    Region region = exploreRegion(G, r, c, R, C, SEEN, DIRS);

                    p2 += region.area * region.sides;
                }
            }

            System.out.println(p2);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Region exploreRegion(String[] grid, int r, int c, int R, int C, Set<String> SEEN, List<int[]> DIRS) {
        Deque<int[]> Q = new ArrayDeque<>();
        Q.add(new int[]{r, c});
        int area = 0, perim = 0;
        Map<String, Set<String>> PERIM = new HashMap<>();

        while (!Q.isEmpty()) {
            int[] cell = Q.poll();
            int r2 = cell[0], c2 = cell[1];
            String key = r2 + "," + c2;
            if (SEEN.contains(key)) continue;

            SEEN.add(key);
            area++;

            for (int[] dir : DIRS) {
                int rr = r2 + dir[0], cc = c2 + dir[1];
                if (isValidCell(rr, cc, R, C) && grid[rr].charAt(cc) == grid[r2].charAt(c2)) {
                    Q.add(new int[]{rr, cc});
                } else {
                    perim++;
                    PERIM.computeIfAbsent(dir[0] + "," + dir[1], k -> new HashSet<>()).add(key);
                }
            }
        }

        int sides = calculateSides(PERIM, DIRS);
        return new Region(area, sides);
    }

    private static boolean isValidCell(int r, int c, int R, int C) {
        return 0 <= r && r < R && 0 <= c && c < C;
    }

    private static int calculateSides(Map<String, Set<String>> PERIM, List<int[]> DIRS) {
        int sides = 0;
        for (Set<String> coords : PERIM.values()) {
            Set<String> SEEN_PERIM = new HashSet<>();
            for (String coord : coords) {
                if (!SEEN_PERIM.contains(coord)) {
                    sides++;
                    Deque<String> Q2 = new ArrayDeque<>(Collections.singletonList(coord));
                    while (!Q2.isEmpty()) {
                        String[] coordParts = Q2.poll().split(",");
                        int r2 = Integer.parseInt(coordParts[0]), c2 = Integer.parseInt(coordParts[1]);
                        String newKey = r2 + "," + c2;
                        if (SEEN_PERIM.contains(newKey)) continue;
                        SEEN_PERIM.add(newKey);
                        for (int[] dir : DIRS) {
                            int rr = r2 + dir[0], cc = c2 + dir[1];
                            if (coords.contains(rr + "," + cc)) Q2.add(rr + "," + cc);
                        }
                    }
                }
            }
        }
        return sides;
    }

    private static class Region {
        int area;
        int sides;

        Region(int area, int sides) {
            this.area = area;
            this.sides = sides;
        }
    }
}
