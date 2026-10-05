# Stream 내부 정렬 정리

Java Stream으로 정렬할 때 내부에서 실제로 무슨 일이 일어나는지, 코딩테스트에서 걸리는 지점만 모은다.

## 정렬 알고리즘은 원소 타입으로 갈린다

`Stream.sorted`는 원소를 배열에 전부 모은 뒤 `Arrays.sort`를 부른다. 그래서 스트림을 썼는지 아닌지가 아니라 **원소가 기본형인지 객체인지**가 알고리즘을 정한다.

| 코드 | 원소 | 알고리즘 | 최악 | 안정 |
|---|---|---|---|---|
| `Arrays.sort(int[])`, `IntStream.sorted()` | 기본형 | Dual-Pivot Quicksort | O(n log n) (Java 14+) | X |
| `Arrays.sort(String[])`, `Stream<T>.sorted()`, `List.sort()` | 객체 | TimSort | O(n log n) | O |

- 안정 정렬: 비교 결과가 같은 원소끼리 원래 순서가 유지되는 것.
- **TimSort**: 이미 정렬된 구간(run)을 찾고, 짧은 구간은 삽입 정렬로 늘린 뒤 병합 정렬처럼 합친다. 정렬된 입력이면 O(n). 추가 메모리 최대 n/2.
- **Dual-Pivot Quicksort**: 피벗 2개로 3구간 분할. Java 14부터 재귀가 깊어지면 힙 정렬로 전환해 최악도 O(n log n).
- 나눠 쓰는 이유: 객체는 같은 값이어도 서로 다른 객체라 안정성이 필요하고, 기본형은 구분할 방법이 없으니 메모리를 덜 쓰고 빠른 퀵 정렬을 쓴다.

## Comparator

- `compare(a, b)`가 **음수면 a가 앞**. 람다 `(a, b) -> ...`가 인자 2개인 건 정렬 알고리즘이 원소 쌍을 골라 "어느 쪽이 앞인가"를 묻기 때문이다.
- 내림차순은 인자를 뒤집어 `b.compareTo(a)`로 쓰거나 `-a.compareTo(b)`로 부호를 뒤집는다. `compareTo`는 `Integer.MIN_VALUE`를 반환하지 않으므로 `-`를 붙여도 안전하다.
- **기본형 정렬에는 Comparator를 못 넘긴다.** `int`를 원하는 규칙으로 정렬하려면 `boxed()`나 `mapToObj`로 객체로 바꿔야 하고, 그 순간 TimSort가 된다.
- 비교 규칙이 모순되면(a < b, b < c인데 c < a) TimSort가 `Comparison method violates its general contract!` 예외를 던질 수 있다.

## String.compareTo는 숫자 비교가 아니다

글자를 앞에서부터 문자 코드값으로 비교하는 사전순 비교다. 한쪽이 다른 쪽의 앞부분이면 길이 차이를 반환한다.

- `"10".compareTo("9")`는 음수다. `'1'`(49)이 `'9'`(57)보다 작아서다.
- **두 문자열의 길이가 같을 때만** 숫자 크기 비교와 결과가 같다. 가장 큰 수 문제의 `(b + a).compareTo(a + b)`가 맞는 이유다.
- 숫자로 바꾸지 않으니 `long` 범위를 넘는 수십만 자리 문자열도 비교할 수 있다.

## sorted는 다 모은 뒤에야 다음 단계로 넘긴다

`map`, `filter`는 원소를 하나씩 흘려보내지만 `sorted`는 전부 받아야 정렬할 수 있다. 스트림 중간에 있어도 O(n) 메모리를 쓴다.

- 버릴 원소가 있으면 `filter`를 `sorted` **앞에** 둔다. 정렬할 개수가 줄어든다.
- `filter`나 `map` 람다 안에서 새 스트림을 열어 `sorted`를 부르면, 그 원소 하나의 내용물만 정렬한다. 바깥 스트림과는 별개다(예: K번째수의 `Arrays.stream(array, i, j).sorted()`).

## 정렬 결과를 받을 때 걸리는 것

- `.toList()`는 **수정할 수 없는 리스트**를 돌려준다. 여기에 `list.sort(...)`를 부르면 `UnsupportedOperationException`. 정렬은 스트림 안에서 `sorted`로 끝낸다.
- `.toList()`는 Java 16부터 있다. 채점 환경이 그보다 낮으면 `.collect(Collectors.toList())`.

## 문자열 이어 붙이기: reduce + concat은 느리다

`reduce("", (s1, s2) -> s1.concat(s2))`는 매번 지금까지 쌓인 문자열 전체를 복사해서 **총 길이의 제곱**에 비례한다. `String.join("", list)`나 `Collectors.joining()`은 한 번만 복사한다. 반복문의 `answer += s`도 같은 이유로 느리다.

측정(2026-10-05, Temurin 21, 0~1000 무작위 정수 10만 개, 이어 붙인 길이 약 29만 자리, 같은 비교 함수로 정렬):

| 방식 | 1회 | 2회 | 3회 |
|---|---|---|---|
| `sorted` + `reduce` + `concat` | 1528ms | 1937ms | 1668ms |
| `sorted` + `String.join` | 128ms | 87ms | 73ms |

## 이 저장소에서 Stream으로 푼 문제

| 문제 | 메서드 | 쓴 것 | 내부에서 일어나는 일 |
|---|---|---|---|
| [가장 큰 수](<sort/src/main/java/level1/BiggestNumber.java>) | `solution` | `mapToObj` → `sorted(Comparator)` → `String.join` | `Stream<String>`이라 TimSort. 비교 1회마다 `a + b`, `b + a` 문자열 2개 생성. 전체 O(n log n) |
| [K번째수](<sort/src/main/java/level1/KNumber.java>) | `solution2` | `mapToInt` 안에서 `Arrays.stream(array, i, j).sorted()` | 명령마다 별도 `IntStream`을 열어 그 구간만 정렬. 기본형이라 Dual-Pivot Quicksort |
| [의상](<hashmap/src/main/java/level2/Clothes.java>) | `solution2` | `groupingBy` + `mapping` + `counting` → `reducing` | `groupingBy`는 기본으로 `HashMap`에 모은다. `counting()`은 `Long`을 돌려줘서 마지막에 `intValue()`가 필요 |
| [폰켓몬](<hashmap/src/main/java/level1/Phoneketmon.java>) | `solution3` | `distinct().count()` | 정렬 없이 해시로 중복을 걸러 평균 O(n). `IntStream.distinct()`는 내부에서 박싱한 뒤 처리 |
| [베스트앨범](<hashmap/src/main/java/level3/BestAlbum.java>) | `solution` | 결과 변환만 `mapToInt(...).toArray()` | 정렬은 `Collections.sort` + `Comparable`(`Song.compareTo`)이라 TimSort. Stream이 아니어도 객체 정렬이면 같은 알고리즘 |

## 출처

아래 항목은 Temurin 21 JDK 소스(`lib/src.zip`)에서 직접 확인했다(2026-10-05).

- `SortedOps`: 객체는 `Arrays.sort(array, 0, offset, comparator)`, `int`는 `Arrays.sort(ints)`를 부른다.
- `Arrays.sort(T[], Comparator)`는 `TimSort.sort`, `Arrays.sort(int[])`는 `DualPivotQuicksort.sort`를 부른다.
- `DualPivotQuicksort`: 재귀 깊이가 `MAX_RECURSION_DEPTH`를 넘으면 `heapSort`로 전환한다.
- `IntPipeline.distinct()`는 `boxed().distinct().mapToInt(i -> i)`이고, `DistinctOps`는 `HashSet`으로 중복을 거른다. JDK 주석에도 "효율적이지 않다"고 적혀 있다.
- `Collectors.groupingBy(classifier, downstream)`는 `HashMap::new`를 쓴다.
- `Stream.toList()`는 수정할 수 없는 리스트를 돌려준다.

TimSort의 run·삽입 정렬·병합 세부 동작과 Java 14 이전 버전의 동작은 소스로 확인하지 않았다. 측정값은 위 조건에서 직접 실행한 결과다.
