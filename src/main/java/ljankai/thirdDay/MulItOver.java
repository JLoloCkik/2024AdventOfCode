package ljankai.thirdDay;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MulItOver {
    public static void main(String[] args) {
        String filePath = "./src/main/java/ljankai/thirdDay/data";

        StringBuilder input = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                input.append(line);
            }
        } catch (IOException e) {
            System.err.println("Hiba a fájl olvasásakor: " + e.getMessage());
        }

        boolean mulEnabled = true;
        int sum = 0;

        String regex = "mul\\((\\d+),(\\d+)\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input.toString());

        int currentIndex = 0;
        while (matcher.find()) {
            String currentSubstring = input.substring(currentIndex, matcher.start());
            currentIndex = matcher.end();

            if (currentSubstring.contains("don't()")) {
                mulEnabled = false;
            } else if (currentSubstring.contains("do()")) {
                mulEnabled = true;
            } else if (currentSubstring.contains("undo()")) {
                mulEnabled = true;
            }

            int x = Integer.parseInt(matcher.group(1));
            int y = Integer.parseInt(matcher.group(2));

            if (mulEnabled) {
                sum += x * y;
            }
        }
        System.out.println("Sum of valid multiplications: " + sum);
    }
}
