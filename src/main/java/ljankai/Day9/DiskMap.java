package ljankai.Day9;

import java.io.*;
import java.util.*;

public class DiskMap {
    private static long[] size;
    private static long[] loc;

    public static void main(String[] args) {
        String filename = "./src/main/java/ljankai/Day9/data";
        String line = readInput(filename);
        size = new long[line.length()];
        loc = new long[line.length()];

        System.out.println("PartOne: " + partOne(line));
        System.out.println("PatTwo: " + partTwo(line));
    }

    private static String readInput(String filename) {
        StringBuilder data = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.append(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return data.toString();
    }

    private static long partOne(String line) {
        List<Long> filesystem = parseDiskMap(line);
        filesystem = moveBlocks(filesystem);
        long checksum = calculateChecksum(filesystem);
        return checksum;
    }

    //    PartOne
    public static List<Long> parseDiskMap(String diskmap) {
        List<Long> blocks = new ArrayList<>();
        boolean isFile = true;
        long id = 0;

        for (char c : diskmap.toCharArray()) {
            int x = Character.getNumericValue(c);
            if (isFile) {
                for (int i = 0; i < x; i++) {
                    blocks.add(id);
                }
                id++;
                isFile = false;
            } else {
                for (int i = 0; i < x; i++) {
                    blocks.add(null);
                }
                isFile = true;
            }
        }

        return blocks;
    }

    public static List<Long> moveBlocks(List<Long> arr) {
        int firstFree = 0;
        while (arr.get(firstFree) != null) {
            firstFree++;
        }

        int i = arr.size() - 1;
        while (arr.get(i) == null) {
            i--;
        }

        while (i > firstFree) {
            arr.set(firstFree, arr.get(i));
            arr.set(i, null);
            while (arr.get(i) == null) {
                i--;
            }
            while (arr.get(firstFree) != null) {
                firstFree++;
            }
        }

        return arr;
    }

    public static long calculateChecksum(List<Long> arr) {
        long checksum = 0;
        for (int i = 0; i < arr.size(); i++) {
            Long block = arr.get(i);
            if (block != null) {
                checksum += i * block;
            }
        }
        return checksum;
    }

    //    PatTwo
    private static long partTwo(String line) {
        Long[] filesystem = makeFilesystem(line);
        move(filesystem);
        return checksum(filesystem);
    }

    private static Long[] makeFilesystem(String diskmap) {
        List<Long> blocks = new ArrayList<>();
        boolean isFile = true;
        long id = 0;

        for (char c : diskmap.toCharArray()) {
            int x = Character.getNumericValue(c);

            if (isFile) {
                loc[(int) id] = blocks.size();
                size[(int) id] = x;
                for (int i = 0; i < x; i++) {
                    blocks.add(id);
                }
                id++;
                isFile = false;
            } else {
                for (int i = 0; i < x; i++) {
                    blocks.add(null);
                }
                isFile = true;
            }
        }
        return blocks.toArray(new Long[0]); // Convert list to array
    }

    private static void move(Long[] arr) {
        int big = 0;
        while (size[big] > 0) {
            big++;
        }
        big--;

        for (int toMove = big; toMove >= 0; toMove--) {
            int freeSpace = 0, firstFree = 0;
            while (firstFree < loc[toMove] && freeSpace < size[toMove]) {
                firstFree += freeSpace;
                freeSpace = 0;
                while (firstFree < arr.length && arr[firstFree] != null) {
                    firstFree++;
                }
                while (firstFree + freeSpace < arr.length && arr[firstFree + freeSpace] == null) {
                    freeSpace++;
                }
            }

            if (firstFree >= loc[toMove]) {
                continue;
            }

            for (int idx = firstFree; idx < firstFree + size[toMove]; idx++) {
                arr[idx] = (long) toMove;
            }
            for (int idx = (int) loc[toMove]; idx < loc[toMove] + size[toMove]; idx++) {
                arr[idx] = null;
            }
        }
    }

    private static long checksum(Long[] arr) {
        long ans = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null) {
                ans += i * arr[i];
            }
        }
        return ans;
    }
}
