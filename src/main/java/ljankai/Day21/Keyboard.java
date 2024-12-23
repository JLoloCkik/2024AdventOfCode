package ljankai.Day21;

import java.util.*;

public class Keyboard {

    // Billentyűzet pozíciók definiálása
    private static final Map<String, int[]> numericKeys = new HashMap<>() {{
        put("7", new int[]{0, 0});
        put("8", new int[]{0, 1});
        put("9", new int[]{0, 2});
        put("4", new int[]{1, 0});
        put("5", new int[]{1, 1});
        put("6", new int[]{1, 2});
        put("1", new int[]{2, 0});
        put("2", new int[]{2, 1});
        put("3", new int[]{2, 2});
        put("0", new int[]{3, 1});
        put("A", new int[]{3, 2});
    }};

    // Mozgási irányok definiálása
    private static final Map<String, int[]> directionKeys = new HashMap<>() {{
        put("^", new int[]{-1, 0});
        put("v", new int[]{1, 0});
        put("<", new int[]{0, -1});
        put(">", new int[]{0, 1});
        put("A", new int[]{0, 0});
    }};


    public static List<String> dfs(int[] current, int[] target, Map<String, int[]> keypad, String path, Set<String> visited) {
        if (Arrays.equals(current, target)) {
            return List.of(path);
        }

        List<String> paths = new ArrayList<>();
        for (Map.Entry<String, int[]> move : directionKeys.entrySet()) {
            String direction = move.getKey();
            int[] delta = move.getValue();
            int[] nextPos = {current[0] + delta[0], current[1] + delta[1]};

            if (keypad.values().stream().anyMatch(pos -> Arrays.equals(pos, nextPos)) &&
                    !visited.contains(Arrays.toString(nextPos))) {
                visited.add(Arrays.toString(nextPos));
                paths.addAll(dfs(nextPos, target, keypad, path + direction, visited));
                visited.remove(Arrays.toString(nextPos));
            }
        }

        return paths;
    }

    public static List<String> generatePaths(String code) {
        List<String> paths = new ArrayList<>();
        int[] curPos = numericKeys.get("A");

        for (char ch : code.toCharArray()) {
            int[] targetPos = numericKeys.get(String.valueOf(ch));
            Set<String> visited = new HashSet<>();
            List<String> pathOptions = dfs(curPos, targetPos, numericKeys, "", visited);
            paths.add(pathOptions.get(0)); // Az első elérhető út kiválasztása
            curPos = targetPos;
        }

        return paths;
    }

    public static void main(String[] args) {
        // Példa kódok és komplexitás számítása
        List<String> codes = List.of("208A", "540A", "685A", "879A", "826A");
        int totalComplexity = 0;

        for (String code : codes) {
            List<String> paths = generatePaths(code);
            String sequence = String.join("", paths);
            int complexity = sequence.length() * Integer.parseInt(code.substring(0, code.length() - 1));
            totalComplexity += complexity;
        }

        System.out.println("Az összes kód komplexitása: " + totalComplexity);
    }
}
