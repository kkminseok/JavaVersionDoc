public class Java8Test {

    @Test
    void init(){
        System.out.println("hello");
    }

    @Test
    void foreachTest(){
        List<String> subList = new ArrayList<String>();
        subList.add("Carrot");
        subList.add("Potato");
        subList.add("Cauliflower");
        subList.add("LadyFinger");
        subList.add("Tomato");
        subList.forEach(sub -> System.out.println(sub));
    }

    @Test
    void foreachTestBeforeJava8(){
        List<String> subList = new ArrayList<String>();
        subList.add("Carrot");
        subList.add("Potato");
        subList.add("Cauliflower");
        subList.add("LadyFinger");
        subList.add("Tomato");
        Iterator<String> it = subList.iterator();
        while(it.hasNext()){
            System.out.println(it.next());
        }
    }

    @Test
    void foreachTestDetail(){
        List<String> subList = new ArrayList<String>();
        subList.add("Carrot");
        subList.add("Potato");
        subList.add("Cauliflower");
        subList.add("LadyFinger");
        subList.add("Tomato");

        subList.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        });
    }

    @Test
    void defaultMethodInInterface(){
        DefaultMethodTestImpl interface1 = new DefaultMethodTestImpl();
        interface1.printThisInterface();
        //interface에서 직접호출
        DefaultMethodTestInterface.printTest();
    }

    @Test
    void defaultMethodInterfaceMulti(){
        MultiImplementClass multiImplementClass = new MultiImplementClass();
        multiImplementClass.method1("kms");
        multiImplementClass.method2();
        multiImplementClass.log("test.txt");
    }

    @Test
    void streamTest(){
        List<Integer> myList = new ArrayList<>();
        for(int i=0; i<1000000; i++) myList.add(i);

        Stream<Integer> sequentialStream = myList.stream().sequential();
        Stream<Integer> parallelStream = myList.stream().parallel();

        long startTime = System.currentTimeMillis();
        Stream<Integer> integerStream = parallelStream.filter(p -> p > 500000);
        integerStream.forEach(i -> System.out.println(i));
        long endTime = System.currentTimeMillis();
        long duration1 = endTime - startTime;


        startTime = System.currentTimeMillis();
        Stream<Integer> integerStream1 = sequentialStream.filter(p -> p > 500000);
        integerStream1.forEach(i -> System.out.println(i));
        endTime = System.currentTimeMillis();
        long duration2 = endTime - startTime;
        System.out.println("parallel test.txt took " + duration1 + " milliseconds");
        System.out.println("sequential test.txt took " + duration2 + " milliseconds");
    }

    @Test
    void fileListTest(){
        Path dir = Paths.get("/");
        try (Stream<Path> stream = Files.list(dir)) {
            stream.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void fileLinesTest() throws IOException {
        Path file = Paths.get("test.txt");
        Stream<String> lines = Files.lines(file);
        lines.forEach(System.out::println);
    }

    @Test
    void OptionalTest(){
        // Creating an Optional object from a non-null value
        Optional<String> optional1 = Optional.of("value");

        // Creating an Optional object from a null value
        Optional<String> optional2 = Optional.ofNullable(null);

        // Creating an empty Optional object
        Optional<String> optional3 = Optional.empty();

        // Check if an Optional object has a value
        if (optional1.isPresent()) {
            System.out.println("optional1 has a value: " + optional1.get());
        }

        // Get the value of an Optional object, or a default value if it is empty
        String value1 = optional1.orElse("default value");
        String value2 = optional2.orElse("default value");
        System.out.println("value1:" + value1);
        System.out.println("value2:" + value2);

        // Use the value of an Optional object if it is present, or throw an exception if it is empty
        try {
            String value3 = optional1.orElseThrow(IllegalStateException::new);
        } catch (IllegalStateException e) {
            System.out.println("optional1 is empty");
        }

        try {
            String value4 = optional2.orElseThrow(IllegalStateException::new);
        } catch (IllegalStateException e) {
            System.out.println("optional2 is empty");
        }

        // Use the value of an Optional object if it is present, or execute a function if it is empty
        String value5 = optional1.orElseGet(() -> "default value from function");
        String value6 = optional2.orElseGet(() -> "default value from function");

        System.out.println("value5: " + value5);
        System.out.println("value6: " + value6);
    }
}
