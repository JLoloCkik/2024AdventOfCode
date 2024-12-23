package ljankai.Day22;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class BuyersProcessor {
    public static void main(String[] args) throws IOException {
        // Process buyers
        Pair<Long, List<List<Integer>>> result = processBuyers("src/main/java/ljankai/Day22/data");
        long total = result.getKey();  // Total is now long
        List<List<Integer>> buyers = result.getValue();
        System.out.println("Total: " + total);

        // Calculate sequences and find max value
        Map<List<Integer>, Integer> sorok = calculateSorok(buyers);
        int maxValue = Collections.max(sorok.values());
        System.out.println("Max Value: " + maxValue);
    }
    private static final long MOD = 16777216;

    // Step function updated for long
    public static long step(long num) {
        num = (num ^ (num * 64)) % MOD;
        num = (num ^ (num / 32)) % MOD;
        num = (num ^ (num * 2048)) % MOD;
        return num;
    }

    // Process buyers method returns total as long
    public static Pair<Long, List<List<Integer>>> processBuyers(String filePath) throws IOException {
        long total = 0;
        List<List<Integer>> buyers = new ArrayList<>();

        List<String> lines = Files.readAllLines(Paths.get(filePath));
        for (String line : lines) {
            long num = Long.parseLong(line.trim());  // Use long for num
            List<Integer> buyer = new ArrayList<>();
            buyer.add((int)(num % 10));  // Modulo 10, but stored as integer for buyer list

            for (int i = 0; i < 2000; i++) {
                num = step(num);
                buyer.add((int)(num % 10));  // Again, store the last digit as integer

            }
            total += num;  // total is now long
            buyers.add(buyer);
        }

        return new Pair<>(total, buyers);
    }

    // Calculate sequences method, no change needed here, it uses Integer values
    public static Map<List<Integer>, Integer> calculateSorok(List<List<Integer>> buyers) {
        Map<List<Integer>, Integer> sorok = new HashMap<>();

        for (List<Integer> b : buyers) {
            Set<List<Integer>> seen = new HashSet<>();

            for (int i = 0; i < b.size() - 4; i++) {
                int a1 = b.get(i);
                int a2 = b.get(i + 1);
                int a3 = b.get(i + 2);
                int a4 = b.get(i + 3);
                int a5 = b.get(i + 4);

                List<Integer> sor = Arrays.asList(a2 - a1, a3 - a2, a4 - a3, a5 - a4);

                if (seen.contains(sor)) {
                    continue;
                }

                seen.add(sor);
                sorok.put(sor, sorok.getOrDefault(sor, 0) + a5);
            }
        }

        return sorok;
    }

    // Simple Pair class to store key-value pairs
    public static class Pair<K, V> {
        private final K key;
        private final V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
}
