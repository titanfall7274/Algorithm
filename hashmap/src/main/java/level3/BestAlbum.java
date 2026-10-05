package level3;

import java.util.*;

public class BestAlbum {

    public static void main(String[] args) {

        String[] genres = {"classic", "pop", "classic", "classic", "pop"};
        int[] plays = {500, 600, 150, 800, 2500}; // 노래가 재생된 횟수

        System.out.println(Arrays.toString(solution(genres, plays)));
    }

    // n = 노래 수 (최대 1만), g = 장르 수 (최대 100)
    // 총 O(g · n + n log n) — while이 g번 돌며 매번 findMostPlayed가 남은 노래 전부를 훑음
    // 장르별 합을 처음에 한 번만 구해 두고 정렬하면 O(n log n)까지 줄일 수 있음
    private static int[] solution(String[] genres, int[] plays) {
        int[] answer = {};
        ArrayList<Integer> result = new ArrayList<>();
        HashMap<String, List<Song>> map = new HashMap<>();

        int n = genres.length;
        for (int i = 0; i < n; i++) { // O(n), 해시 연산 평균 O(1)
            String genre = genres[i];

            // <장르, ?>
            if(!map.containsKey(genre)) {
                map.put(genre, new ArrayList<>());
            }

            // <장르, (id, count)>
            map.get(genre).add(new Song(i, plays[i]));
        }


        // 장르 번만큼 반복
        while (!map.isEmpty()) { // g번
            // 1. 속한 노래가 많이 재생된 장르를 먼저 수록합니다. - 수록이 완료된 후에 map에서 제거하는 과정이 필요해보임
            String mostPlayed = findMostPlayed(map); // O(n) → while 전체로 O(g · n)

            // 2.해당 장르에서 많이 재생된 노래를 먼저 수록합니다.
            List<Song> findSong = map.get(mostPlayed);
            Collections.sort(findSong); // TimSort, 장르 노래 수 s에 대해 O(s log s) → 모든 장르 합쳐 O(n log n)

            // 3. 장르 내에서 재생 횟수가 같은 노래 중에서는 고유 번호가 낮은 노래를 먼저 수록합니다.
            // 또 장르에서 2개만 담는다고 합니다. 장르에 곡이 1개인 경우에는 IndexOutOfBoundsException발생 가능
            for (int j = 0; j < Math.min(2, findSong.size()); j++) { // O(1)
                Song song = findSong.get(j);
                result.add(song.getId());
            }

            map.remove(mostPlayed); // 1. 중복된 장르가 선택되지 않도록 삭제 + 더이상 사용할 필요가 없음
        }

        answer = result.stream() // Stream<Integer>
                .mapToInt(i -> i.intValue())
                .toArray(); // 결과는 최대 2g개라 O(g)
        return answer;
    }

    // 남은 장르의 노래를 전부 더해 보므로 O(n)
    private static String findMostPlayed(HashMap<String, List<Song>> map) {
        String mostPlayed = "";
        int max = 0;
        for (String genre : map.keySet()) {
            List<Song> findSong = map.get(genre);
            int count = 0;
            for (Song song : findSong) {
                count += song.getPlays();

            }

            if(count > max) {
                max = count;
                mostPlayed = genre;
            }
        }
        return mostPlayed;
    }

    private static class Song implements Comparable<Song> {

        private int id;
        private int plays;

        public Song(int id, int plays) {
            this.id = id;
            this.plays = plays;
        }

        public int getId() {
            return id;
        }

        public int getPlays() {
            return plays;
        }


        @Override
        public int compareTo(Song o) {
            // 1. 재생 수 desc
            if(this.plays != o.plays) {
                return Integer.compare(o.plays, this.plays);
            }
            // 2. 재생수가 같으면 번호 asc
            return Integer.compare(this.id, o.id);
        }
    }
}
