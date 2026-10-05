package level2;

import java.util.*;

import static java.util.stream.Collectors.*;

public class Clothes {

    public static void main(String[] args) {
        // 최대 30개의 의상, 옷들은 20글자
        String [][] clothes = {
                {"yellow_hat", "headgear"},
                {"blue_sunglasses", "eyewear"},
                {"green_turban", "headgear"}
        };

        System.out.println(solution(clothes));
        System.out.println(solution2(clothes));
        System.out.println(solution3(clothes));
    }

    // n = clothes.length (최대 30), t = 종류 수 (t ≤ n)
    // 총 O(n) — 해시 연산은 평균 O(1)
    private static int solution(String[][] clothes) {
        int answer = 0;

        // 같은 이름을 가진 의상은 존재하지 않습니다.
        // <종류, 선택지 수>
        HashMap<String, Integer> map = new HashMap<>();
        for (int i = 0; i < clothes.length; i++) { // O(n)
            map.put(clothes[i][1], map.getOrDefault(clothes[i][1], 1) + 1); // 평균 O(1)
        }

        int result = 1;
        for (int value : map.values()) { // O(t)
            result *= value;
        }

        answer = result - 1;
        return answer;
    }

    // 총 O(n), 정렬 없음
    private static int solution2(String[][] clothes) {
        return Arrays.stream(clothes)
                .collect(groupingBy(p -> p[1], mapping(p -> p[0], counting()))) // HashMap에 모음, O(n)
                .values()
                .stream()
                .collect(reducing(1L, (x, y) -> x * (y + 1))) // O(t)
                .intValue() - 1;
    }

    // 총 O(n), 출력까지 포함하면 O(n) 그대로
    private static int solution3(String[][] clothes) {
        HashMap<String, List<String>> map = new HashMap<>();

        for (String[] clothe : clothes) { // O(n)
            // <종류, <옷 이름들>>
            map.computeIfAbsent(clothe[1], k -> new ArrayList<>()).add(clothe[0]); // 평균 O(1)
        }

        int result = 1;
        for (Map.Entry<String, List<String>> entry : map.entrySet()) { // O(t)
            String type = entry.getKey();
            List<String> names = entry.getValue();
            System.out.println(type + " : " + names);

            result *= names.size() + 1;
        }

        return result - 1;
    }
}
