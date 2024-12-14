package ljankai.Day14;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RobotMove {
    public static void main(String[] args) throws IOException {
        String filename = "./src/main/java/ljankai/Day14/data";
        List<Robot> data = readFile(filename);
        int width = 101, height = 103;

        int seconds = 0;
        boolean foundTree = false;

        while (!foundTree && seconds < 10000) {
            seconds++;
            map(width, height, data);
            foundTree = checkForChristmasTree(width, height, data);
        }
        int safetyFactor = calculateSafetyFactor(width, height, data);
        System.out.println("Biztonsági faktor: " + safetyFactor);
        System.out.println("Karácsonyfa megtalálva " + seconds + " másodperc után.");
    }

    private static List<Robot> readFile(String filename) {
        List<Robot> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");

                String[] position = parts[0].substring(2).split(",");
                int x = Integer.parseInt(position[0]);
                int y = Integer.parseInt(position[1]);

                String[] velocity = parts[1].substring(2).split(",");
                int vx = Integer.parseInt(velocity[0]);
                int vy = Integer.parseInt(velocity[1]);

                lines.add(new Robot(x, y, vx, vy));
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return lines;
    }

    private static void map(int width, int height, List<Robot> data) {
        for (int i = 0; i < data.size(); i++) {
            Robot robot = data.get(i);
            int newX = (robot.x() + robot.vx() + width) % width;
            int newY = (robot.y() + robot.vy() + height) % height;
            data.set(i, new Robot(newX, newY, robot.vx(), robot.vy()));
        }
    }

    private static boolean checkForChristmasTree(int width, int height, List<Robot> data) {
        char[][] treePattern = {
                {'.', '.', '.', '.', 'X', '.', '.', '.', '.', '.'},
                {'.', '.', '.', 'X', 'X', 'X', '.', '.', '.', '.'},
                {'.', '.', 'X', 'X', 'X', 'X', 'X', '.', '.', '.'},
                {'.', 'X', 'X', 'X', 'X', 'X', 'X', 'X', '.', '.'},
                {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', '.'},
                {'.', '.', '.', 'X', '.', 'X', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}
        };


        char[][] map = new char[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                map[i][j] = '.';
            }
        }

        for (Robot robot : data) {
            map[robot.y()][robot.x()] = 'X';
        }

        for (int y = 0; y < height - 6; y++) {
            for (int x = 0; x < width - 6; x++) {
                boolean match = true;
                for (int i = 0; i < 7; i++) {
                    for (int j = 0; j < 7; j++) {
                        if (treePattern[i][j] == 'X' && map[y + i][x + j] != 'X') {
                            match = false;
                            break;
                        }
                    }
                    if (!match) break;
                }
                if (match) return true;
            }
        }
        return false;
    }

    private static int calculateSafetyFactor(int width, int height, List<Robot> robots) {
        int topLeft = 0, topRight = 0, bottomLeft = 0, bottomRight = 0;

        for (Robot robot : robots) {
            if (robot.x() == width / 2 || robot.y() == height / 2) continue;
            if (robot.x() < width / 2 && robot.y() < height / 2) topLeft++;
            else if (robot.x() >= width / 2 && robot.y() < height / 2) topRight++;
            else if (robot.x() < width / 2 && robot.y() >= height / 2) bottomLeft++;
            else if (robot.x() >= width / 2 && robot.y() >= height / 2) bottomRight++;
        }

        return topLeft * topRight * bottomLeft * bottomRight;
    }

    private record Robot(int x, int y, int vx, int vy) {
    }
}
