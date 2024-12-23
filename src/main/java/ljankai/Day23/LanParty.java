package ljankai.Day23;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class LanParty {
    public static void main(String[] args) throws IOException {
        // Bemeneti fájl olvasása
        String filePath = "src/main/java/ljankai/Day23/data";
        List<String> lines = Files.readAllLines(Paths.get(filePath));

        // Gráf reprezentálása szomszédsági lista formájában
        Map<String, Set<String>> graph = new HashMap<>();

        for (String line : lines) {
            String[] parts = line.split("-");
            String nodeA = parts[0];
            String nodeB = parts[1];

            graph.computeIfAbsent(nodeA, k -> new HashSet<>()).add(nodeB);
            graph.computeIfAbsent(nodeB, k -> new HashSet<>()).add(nodeA);
        }

        // Part One: Háromszögek keresése
        Set<Set<String>> triangles = findTriangles(graph);
        long count = triangles.stream()
                .filter(triangle -> triangle.stream().anyMatch(node -> node.startsWith("t")))
                .count();
        System.out.println("Háromszögek kezdődő csúccsal: " + count);

        // Part Two: Legnagyobb klikk keresése
        Set<String> largestClique = findLargestClique(graph);

        // Jelszó generálása
        String password = String.join(",", largestClique.stream().sorted().toList());
        System.out.println("Jelszó a LAN partyhoz: " + password);
    }

    private static Set<Set<String>> findTriangles(Map<String, Set<String>> graph) {
        Set<Set<String>> triangles = new HashSet<>();
        for (String nodeA : graph.keySet()) {
            for (String nodeB : graph.get(nodeA)) {
                if (nodeB.compareTo(nodeA) <= 0) continue; // Rendezett iteráció
                for (String nodeC : graph.get(nodeB)) {
                    if (nodeC.compareTo(nodeB) <= 0) continue; // Rendezett iteráció
                    if (graph.get(nodeC).contains(nodeA)) {
                        // Háromszög megtalálva
                        Set<String> triangle = new HashSet<>(Arrays.asList(nodeA, nodeB, nodeC));
                        triangles.add(triangle);
                    }
                }
            }
        }
        return triangles;
    }

    private static Set<String> findLargestClique(Map<String, Set<String>> graph) {
        Set<String> largestClique = new HashSet<>();
        bronKerbosch(new HashSet<>(), new HashSet<>(graph.keySet()), new HashSet<>(), graph, largestClique);
        return largestClique;
    }

    private static void bronKerbosch(Set<String> r, Set<String> p, Set<String> x, Map<String, Set<String>> graph, Set<String> largestClique) {
        if (p.isEmpty() && x.isEmpty()) {
            if (r.size() > largestClique.size()) {
                largestClique.clear();
                largestClique.addAll(r);
            }
            return;
        }
        for (String v : new HashSet<>(p)) {
            Set<String> newR = new HashSet<>(r);
            newR.add(v);
            Set<String> newP = new HashSet<>(p);
            newP.retainAll(graph.get(v));
            Set<String> newX = new HashSet<>(x);
            newX.retainAll(graph.get(v));
            bronKerbosch(newR, newP, newX, graph, largestClique);
            p.remove(v);
            x.add(v);
        }
    }
}
