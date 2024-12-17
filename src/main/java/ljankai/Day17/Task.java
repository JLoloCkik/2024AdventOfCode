package ljankai.Day17;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Task {
    public static void main(String[] args) {
        String filename = "./src/main/java/ljankai/Day17/data";
        RegisterData data = readInput(filename);

        System.out.println("PartOne: " + runProgram(data, false));
        System.out.println("PartTwo: " + runProgram(data, true));
    }

    private static RegisterData readInput(String filename) {
        int registerA = 0;
        int registerB = 0;
        int registerC = 0;
        List<Integer> program = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Register A")) {
                    registerA = Integer.parseInt(line.split(":")[1].trim());
                } else if (line.startsWith("Register B")) {
                    registerB = Integer.parseInt(line.split(":")[1].trim());
                } else if (line.startsWith("Register C")) {
                    registerC = Integer.parseInt(line.split(":")[1].trim());
                } else if (line.startsWith("Program")) {
                    String[] instructions = line.split(":")[1].trim().split(",");
                    for (String instr : instructions) {
                        program.add(Integer.parseInt(instr));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return new RegisterData(registerA, registerB, registerC, program);
    }

    private static List<Integer> runProgram(RegisterData data, boolean part2) {
        int A = data.registerA;
        int B = data.registerB;
        int C = data.registerC;
        List<Integer> program = data.program();

        int pointer = 0;
        List<Integer> output = new ArrayList<>();

        while (pointer < program.size()) {
            int cmd = program.get(pointer);
            int op = program.get(pointer + 1);
            pointer += 2;
            int combo = getCombo(op, A, B, C);

            switch (cmd) {
                case 0:
                    A = A / (1 << combo);
                    break;
                case 1:
                    B = B ^ op;
                    break;
                case 2:
                    B = combo % 8;
                    break;
                case 3:
                    if (A != 0) {
                        pointer = op;
                        continue;
                    }
                    break;
                case 4:
                    B = B ^ C;
                    break;
                case 5:
                    output.add(combo % 8);
                    if (part2 && output.size() > 1 && output.get(output.size() - 1) != program.get(output.size() - 1)) {
                        return output;
                    }
                    break;
                case 6:
                    B = A / (1 << combo);
                    break;
                case 7:
                    C = A / (1 << combo);
                    break;
                default: throw new IllegalArgumentException("Unknown opcode: " + cmd);
            }
        }
        return output;
    }

    private static int getCombo(int op, int A, int B, int C) {
        if (op == 4) return A;
        if (op == 5) return B;
        if (op == 6) return C;
        return op;
    }

    record RegisterData(int registerA, int registerB, int registerC, List<Integer> program) {}
}
