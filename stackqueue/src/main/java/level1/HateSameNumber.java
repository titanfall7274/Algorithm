package level1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HateSameNumber {
    public static void main(String[] args) {
        int[] arr = {1, 1, 3, 3, 0, 1, 1};
        System.out.println(Arrays.toString(solution(arr)));

        int[] arr2 = {4, 4, 4, 3, 3};
        System.out.println(Arrays.toString(solution(arr2)));
    }

    private static int[] solution(int[] arr) {
        int[] answer = {};

        List<Integer> result = new ArrayList<>();

        result.add(arr[0]);

        // O(N)
        for (int i = 1; i < arr.length; i++) {
            if(arr[i - 1] == arr[i]) continue;

            result.add(arr[i]); // O(1)
        }

        answer = result.stream()
                .mapToInt(Integer::intValue)
                .toArray();

        return answer;
    }
}
