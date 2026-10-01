package level1;

import java.util.HashMap;

public class CantComplete {

    public static String solution(String[] participants, String[] completions) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        for (String participant : participants) {
            hashMap.put(participant, hashMap.getOrDefault(participant, 0) + 1);
        }
        for (String completion : completions) {
            hashMap.put(completion, hashMap.getOrDefault(completion, 0) - 1);
        }

        String answer = "";
        for (String name : hashMap.keySet()) {
            if(hashMap.get(name) != 0) {
                answer = name;
                break;
            }
        }

        return answer;
    }
    public static void main(String[] args) {
        String[] participants = {"leo", "kiki", "eden"};
        String[] completions = {"eden", "kiki"};

        System.out.println("solution(participants, completions) = " + solution(participants, completions));
    }
}
