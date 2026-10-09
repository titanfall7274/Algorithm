package level2;

import java.util.*;

public class Progress {
    // 전체: O(N × D), D = 최대 소요 일수(≤ 99)
    private static int[] solution(int[] progresses, int[] speeds) {
        int[] answer = {};

        Queue<Integer> progressQ = new ArrayDeque<>();
        Queue<Integer> speedQ = new ArrayDeque<>();
        List<Integer> result = new ArrayList<>();

        // O(N): 큐 적재
        for (int i = 0; i < progresses.length; i++) {
            progressQ.offer(progresses[i]);
            speedQ.offer(speeds[i]);
        }

        // O(D): 하루 단위 반복
        while (!progressQ.isEmpty()) {
            // O(N): 하루 작업 진행: 현재 큐 크기만큼만 회전
            for (int i = 0; i < progressQ.size(); i++) {
                int progress = progressQ.poll();
                int speed = speedQ.poll();
                progressQ.offer(progress + speed);
                speedQ.offer(speed);
            }

            // 총 O(N): 맨 앞부터 완료된 것들만 배포 (각 작업은 한 번만 poll)
            int count = 0;
            while (!progressQ.isEmpty() && progressQ.peek() >= 100) {
                progressQ.poll();
                speedQ.poll();
                count++;
            }
            if (count > 0) result.add(count);
        }

        // O(N): 리스트 → 배열
        answer = result.stream().mapToInt(Integer::intValue).toArray();
        return answer;
    }

    // 전체: O(N)
    private int[] solution2(int[] progresses, int[] speeds) {
        // O(N): 작업별 완료 일수 계산
        Queue<Integer> days = new ArrayDeque<>();
        for (int i = 0; i < progresses.length; i++) {
            days.offer((100 - progresses[i] + speeds[i] - 1) / speeds[i]); // 올림
        }
        // 총 O(N): 각 작업은 한 번만 poll
        List<Integer> result = new ArrayList<>();
        while (!days.isEmpty()) {
            int deploy = days.poll();
            int count = 1;
            while (!days.isEmpty() && days.peek() <= deploy) {
                days.poll();
                count++;
            }
            result.add(count);
        }
        // O(N): 리스트 → 배열
        return result.stream().mapToInt(Integer::intValue).toArray();
    }
    public static void main(String[] args) {
        int[] progresses = new int[] {93, 30, 55};
        int[] speeds = new int[] {1, 30, 5};

        int[] solution = solution(progresses, speeds);
        System.out.println("solution = " + Arrays.toString(solution));
    }
}
