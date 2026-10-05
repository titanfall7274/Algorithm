package level1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KNumber {

    public static void main(String[] args) {
        int[] array = {1, 5, 2, 6, 3, 7, 4};
        int[][] commands = {{2, 5, 3}, {4, 4, 1}, {1, 7, 3}};
        System.out.println(Arrays.toString(solution(array, commands)));

        System.out.println(Arrays.toString(solution2(array, commands)));
    }

    // n = array.length (최대 100), m = commands.length (최대 50), L = 구간 길이 (최대 n)
    // 명령 하나에 O(L log L) → 총 O(m · n log n)
    private static int[] solution(int[] array, int[][] commands) {
        ArrayList<Integer> result = new ArrayList<>();

        // 정렬 아무거나 하면 되는듯
        for (int[] command : commands) { // m번
            // i, k가 1부터 세는 번호라 1을 뺀다
            int start = command[0] - 1;
            int end = command[1];

            ArrayList<Integer> temp = new ArrayList<>();
            for (int i = start; i < end; i++) { // O(L), Integer로 박싱
                temp.add(array[i]);
            }

            List<Integer> sorted = temp.stream()
                    .sorted() // Stream<Integer>라 TimSort, O(L log L)
                    .toList(); // O(L)

            result.add(sorted.get(command[2] - 1)); // O(1)
        }

        return result.stream()
                .mapToInt(Integer::intValue)
                .toArray(); // O(m)
    }

    // 복잡도는 solution과 같은 O(m · n log n). 박싱이 없어 상수만 작다
    private static int[] solution2(int[] array, int[][] commands) {
        ArrayList<Integer> result = new ArrayList<>();

        return Arrays.stream(commands) // m번
                .mapToInt(command -> Arrays.stream(array, command[0] - 1, command[1]) // 구간만 O(L)
                        .sorted() // IntStream이라 Dual-Pivot Quicksort, O(L log L)
                        .toArray()[command[2] - 1]) // O(L)
                .toArray(); // O(m)
    }
}
