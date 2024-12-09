package ljankai.Day9;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Task {
    public static void main(String[] args) {
        String filename = "./src/main/java/ljankai/Day9/data";
        List<String> lines = readFile(filename);

        if (lines.isEmpty()) {
            System.err.println("Error: Input file could not be read or is empty.");
            return;
        }

        String diskMap = lines.get(0);


        System.out.println("Checksum: " + calculateChecksum(diskMap));
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

    // A fájlok tömörítése és a checksum kiszámítása
    private static int calculateChecksum(String map) {
        int checksum = 0;
        int position = 0;


        for (int i = 0; i < map.length(); i++) {
            char ch = map.charAt(i);


            if (ch != '.') {
                int fileId = Character.getNumericValue(ch); // A fájl ID-ja pl 2,9
                checksum += position * fileId; // Pozíció * fájl ID
            }

            position++; // Tovább lépünk a következő pozícióra
        }
        return checksum; // Visszatérünk a végső checksum-mal
    }

    // A fájlok és a szabad helyek elemzése
    private static void parseDiskMap(String map, List<Integer> fileSizes, List<Integer> freeSpaces) {
        for (int i = 0; i < map.length(); i++) {
            char ch = map.charAt(i);
            int size = Character.getNumericValue(ch);

            if (i % 2 == 0) {
                fileSizes.add(size);
            } else {
                freeSpaces.add(size);
            }
        }
    }

    // A fájlok tömörítése
    private static void compressFiles(List<Integer> fileSizes, List<Integer> freeSpaces) {
        int freeSpaceIndex = 0;

        for (int i = 0; i < fileSizes.size(); i++) {
            while (freeSpaceIndex < freeSpaces.size() && freeSpaces.get(freeSpaceIndex) >= fileSizes.get(i)) {
                freeSpaces.set(freeSpaceIndex, freeSpaces.get(freeSpaceIndex) - fileSizes.get(i));
                break;
            }
        }
    }
}
