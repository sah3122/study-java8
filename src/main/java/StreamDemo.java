import javax.swing.text.html.Option;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.*;

/**
 * Created by dongchul on 2019-09-03.
 * https://futurecreator.github.io/2018/08/26/java-8-streams/ 코드 예제 프로젝트.
 */
public class StreamDemo {
    static int counter;
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

        startLine("Compare");

        List<String> resultString = lang.stream()
                .sorted(Comparator.comparingInt(String::length))
                .collect(Collectors.toList());

        System.out.println(resultString);

        resultString = lang.stream()
                .sorted((s1, s2) -> s2.length() - s1.length())
                .collect(Collectors.toList());

        System.out.println(resultString);
        endLine();

        startLine("Iterating");

        // 특정 결과를 반환 하지 않는 peek 메소드를 사용해서 연산 결과 중간을 확인 할 수 있다
        int sum = IntStream.of(1,3,5,7,9)
            .peek(System.out::println)
            .sum();

        endLine();

        startLine("Calculating");

        long count = IntStream.of(1, 3, 5, 7, 9).count();
        long sum2 = IntStream.of(1, 3, 5, 7, 9).sum();

        OptionalInt min = IntStream.of(1, 3, 5, 7, 9).min();
        OptionalInt max = IntStream.of(1, 3, 5, 7, 9).max();

        DoubleStream.of(1.1, 2.2, 3.3, 4.4, 5,5)
                .average()
                .ifPresent(System.out::println);

        endLine();

        startLine("Reduction");
        /**
         * accumulator : 각 요소를 처리하는 계산 로직, 각 요소가 올 때마다 중간 결과를 생성
         * identity : 계산을 위한 초기값으로 스트림이 비어서 계산할 내용이 없더라도 리턴
         * combiner : 병렬 스트림에서 나눠 계산한 결과를 하나로 합치는 동작
         * 1개
         * Optional<T> reduce(BinaryOperator<T> accumlator)
         * 2개
         * T reduce (T identity, BinaryOperator<T> accumulator)
         * 3개
         * <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner)
         */
        OptionalInt reduced = IntStream.range(1,4) // 인자 1
                .reduce(Integer::sum);

        System.out.println(reduced);

        int reduceTwoParams = IntStream.range(1,4) // 인자 2
                .reduce(10, Integer::sum);

        System.out.println(reduceTwoParams);

        Integer reduceParallel = Stream.of(1, 2, 3)
                .reduce(10, Integer::sum, (a,b) -> {
                    System.out.println("combiner was called");
                    return a+b;
                });

        System.out.println(reduceParallel);
        // Combiner 는 병렬 처리 시 각자 다른 쓰레드에서 실행한 결과를 마지막에 합치는 단계라 병렬 스트림에서만 동작한다.
        Integer reduceParallel2 = Arrays.asList(1, 2, 3).parallelStream()
                .reduce(10, Integer::sum, (a,b) -> {
                    System.out.println("combiner was called");
                    return a+b;
                });

        System.out.println(reduceParallel2);

        endLine();

        startLine("Collecting");

        List<Product> products = Arrays.asList(new Product(23, "potatos"),
                new Product(14, "orange"),
                new Product(13, "lemon"),
                new Product(23, "bread"),
                new Product(13, "sugar"),
                new Product(13, "sugar"));

        String listToString =
                products.stream()
                .map(Product::getName)
                .collect(Collectors.joining(",", "<", ">"));
        System.out.println(listToString);

        Double averageAmount = products.stream().collect(Collectors.averagingInt(Product::getAmount));
        System.out.println(averageAmount);
        Integer summingAmount =
                products.stream()
                .collect(Collectors.summingInt(Product::getAmount));

        summingAmount = products.stream()
                .mapToInt(Product::getAmount).sum();

        System.out.println(summingAmount);

        IntSummaryStatistics statistics =
                products.stream()
                .collect(Collectors.summarizingInt(Product::getAmount));
        System.out.println(statistics);

        Map<Integer, List<Product>> collectorMapOfLists =
                products.stream()
                .collect(Collectors.groupingBy(Product::getAmount));

        System.out.println(collectorMapOfLists.toString());

        Map<Boolean, List<Product>> mapPartitioned =
                products.stream()
                .collect(Collectors.partitioningBy(el -> el.getAmount() > 15));

        System.out.println(mapPartitioned);

        Set<Product> unmodifiableSet =
                products.stream()
                .collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));

        System.out.println(unmodifiableSet);

        Collector<Product, ?, LinkedList<Product>> toLinkedList =
                Collector.of(LinkedList::new,
                        LinkedList::add,
                        (first, second) -> {
                            first.addAll(second);
                            return first;
                        });

        LinkedList<Product> linkedListOfPersons =
                products.stream()
                .collect(toLinkedList);

        System.out.println(linkedListOfPersons);

        endLine();

        startLine("Matching");

        List<String> nameList = Arrays.asList("Eric", "Elena", "Java");

        boolean anyMatch = nameList.stream().anyMatch(name -> name.contains("a"));
        boolean allMatch = nameList.stream().allMatch(name -> name.length() > 3);
        boolean noneMatch = nameList.stream().noneMatch(name -> name.endsWith("s"));

        System.out.println(anyMatch);
        System.out.println(allMatch);
        System.out.println(noneMatch);
                
        endLine();

        startLine("process order");

        System.out.println(names.toString());
        names.stream()
                .filter(el -> {
                    System.out.println("filter was called : " + el);
                    return el.contains("a");
                })
                .map(el -> {
                    System.out.println("map was called : " + el);
                    return el.toUpperCase();
                })
        .findFirst();

        endLine();



        startLine("performence prove");
        List<String> namesList1 =  names.stream()
                .map(el -> {
                    wasCalled();
                    return el.substring(0, 3);
                })
                .skip(2)
                .collect(Collectors.toList());
        System.out.println(namesList1);
        System.out.println(counter);
        endLine();

        namesList1 = names.stream()
                .skip(2)
                .map(el -> {
                    wasCalled();
                    return el.substring(0, 3);
                })
                .collect(Collectors.toList());
        System.out.println(namesList1);
        System.out.println(counter);
        endLine();




    }

    public static void wasCalled() {
        counter++;
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
        counter = 0;
        System.out.println("=================================================");
    }
}
