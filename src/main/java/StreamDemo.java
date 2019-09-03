import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
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
        List<Product> productList = Arrays.asList(new Product(), new Product(), new Product());
        Stream<Product> parallelStream = productList.parallelStream();
        boolean isParallel = parallelStream.isParallel();
        System.out.println(isParallel);
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
