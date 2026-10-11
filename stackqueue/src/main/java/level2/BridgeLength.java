package level2;

import java.util.LinkedList;
import java.util.Queue;

public class BridgeLength {

    public static void main(String[] args) {
        int[] truck_weights = new int[]{7, 4, 5, 6};
        System.out.println("solution(2, 10, truck_weights) = " + solution(2, 10, truck_weights));
    }

    private static int solution(int bridge_length, int weight, int[] truckWeights) {
        int answer = 0;

        Queue<int[]> bridge = new LinkedList<>();

        int time = 0;
        int sum = 0;
        int idx = 0;

        while (idx < truckWeights.length || !bridge.isEmpty()) {
            time++;

            // 1. 나갈 트럽 빼기
            if(!bridge.isEmpty() && bridge.peek()[1] == time) {
                sum -= bridge.poll()[0];
            }

            // 2. 다음 트럭 빼기
            if(idx < truckWeights.length && sum + truckWeights[idx] <= weight) {
                sum += truckWeights[idx];
                bridge.offer(new int[]{truckWeights[idx], time + bridge_length});
                idx++;
            }
        }

        answer = time;
        return answer;
    }
}
