package level1;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Phoneketmon {

    public static void main(String[] args) {
        int[] nums = {3, 1, 2, 3};          // 2
        int[] nums2 = {3, 3, 3, 2, 2, 4};   // 3
        int[] nums3 = {3, 3, 3, 2, 2, 2};   // 2
        int[] nums4 = {1, 2, 3, 4};         // 2 (전부 다른 종류 → N/2에서 잘림)
        int[] nums5 = {7, 7, 7, 7};         // 1 (전부 같은 종류)

        System.out.println("solution(nums) = " + solution(nums));
        System.out.println("solution(nums2) = " + solution(nums2));
        System.out.println("solution(nums3) = " + solution(nums3));
        System.out.println("solution(nums4) = " + solution(nums4));
        System.out.println("solution(nums5) = " + solution(nums5));

        System.out.println("solution2(nums) = " + solution2(nums));
        System.out.println("solution2(nums2) = " + solution2(nums2));
        System.out.println("solution2(nums3) = " + solution2(nums3));
        System.out.println("solution2(nums4) = " + solution2(nums4));
        System.out.println("solution2(nums5) = " + solution2(nums5));
    }


    // N마리의 포켓몬중 N/2을 가져가라
    // n = nums.length (최대 1만), 총 O(n)
    private static int solution(int[] nums) {
        int answer = 0;

        // 손에 있는 종류가 가장 많게 가져가봐라
        HashMap<Integer, Integer> map = new HashMap<>();
        for (Integer num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1); // O(1)
        }

        // 중복을 제거한 자료구조를 사용함
        int half = nums.length / 2;
        HashSet<Integer> result = new HashSet<>();
        // O(n)
        for (int key : map.keySet()) {
            if (result.size() == half) return half;

            int value = map.getOrDefault(key, 0) - 1; // O(1)
            if(value < 0) continue;
            result.add(value); // O(1)
        }

        answer = result.size(); // O(1)
        return answer;
    }

    // 총 O(n)
    private static int solution2(int[] nums) {
        HashSet<Integer> hashSet = new HashSet<>();
        // O(n)
        for (int num : nums) {
            hashSet.add(num); // O(1)
        }

        return Math.min(hashSet.size(), nums.length / 2);
    }

    // 총 O(n), 정렬 없음
    private static int solution3(int[] nums) {
        // distinct() O(N) — 내부에서 Integer로 박싱한 뒤 HashSet으로 거름
        // count() O(N)
        return (int) Math.min(Arrays.stream(nums).distinct().count(), nums.length / 2);
    }
}
