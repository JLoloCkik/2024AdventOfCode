package ljankai.Day19;

import java.util.*;
import java.nio.file.*;
import java.io.*;

public class Task {
    private static Map<String, Long> DP = new HashMap<>();
    public static void main(String[] args) throws IOException {
        String filePath = "./src/main/java/ljankai/Day19/data";
        List<String> words = new ArrayList<>();
        List<String> targets = new ArrayList<>();

        String data = new String(Files.readAllBytes(Paths.get(filePath))).trim();
        String[] parts = data.split("\n\n");

        words.addAll(Arrays.asList(parts[0].split(", ")));
        targets.addAll(Arrays.asList(parts[1].split("\n")));

        System.out.println("Part 1: " + countPart1(targets, words));
        System.out.println("Part 2: " + sumPart2(targets, words));
    }

    private static long countPart1(List<String> targets, List<String> words) {
        long count = 0;
        for (String target : targets) {
            if (solve(target, words) > 0) {
                count++;
            }
        }
        return count;
    }

    private static long sumPart2(List<String> targets, List<String> words) {
        long sum = 0;
        for (String target : targets) {
            sum += solve(target, words);
        }
        return sum;
    }

    private static long solve(String target, List<String> words) {
        long ans = 0;

        if (DP.containsKey(target)) return DP.get(target);

        if (target.isEmpty()) ans = 1;

        for (String word : words) {
            if (target.startsWith(word)) {
                ans += solve(target.substring(word.length()), words);
            }
        }

        DP.put(target, ans);
        return ans;
    }
}
