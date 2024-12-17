package ljankai.Day17;

import java.util.*;

public class ChronospatialComputer {
    public record RegisterData(long registerA, long registerB, long registerC, List<Integer> program) {
    }

    public static void main(String[] args) {
        RegisterData program = new RegisterData(25358015, 0, 0, Arrays.asList(2, 4, 1, 1, 7, 5, 0, 3, 4, 7, 1, 6, 5, 5, 3, 0));
        System.out.println("Part 1: " + simulateComputer(program));
        System.out.println("Part 2: " + check(program));
    }

    private static Set<Long> check(RegisterData data) {
        List<Integer> program = data.program();
        Set<Long> valids = new HashSet<>();
        Deque<Pair<Integer, Long>> stack = new ArrayDeque<>();
        stack.push(new Pair<>(0, 0L));

        while (!stack.isEmpty()) {
            Pair<Integer, Long> state = stack.pop();
            int depth = state.a();
            long score = state.b();

            if (depth == program.size()) valids.add(score);
            else {
                for (int i = 0; i < 8; i++) {
                    long newScore = i + 8 * score;
                    RegisterData simulation = new RegisterData(newScore, data.registerB(), data.registerC(), program);

                    if (simulateComputer(simulation).getFirst() == program.get(program.size() - 1 - depth)) {
                        stack.push(new Pair<>(depth + 1, newScore));
                    }
                }
            }
        }
        return valids;
    }

    private static List<Integer> simulateComputer(RegisterData data) {
        List<Integer> outs = new ArrayList<>();
        long registerA = data.registerA(), registerB = data.registerB(), registerC = data.registerC();

        List<Integer> input = data.program();
        for (int i = 1; i <= input.size(); i += 2) {
            long cmd = input.get(i - 1);
            switch ((int) cmd) {
                case 0 -> registerA >>= computeOperand(input.get(i), registerA, registerB, registerC);
                case 1 -> registerB ^= input.get(i);
                case 2 -> registerB = computeOperand(input.get(i), registerA, registerB, registerC) % 8;
                case 3 -> {
                    if (registerA != 0) i = input.get(i) - 1;
                }
                case 4 -> registerB ^= registerC;
                case 5 -> outs.add((int) (computeOperand(input.get(i), registerA, registerB, registerC) % 8));
                case 6 -> registerB = registerA >> computeOperand(input.get(i), registerA, registerB, registerC);
                case 7 -> registerC = registerA >> computeOperand(input.get(i), registerA, registerB, registerC);
                default -> throw new IllegalArgumentException("Invalid opcode: " + cmd);
            }
        }
        return outs;
    }

    private static long computeOperand(long val, long a, long b, long c) {
        return switch ((int) val) {
            case 0, 1, 2, 3 -> val;
            case 4 -> a;
            case 5 -> b;
            case 6 -> c;
            default -> throw new IllegalArgumentException("Invalid combo operand: " + val);
        };
    }

    public record Pair<A, B>(A a, B b) {
    }
}