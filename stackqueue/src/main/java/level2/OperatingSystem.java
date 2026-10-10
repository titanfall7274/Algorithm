package level2;

import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class OperatingSystem {

    public static void main(String[] args) {
        int[] priorities = {2, 1, 3, 2};
        int location = 2;
        solution(priorities, location);
    }

    private static int solution(int[] priorities, int location) {
        int answer = 0;

        Queue<int[]> q = new LinkedList<>();
        // 순서를 가진 우선순위 삽입
        for (int i = 0; i < priorities.length; i++) {
            q.offer(new int[]{i, priorities[i]});
        }

        // 가장 높은 우선순위부터 나열
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        for (int priority : priorities) {
            pq.offer(priority);
        }

        while (!q.isEmpty()) {

            int[] poll = q.poll();

            if(poll[1] < pq.peek()) {
                q.offer(poll);
            } else {
                pq.poll();
                answer++;
                if(poll[0] == location) return answer;
            }
        }
        return answer;
    }

    public int solution2(int[] priorities, int location) {
        int answer = 0;

        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        for (int priority : priorities) {
            pq.add(priority);
        }

        while (!pq.isEmpty()) {
            for (int i = 0; i < priorities.length; i++) {
                if(priorities[i] == pq.peek()) {
                    if(i == location) return answer;

                    pq.poll();
                    answer++;
                }
            }
        }
        return answer;
    }
}
