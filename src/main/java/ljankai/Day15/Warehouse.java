package ljankai.Day15;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class Warehouse {
    public static void main(String[] args) throws IOException {
        String filename = "./src/main/java/ljankai/Day15/data";
        String file = new String(Files.readAllBytes(Paths.get(filename))).trim();

        // Split input into grid and instructions
        String[] parts = file.split("\n\n");
        String[] G = parts[0].split("\n");
        String instrs = parts[1];

        // Solve for both parts
        System.out.println("PartOne: " + solve(G, instrs, false));
        System.out.println("PartTwo: " + solve(G, instrs, true));
    }

    private static int solve(String[] G, String instrs, boolean part2) {
        int R = G.length;
        int C = G[0].length();
        char[][] grid = new char[R][C];

        for (int r = 0; r < R; r++) {
            grid[r] = G[r].toCharArray();
        }

        if (part2) {
            char[][] BIG_G = new char[R][C * 2];
            for (int r = 0; r < R; r++) {
                for (int c = 0; c < C; c++) {
                    switch (grid[r][c]) {
                        case '#': BIG_G[r][c * 2] = BIG_G[r][c * 2 + 1] = '#'; break;
                        case 'O': BIG_G[r][c * 2] = '['; BIG_G[r][c * 2 + 1] = ']'; break;
                        case '.': BIG_G[r][c * 2] = BIG_G[r][c * 2 + 1] = '.'; break;
                        case '@': BIG_G[r][c * 2] = '@'; BIG_G[r][c * 2 + 1] = '.'; break;
                    }
                }
            }
            grid = BIG_G;
            C *= 2;
        }

        int sr = 0, sc = 0;
        for (int r = 0; r < R; r++) {
            for (int c = 0; c < C; c++) {
                if (grid[r][c] == '@') {
                    sr = r;
                    sc = c;
                    grid[r][c] = '.';
                    break;
                }
            }
        }

        int r = sr, c = sc;

        for (char inst : instrs.toCharArray()) {
            if (inst == '\n') {
                continue;
            }
            int dr = 0, dc = 0;
            switch (inst) {
                case '^': dr = -1; break;
                case '>': dc = 1; break;
                case 'v': dr = 1; break;
                case '<': dc = -1; break;
            }

            int rr = r + dr, cc = c + dc;

            if (grid[rr][cc] == '#') continue;
            else if (grid[rr][cc] == '.') {
                r = rr;
                c = cc;
            } else if (grid[rr][cc] == '[' || grid[rr][cc] == ']' || grid[rr][cc] == 'O') {
                Queue<int[]> Q = new ArrayDeque<>();
                Set<String> SEEN = new HashSet<>();
                boolean ok = true;

                Q.add(new int[]{r, c});

                while (!Q.isEmpty()) {
                    int[] pos = Q.poll();
                    rr = pos[0];
                    cc = pos[1];
                    String key = rr + "," + cc;
                    if (SEEN.contains(key)) continue;
                    SEEN.add(key);
                    int rrr = rr + dr, ccc = cc + dc;
                    switch (grid[rrr][ccc]) {
                        case '#': ok = false; break;
                        case 'O':
                        case '[':
                        case ']':
                            Q.add(new int[]{rrr, ccc});
                            if (grid[rrr][ccc] == '[') {
                                assert grid[rrr][ccc + 1] == ']';
                                Q.add(new int[]{rrr, ccc + 1});
                            } else if (grid[rrr][ccc] == ']') {
                                assert grid[rrr][ccc - 1] == '[';
                                Q.add(new int[]{rrr, ccc - 1});
                            }
                            break;
                    }
                }
                if (!ok) continue;

                while (!SEEN.isEmpty()) {
                    for (String key : SEEN.toArray(new String[0])) {
                        String[] parts = key.split(",");
                        rr = Integer.parseInt(parts[0]);
                        cc = Integer.parseInt(parts[1]);
                        int rrr = rr + dr;
                        int ccc = cc + dc;
                        if (!SEEN.contains(rrr + "," + ccc)) {
                            assert grid[rrr][ccc] == '.';
                            grid[rrr][ccc] = grid[rr][cc];
                            grid[rr][cc] = '.';
                            SEEN.remove(key);
                        }
                    }
                }

                r += dr;
                c += dc;
            }
        }

        int ans = 0;
        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                if (grid[i][j] == '[' || grid[i][j] == 'O') {
                    ans += 100 * i + j;
                }
            }
        }
        return ans;
    }
}
