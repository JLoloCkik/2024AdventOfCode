package ljankai.sixthyDay;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

public class Task {
    public static void main(String[] args) {
        String filename = "./src/main/java/ljankai/sixthyDay/data";
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Hiba történt a fájl beolvasása közben: " + e.getMessage());
        }

        System.out.println(partOne(lines));
        System.out.println(findLoopPositions(lines));
    }

    private static int findLoopPositions(List<String> lines) {
        int rows = lines.size();
        int cols = lines.get(0).length();

        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int dirIndex = 0;
        int x = 0, y = 0;

        boolean found = false;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char c = lines.get(i).charAt(j);
                if ("^v<>".indexOf(c) != -1) {
                    x = i;
                    y = j;
                    dirIndex = "^v<>".indexOf(c);
                    found = true;
                    break;
                }
            }
            if (found) break;
        }

        if (!found) {
            throw new IllegalArgumentException("Nem található kezdőpozíció (^) a térképen!");
        }

        Set<String> visited = new HashSet<>();
        visited.add(x + "," + y);

        int stepLimit = rows * cols * 4;
        int steps = 0;

        while (x >= 0 && x < rows && y >= 0 && y < cols) {
            if (steps++ > stepLimit) {
                throw new IllegalStateException("Végtelen ciklus gyanú!");
            }

            int newX = x + directions[dirIndex][0];
            int newY = y + directions[dirIndex][1];

            if (newX < 0 || newX >= rows || newY < 0 || newY >= cols || lines.get(newX).charAt(newY) == '#') {
                dirIndex = (dirIndex + 1) % 4;
            } else {
                x = newX;
                y = newY;

                String pos = x + "," + y;
                if (visited.contains(pos)) {
                    break;
                }

                visited.add(pos);
            }
        }

        Set<String> loopPositions = new HashSet<>();
        for (String pos : visited) {
            String[] parts = pos.split(",");
            int loopX = Integer.parseInt(parts[0]);
            int loopY = Integer.parseInt(parts[1]);

            if (lines.get(loopX).charAt(loopY) != '^' && lines.get(loopX).charAt(loopY) != '#') {
                loopPositions.add(pos);
            }
        }

        return loopPositions.size();
    }

    private static int partOne(List<String> lines) {
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("A térkép nem lehet üres!");
        }

        int rows = lines.size();
        int cols = lines.get(0).length();

        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int dirIndex = 3;
        int x = 0, y = 0;

        boolean found = false;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (lines.get(i).charAt(j) == '^') {
                    x = i;
                    y = j;
                    found = true;
                    break;
                }
            }
            if (found) break;
        }

        if (!found) {
            throw new IllegalArgumentException("Nem található kezdőpozíció (^) a térképen!");
        }

        Set<String> visited = new HashSet<>();
        visited.add(x + "," + y);

        while (true) {
            int newX = x + directions[dirIndex][0];
            int newY = y + directions[dirIndex][1];

            if (newX < 0 || newX >= rows || newY < 0 || newY >= cols) {
                break;
            }

            if (lines.get(newX).charAt(newY) == '#') {
                dirIndex = (dirIndex + 1) % 4;
            } else {
                x = newX;
                y = newY;
                visited.add(x + "," + y);
            }
        }
        return visited.size();
    }
}
