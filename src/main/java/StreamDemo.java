import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Created by dongchul on 2019-09-03.
 * https://futurecreator.github.io/2018/08/26/java-8-streams/ 코드 예제 프로젝트.
 */
public class StreamDemo {
    public static void main(String[] args) {
        Stream<String> generateStream = Stream.generate(() -> "generate").limit(5);
        startLine("generate");
        printStream(generateStream);
        endLine();
        Stream<Integer> iteratedStream = Stream.iterate(30, n -> n + 2).limit(5);
        startLine("iterate");
        printStream(iteratedStream);
        endLine();

        IntStream intStream = IntStream.range(1, 5);
        Stream<Integer> ntStream = IntStream.range(1, 5).boxed();
        startLine("intStream");
        printIntStream(intStream);
        printStream(ntStream);
        endLine();

        LongStream longStream = LongStream.rangeClosed(1, 5);
        startLine("longStream");
        printLongStream(longStream);
        endLine();

        IntStream charsStream = "Stream".chars();
        startLine("charsStream");
        printIntStream(charsStream);
        endLine();

        Stream<String> regStream = Pattern.compile(",").splitAsStream("dongchul, stream, demo");
        startLine("string regex stream");
        printStream(regStream);
        endLine();

        startLine("parallel Stream");

        List<Product> productList = Arrays.asList(new Product(10), new Product(20), new Product(21));
        Stream<Product> parallelStream = productList.parallelStream();

        boolean isParallel = parallelStream.isParallel();
        System.out.println(isParallel);

        boolean isMany = parallelStream.map(product -> product.getAmount() * 10).anyMatch(amount -> amount > 200);
        System.out.println(isMany);

        //Arrays.stream(arr).parallel(); 배열을 이용한 병렬 스트림 생성

        IntStream intStream1 = IntStream.range(1, 150).parallel();
        isParallel = intStream1.isParallel();
        System.out.println(isParallel);

        intStream1 = intStream1.sequential(); // 되돌리기
        isParallel = intStream1.isParallel();
        System.out.println(isParallel);

        endLine();

        startLine("Stream Concat");

        Stream<String> stream1 = Stream.of("Java", "Scala", "Groovy");
        Stream<String> stream2 = Stream.of("Python", "Go", "Swift");
        Stream<String> concat = Stream.concat(stream1, stream2);

        concat.forEach(str -> {
            System.out.println(str);
        });

        endLine();

        startLine("Intermediate operations");
        List<String> names = Arrays.asList("Eric", "Elena", "Java");
        /**
         * Filter 는 스트림 내 요소들을 하나씩 평가해서 걸러내는 작업, 인자로 받는 Predicate는 boolean을 리턴하는 함수형 인터페이스로 평가식을 받음.
         */
        startLine("Filtering");
        Stream<String> filterStream = names.stream()
                .filter(name -> name.contains("a")); // filter는 predicate 인터페이스를 인자로 받으며 boolean을 리턴해야 한다.
        System.out.println(filterStream.collect(Collectors.toList())); // Stream to List
        endLine();

        startLine("Mapping");
        /**
         * 맵은 스트림내 요소들을 하나씩 특정 값으로 변환해준다. 인자로는 람다식을 받음.
         */
        Stream<String> mappingStream = names.stream().map(String::toUpperCase);
        System.out.println(mappingStream.collect(Collectors.toList()));

        Stream<Integer> mappingStream2 = productList.stream().map(Product::getAmount);
        System.out.println(mappingStream2.collect(Collectors.toList()));
        endLine();

        startLine("FlatMap");
        /**
         * 인자로 Mapper를 받으며 리턴 타입이 Stream이다.
         * flatmap은 중첩 구조를 한단계 제거하고 단일 컬렉션으로 만들어 주는 역할을 한다.
         * Flattening
         */
        List<List<String>> list = Arrays.asList(Arrays.asList("a"), Arrays.asList("b")); //[[a],[b]]
        List<String> flatList = list.stream().flatMap(Collection::stream).collect(Collectors.toList());

        System.out.println(flatList.toString());

        List<Student> students = Arrays.asList(new Student(10,11,12), new Student(5,8,6), new Student(13,16,14));

        students.stream()
                .flatMapToInt(student -> IntStream.of(student.getKor(), student.getEng(), student.getMath()))
                .average().ifPresent(avg ->
                System.out.println(Math.round(avg * 10)/10.0)); // student 객체를 int stream으로 만들어 점수의 총합을 평균을 냄

        endLine();

        startLine("Sorting");

        List<Integer> sortingResult = IntStream.of(14,11,25,13,16)
                .sorted()
                .boxed()
                .collect(Collectors.toList());

        System.out.println(sortingResult);

        List<String> lang = Arrays.asList("Java", "Scala", "Groovy", "Python", "Go", "Swift");

        lang.stream()
                .sorted() // 순방향 정렬
                .collect(Collectors.toList());

        lang.stream()
                .sorted(Comparator.reverseOrder()) // 역정렬
                .collect(Collectors.toList());

        endLine();
    }

    public static void printStream(Stream stream) {
        stream.forEach(System.out::println);
    }

    public static void printIntStream(IntStream intStream) {
        intStream.forEach(s -> {
            System.out.println(s);
        });
    }

    public static void printLongStream(LongStream longStream) {
        longStream.forEach(s -> {
            System.out.println(s);
        });
    }


    public static void startLine(String name) {
        System.out.println(name);
        System.out.println("=================================================");
    }

    public static void endLine() {
        System.out.println("=================================================");
    }
}
