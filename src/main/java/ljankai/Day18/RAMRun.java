package ljankai.Day18;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class RAMRun {
    private static final int[][] DIRS = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
    private static final int N = 71;
    private static final char EMPTY = '.';
    private static final char CORRUPTED = '#';

    public static void main(String[] args) throws IOException {
        String filename = "./src/main/java/ljankai/Day18/data"; // Default file name
        if (args.length >= 1) {
            filename = args[0];
        }

        String filePath = new String(Files.readAllBytes(Paths.get(filename))).strip();

        char[][] G = new char[N][N];
        for (int i = 0; i < N; i++) {
            Arrays.fill(G[i], EMPTY);
        }

        String[] lines = filePath.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            String[] parts = line.split(",");
            int x = Integer.parseInt(parts[0].trim());
            int y = Integer.parseInt(parts[1].trim());

            if (0 <= y && y < N && 0 <= x && x < N) G[y][x] = CORRUPTED;


            boolean ok = false;
            Queue<int[]> queue = new LinkedList<>();
            Set<String> seen = new HashSet<>();
            queue.offer(new int[]{0, 0, 0});

            while (!queue.isEmpty()) {
                int[] current = queue.poll();
                int steps = current[0], r = current[1], c = current[2];

                if (r == N - 1 && c == N - 1) {
                    if (i == 1023) System.out.println("Steps to reach the exit after 1024 bytes: " + steps);

                    ok = true;
                    break;
                }

                String pos = r + "," + c;
                if (seen.contains(pos)) continue;
                seen.add(pos);

                for (int[] dir : DIRS) {
                    int rr = r + dir[0];
                    int cc = c + dir[1];


                    if (0 <= rr && rr < N && 0 <= cc && cc < N && G[rr][cc] != CORRUPTED) {
                        queue.offer(new int[]{steps + 1, rr, cc});
                    }
                }
            }

            if (!ok) {
                System.out.println("No valid path after byte " + i + " corruption at: " + x + "," + y);
                break;
            }
        }
    }
}
