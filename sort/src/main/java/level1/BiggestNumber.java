package level1;

import java.util.Arrays;
import java.util.List;

public class BiggestNumber {

    public static void main(String[] args) {
        int[] numbers = {6, 10, 2};
        System.out.println(solution(numbers));

        int[] numbers2 = {3, 30, 34, 5, 9};
        System.out.println(solution(numbers2));

        int[] numbers3 = {0, 3, 30, 34, 5, 9, 0};
        System.out.println("solution(numbers3) = " + solution(numbers3));

    }

    // n = numbers.length (최대 10만), k = 원소 자릿수 (최대 4)
    // 총 O(n log n · k) = O(n log n) — k가 상수라 정렬이 지배
    private static String solution(int[] numbers) {
        String answer = "";
        List<String> list = Arrays.stream(numbers)
                .mapToObj(String::valueOf) // O(n · k)
                .sorted((a, b) -> (b + a).compareTo(a + b)) // TimSort, 비교 O(n log n)회 × 회당 O(k)
                .toList(); // O(n)

        if(list.get(0).equals("0")) { // O(1)
            return "0";
        }

        answer = String.join("", list); // O(n · k), 한 번만 복사
        return answer;
    }
}
