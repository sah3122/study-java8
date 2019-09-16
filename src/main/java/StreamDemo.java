import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        startLine("Filtering");
        Stream<String> filterStream = names.stream()
                .filter(name -> name.contains("a")); // filter는 predicate 인터페이스를 인자로 받으며 boolean을 리턴해야 한다.
        System.out.println(filterStream.collect(Collectors.toList()));
        endLine();

    }

    public static void printStream(Stream stream) {
        stream.forEach(s -> {
            System.out.println(s);
        });
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
