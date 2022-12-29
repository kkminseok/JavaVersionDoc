# 🔅 Java8

2014년 3월 18일에 출시된 Java버전이다. 크게 다음과 같은 변화가 있었다.

1. Functional Interfaces 와 Lambda표현식 추가
2. `forEach()`메소드 추가.
3. Interface에 Default Static Method 추가
4. Stream API 추가
5. 새로운 날짜/시간 API 추가
6. 동시성 관련 API 향상
7. Java IO 향상
8. Optional Class 추가

-----

## ✏️ 1. Functional interface, Lambda 추가

### Functional interface

- Functional Interface: 1개의 추상 메소드를 갖고 있는 인터페이스. *Single Abstract Method*라고도 한다.

왜 함수형 인터페이스는 추상 메서드를 1개만 가져야할까? 이는 인터페이스의 메소드가 단 하나의 기능을 제공해야하기 때문이다.

그래서 혹여나 개발자가 2개의 추상 메서드를 적용시킬 수 있으므로 `@FunctionalInterface`라는 어노테이션을 인터페이스 위에 붙여서 컴파일러에게 검증을 요청하고, 컴파일 에러를 발생시킨다. 즉, `@Override`와 비슷하게 개발자의 실수와 협업시의 소통의 목적이 비슷한 것이다.

그리고 이를 사용하는 이유는 자바8에 추가된 Lambda식이 함수형 인터페이스로만 접근이 가능하기 때문이다.

이는 자바에서 함수형 개발 패러다임을 지원하기 시작하면서 인터페이스의 어떤 로직을 **값**으로 쓰기 위함이다.

```java
public interface FunctionalInterfaceClass {
    public abstract void outputText(String text);
}


@Test
void java8FunctionalTest(){
    FunctionalInterfaceClass func = text -> {
        System.out.println(text);
    };
    //output: java8 버전의 람다와 함수형 인터페이스 테스트 
    func.outputText("java8 버전의 람다와 함수형 인터페이스 테스트 ");
}

```

위는 람다식의 구현예제이고, 익명 클래스로 구현도 가능하다.

```java
@Test
void java8FunctionalTestAnonymousfunction(){
    FunctionalInterfaceClass func = new FunctionalInterfaceClass() {
        @Override
        public void outputText(String text) {
            System.out.println(text);
        }
    };
    func.outputText("java8 버전의 람다와 함수형 인터페이스 테스트 ");

    
    FunctionalInterfaceClass func2 = new FunctionalInterfaceClass() {
        @Override
        public void outputText(String text) {
            System.out.println(text.toUpperCase());
        }
    };
    func2.outputText("java8 버전의 람다와 함수형 인터페이스 테스트 ");
}


//output
//java8 버전의 람다와 함수형 인터페이스 테스트 
//JAVA8 버전의 람다와 함수형 인터페이스 테스트 
```

확실히 람다식을 썼을때와 비교해서는 익명 클래스는 보기 복잡함과 간결함이 부족하다.

### Lambda

람다를 정리는 이 글의 목적이 아니므로 간단히 설명하고 넘어가야한다.

- 람다 표현식: 익명 함수로, 이름이 없고 식별자만 있는 함수이다. 일반적으로 다른 함수의 매개변수로 정의되는 곳에서 정확하게 정의된다. 

기본적인 구조는 다음과 같다.

```text
(parameters) -> expression
```

간단하게 이러한 예제로 덧셈 예제가 있는데 위의 구조를 사용하면

`(int x, int y) -> x+y`로 표현할 수 있다. 이 함수의 이름은 없고 식별자만 있다는것이다.

람다를 사용하기 위해서 함수형 인터페이스를 하나 만든다.

```java
@FunctionalInterface
public interface LambdaSumInterface {
    public abstract int sum(int x, int y);
}
```

그리고 위에서 정의한 구조 그대로 사용하면 된다.

```java
@Test
void lambdaSumTest(){
    LambdaSumInterface result = (int x,int y) -> x+y;
    //10
    result.sum(5,5);
}
```

간단하고 자주 쓰이는 함수형 인터페이스들은 매 번 선언하기가 귀찮을 것이다 그래서 자바에서 기본적으로 제공하는 함수형 인터페이스들이 있다.

- Runnable: 인자를 받지 않고 리턴값도 없는 인터페이스

```java
@Test
void runAbleTest(){
    Runnable consoleOutPut = () -> System.out.println("인자도 없고 리턴도 없다.");
    //run()을 통해 호출 가능.
    //output: 인자도 없고 리턴도 없다.
    consoleOutPut.run();
}
```

- Supplier: 인자를 받지 않고 T 타입의 객체를 리턴한다.

```java
@Test
void supplierTest(){
    Supplier<String> consoleOutput = () -> "abcdefg";
    //get()으로 리턴값을 받아올 수 있다.
    String output = consoleOutput.get();
    //2차 작업 대문자로 변환
    System.out.println(output.toUpperCase());
}
```

- Consumer: T타입의 객체를 인자로 받고 리턴은 없다.

```java
@Test
void consumerTest(){
    Consumer<String> myBlogURL = username -> System.out.println("https://" + username + "/github.io");
    //output: https://kkminseok/github.io
    myBlogURL.accept("kkminseok");
}
```

- Function: `Function<T, R>`은 T타입의 인자를 받고, R타입의 객체를 리턴

```java
@Test
void functionTest(){
    Function<String,String> myBlogURL = username ->  "https://" + username + "/github.io";
    String url = myBlogURL.apply("kkminseok");
    //output: https://kkminseok/github.io
    System.out.println(url);
}
```

- Predicate: `Predicate<T>`은 T타입의 인자를 받고, `boolean`을 리턴

```java
@Test
void predicateTest(){
    Predicate<String> isMyBlogURL = url -> url.equals("https://kkminseok/github.io");
    boolean result = isMyBlogURL.test("https://kkminseok/github.io");
    //true
    System.out.println(result);
}
```

좀 더 복잡한 기능들도 존재하고 정리하자니 이글이 너무 무거워질까봐 궁금하면 찾아보자.


## ✏️ 2. forEach() 메소드 추가

`forEach()` 메소드는 List, Map과 같은 여러 자료구조를 순회하면서 개발자가 지정한 작업을 수행하게 도와준다.

