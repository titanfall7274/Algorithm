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

    private static int solution(String[][] clothes) {
        int answer = 0;

        // 같은 이름을 가진 의상은 존재하지 않습니다.
        // <종류, 선택지 수>
        HashMap<String, Integer> map = new HashMap<>();
        for (int i = 0; i < clothes.length; i++) {
            map.put(clothes[i][1], map.getOrDefault(clothes[i][1], 1) + 1);
        }

        int result = 1;
        for (int value : map.values()) {
            result *= value;
        }

        answer = result - 1;
        return answer;
    }

    private static int solution2(String[][] clothes) {
        return Arrays.stream(clothes)
                .collect(groupingBy(p -> p[1], mapping(p -> p[0], counting())))
                .values()
                .stream()
                .collect(reducing(1L, (x, y) -> x * (y + 1)))
                .intValue() - 1;
    }

    private static int solution3(String[][] clothes) {
        HashMap<String, List<String>> map = new HashMap<>();

        for (String[] clothe : clothes) {
            // <종류, <옷 이름들>>
            map.computeIfAbsent(clothe[1], k -> new ArrayList<>()).add(clothe[0]);
        }

        int result = 1;
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            String type = entry.getKey();
            List<String> names = entry.getValue();
            System.out.println(type + " : " + names);

            result *= names.size() + 1;
        }

        return result - 1;
    }
}
