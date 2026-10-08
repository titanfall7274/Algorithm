package level2;

import java.util.ArrayDeque;
import java.util.Deque;

public class Parentheses {

    public static void main(String[] args) {
        System.out.println(solution("()()"));
        System.out.println(solution("(())()"));
        System.out.println(solution(")()("));
        System.out.println(solution("(()("));
    }

    private static boolean solution(String s) {
        boolean answer = true;

        Deque<Character> stack = new ArrayDeque<>();

        // O(N)
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if(c == '(') {
                stack.push(c); // O(1)
            } else {
                if(stack.isEmpty()) return false;
                stack.pop(); // O(1)
            }
        }

        answer = stack.isEmpty(); // O(1)

        return answer;
    }
}
