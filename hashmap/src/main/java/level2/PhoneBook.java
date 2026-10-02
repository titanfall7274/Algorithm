package level2;

import java.util.Arrays;
import java.util.HashSet;

public class PhoneBook {

    public static void main(String[] args) {

        String [] phone_book = {"119", "97674223", "1195524421"};
        System.out.println("solution(phone_book) = " + solution(phone_book));
    }

    private static boolean solution(String[] phoneBook) {
        boolean answer = true;

        HashSet<String> book = new HashSet<>();
        for (String number : phoneBook) {
            book.add(number); // O(1)
        }

        // O(N): 전화번호부는 최대 1,000,000 개
        for (int i = 0; i < phoneBook.length; i++) {
            String current = phoneBook[i];
            // O(20) 1 ~ 20
            for (int j = 0; j < current.length(); j++) {
                String substring = current.substring(0, j);
                if(book.contains(substring)) {
                    answer = false;
                }
            }
        }

        return answer;
    }

    private static boolean solution2(String[] phoneBook) {
        HashSet<String> book = new HashSet<>();
        // O(N)
        for (String number : phoneBook) {
            book.add(number); // O(1)
        }

        // O(N): 전화번호부는 최대 1,000,000 개
        for (int i = 0; i < phoneBook.length; i++) {
            String current = phoneBook[i];
            // O(20) 1 ~ 20
            for (int j = 0; j < current.length(); j++) {
                if(book.contains((current.substring(0, j)))) {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean solution3(String[] phoneBook) {
        Arrays.sort(phoneBook); // 정렬 O(N log N)

        for (int i = 0; i < phoneBook.length; i++) {
            if(phoneBook[i + 1].startsWith(phoneBook[i])) return false;
        }
        return true;
    }
}
