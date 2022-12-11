# 개요

자바 11버전부터는 LTS(Long-Term Support)개념이 들어간다.

일반적인 소프트웨어는 새로운 버전이 출시될 때마다 이전 버전에 대한 업데이트를 중지하는데, LTS는 긴 기간 동안 지속적인 지원을 받을 수 있기 때문에, 기업이나 조직 등에서 사용하여 안정성을 어느정도 보장받을 수 있다.

## ✏️ Local-Variable Syntax for Lambda Parameters

Java10에서 `var`에 대해 알아봤다.  

Java11에서는 람다식에 `var`를 생략할 수 있게 하였다.

일반적으로 람다식에는 매개변수 타입을 명시해야하는데, Java11에서는 매개변수 타입을 생략할 수 있게 해준다는 것이다.

```java
//기존 사용방식
var arrInteger = new Integer[]{5, 9, 3, 6, 2, 4, 8, 7, 1, null};
//기본 명시적 타입
long cnt = Arrays.stream(arrInteger).filter(
        (Integer x) -> (x != null && x > 5)).count();
System.out.println(cnt);

//var를 쓴 명시적 타입
cnt = Arrays.stream(arrInteger).filter(
        (var x) -> (x!= null && x> 5)).count();
System.out.println(cnt);
```

이렇듯 매개변수 타입명을 적어줬는데 타입을 생략할 수 있게 한다는 것이다.

```java
//기본 암묵적 타입
cnt = Arrays.stream(arrInteger).filter(
        x -> (x != null && x > 5)).count();
System.out.println(cnt);
```

자바 컴파일러가 자동으로 타입을 추론한다. 이를 통해 코드를 짧게 쓸 수 있다.

`var`를 명시적으로 쓰는 경우에는 validation을 위해 쓰는 경우가 있다.

```java
var arrInteger = new Integer[]{5, 9, 3, 6, 2, 4, 8, 7, 1, null};
//var에 어노테이션을 추가 Null이면 에러
try {
    cnt = Arrays.stream(arrInteger).filter(
            (@Nonnull var x) -> (x > 5)).count();
    System.out.println(cnt);
}catch (NullPointerException e){
    System.out.println(e);
}

//var에 어노테이션을 추가 Null허용 null인경우 10으로 초기화해서 count값을 증가
cnt = Arrays.stream(arrInteger).filter(
        (@Nullable var x) -> {
            if(x == null)
                x = 10;
            return x > 5;
        }).count();
System.out.println(cnt);
}
```

이 경우 위의 결과는 에러가 발생할 것이고, 밑에는 **5**가 나올 것이다.

![image](https://user-images.githubusercontent.com/30401054/206902824-ad53befd-ec7c-437c-bb88-6acc6df90e14.png)
> 위의 모든 코드를 돌렸을때

두 가지를 혼용하지 말라고 한다. **타입을 모두 생략하거나 타입을 모두 적거나 모든 타입에대해 `var`를 쓰거나 하라고 한다.**

## ✏️ HTTP Client (Standard)

Java11버전 이전에는 HTTP통신을 하려면 많은 양의 코드를 요구하였다.

Java11에서는 새로운 `HttpClient`클래스가 생겨났고, 이 클래스가 제공하는 API들이 코드를 효율적으로 짤 수 있게 도와주었다.

```java
@Test
@DisplayName("자바 11버전 이전 포스트 요청")
void HttpClientBefore() throws IOException {
    URL urlObj = new URL("http://localhost:8080/api/test");
    HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
    con.setRequestMethod("POST");
    con.setRequestProperty("Content-Type", "application/json");

    // Send data
    con.setDoOutput(true);
    String data = "test";
    try (OutputStream os = con.getOutputStream()) {
        byte[] input = data.getBytes(StandardCharsets.UTF_8);
        os.write(input, 0, input.length);
    }

    // Handle HTTP errors
    if (con.getResponseCode() != 200) {
        con.disconnect();
        throw new IOException("HTTP response status: " + con.getResponseCode());
    }else {
        // Read response
        String body;
        try (InputStreamReader isr = new InputStreamReader(con.getInputStream());
                BufferedReader br = new BufferedReader(isr)) {
            body = br.lines().collect(Collectors.joining("n"));
        }
        System.out.println("Body: " + body);
        con.disconnect();
    }
}
```

이 코드를 실행 시키기 위해서 SpringMVC를 추가하고 Controller를 추가하였다.

컨트롤어에는 요청 바디에 있는 값을 그대로 돌려주는 역할을 한다. 즉, body에 **test**를 포함해 Post요청을 하였으므로 **test**가 반환되어야 할 것이다.

![image](https://user-images.githubusercontent.com/30401054/206902962-96243721-4c5a-46a5-82f0-c5a83d222440.png)

코드가 정말 긴데, Java 11버전부터 새로운 `HttpClient`클래스가 생겼다. 

이를 통해 이전 버전에 비해 매우 짧고 효율적은 코드를 작성할 수 있게 되었다.

```java
@Test
@DisplayName("자바 11이후 포스트 요청")
void HttpClientNew() throws IOException, InterruptedException {
    URL url = new URL("http://localhost:8080/api/test");
    HttpClient client = HttpClient.newHttpClient();

    String data = "test";
    HttpRequest request =
            HttpRequest.newBuilder()
                    .uri(URI.create(String.valueOf(url)))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(data))
                    .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() != 200) {
        throw new IOException("HTTP response status: " + response.statusCode());
    }
    System.out.println("Body: "+response.body());
}
```

![image](https://user-images.githubusercontent.com/30401054/206903052-3a741383-1774-486b-a7ec-a380e8cdb8d4.png)

새로생긴 `HttpClient`클래스는 매우 다양한 기능을 수행한다고 한다.

- 위 코드에서 `BodyPublisher.ofString()`을 통해 문자열을 보냈는데, `BodyPublisher`클래스에 있는 `ofByteArray()`,`ofByteArrays()`,`ofFile()` 등을 통해 여러 데이터를 보낼 수 있다.
- 마찬가지로 응답에 있는 `BodyHandlers`도 `ofString()`을 통해 응답을 문자열로 받을 수 있지만 파일, 배열 등으로도 받을 수 있다.
- HttpClient는 HTTP/2.0을 지원하기 시작.
- HttpClient는 비동기적 요청도 지원한다. 

```java
@Test
@DisplayName("자바 11 비동기 post요청")
void AsyncPostTest() throws MalformedURLException, ExecutionException, InterruptedException {
    URL url = new URL("http://localhost:8080/api/test");
    HttpClient client = HttpClient.newHttpClient();

    String data = "kms async test";
    HttpRequest request =
            HttpRequest.newBuilder()
                    .uri(URI.create(String.valueOf(url)))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(data))
                    .build();

    //요청이 비동기적이라 결과를 받아, 저장할 곳이 필요함. CompletableFuture클래스를 이용.
    //사실상 요청이 완료될때까지 프로그램을 대기시키기에 비동기라 할 수 없겠지만 출력물으 보여주기 위해 어쩔 수 없음. 실제 업무에서는 이렇게 쓰지 말 것.
    CompletableFuture<HttpResponse<String>> responseFuture = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

    HttpResponse<String> response = responseFuture.get();
    System.out.println(response.body());
}
```

![image](https://user-images.githubusercontent.com/30401054/206904518-36b8e054-8a77-4299-9ccc-9dbae8e3f956.png)

## ✏️ New Collection.toArray() Method

기존 `Collection` 인터페이스는 두 가지의 `toArray()`라는 컬렉션을 배열로 바꾸는 함수를 제공했다.

```java
List<String> list = List.of("kms","test","java11");

Object[] strings1 = list.toArray();

String[] strings2 = list.toArray(new String[list.size()]);
String[] strings3 = list.toArray(new String[0]);
```

인자를 받지 않는 `toArray()`의 경우 리턴값이 Object형이다. 이는 **Type erasure**라는 현상 때문인데, Type erasure란 자바에서 리스트를 생성할때 제너릭을 사용하면 **컴파일**시점에 타입이 결정이 된다. 근데 컴파일러가 코드를 컴파일하면서 제너릭의 타입 정보는 사라지게 되는데, 이렇게 컴파일 시점에 타입 정보가 사라지는 현상을 Type erasure라고 한다. 따라서 리스트의 타입 정보는 런타임때에 알 수 없어서 오브젝트 형으로 반환되는 것이다.

즉, `List<String> list`는 컴파일때 타입 소거로 인해 단순히 `List`가 되어버리기에 `list.toArray()`는 `Object`형으로 반환해야 한다.

두 번째 `toArray()` 메서드는 매개변수로 배열을 지정할 수 있는 오버로드 된 메서드이다. 이 메서드는 매개변수로 지정한 배열의 타입을 요청하게 된다. 만약 지정한 배열의 크기가 리스트의 요소 개수보다 적다면, 새로운 배열을 생성하여 리스트의 요소를 복사한 후 리턴하고,지정한 배열의 크기가 리스트의 요소 개수보다 크거나 같다면, 지정한 배열에 리스트의 요소를 복사한 후 리턴한다.

Java 11에서는 새로운 방식으로 쓸 수 있게 되었는데,

```java
String[] strings = list.toArray(String[]::new);
```

이 방식을 사용하면 `Collection` 클래스가 절달된 배열 생성자의 참조를 사용하여 필요한 크기의 배열을 만들 수 있게 된다.

그런데 이 기능은 잘 사용되지 않는다. 그 이유는 컬렉션 인터페이스에만 구현되어 있기 때문이다.

이 코드의 구현방식을 보면 빈 배열을 만들고, 기존 `toArray()`를 호출하는 방식으로 되어 있다.

```java
default <T> T[] toArray(IntFunction<T[]> generator) {
    return toArray(generator.apply(0));
}
```

문제는 이 메소드를 오버라이드 하지 않는 컬렉션 구현체가 많다는 것이다.


## ✏️ New String Methods

Java 11버전에서 `String`클래스에 함수들이 추가적으로 생겼다.

### String.strip(), stripLeading(), stripTailing()

예시를 보는게 빠르다.

```java
// 문자열 앞 뒤 공백 제거 함수 strip()
String stripTest = "              1 2 3 4 5 6          ";
System.out.println(stripTest.strip() + "7");

//문자열 앞 공백 제거 함수 stripLeading()
System.out.println(stripTest.stripLeading() +"7");

//문자열 뒤 공백 제거 함수 stripTailing()
System.out.println(stripTest.stripTrailing() + "7");

// trim() vs strip()
String trimTest = "\u2003\u2003\u2003\u2003\u2003kms";

System.out.println(trimTest);
System.out.println(trimTest.trim());
System.out.println(trimTest.strip());

```

![image](https://user-images.githubusercontent.com/30401054/206907809-2fb56c2f-7212-4fe7-ba46-842838891ad6.png)

기존 `trim()`와 뭐가 다른걸까?

- trim(): 이 함수는 '\u0020'이하의 공백들만 제거한다.
- strip(): 유니코드의 공백들을 제거한다. 유니코드에는 '\u0020' 이하의 공백문자 뿐만아니라 다양한 공백문자가 있다. 그 중 하나가 위와 같은 '\u2003'같은 것들이다.

### String.isBlank()

이 함수는 각 요소들이 `Character.isWhitespace()`가 true이면 true를 리턴한다. `Character.isWhitespace()`함수는 공백 문자인지 여부를 판단하는 함수라서 문자열에 속해있느 모든 문자가 공백인지를 확인하여 공백이면 true를 리턴한다는 것이다.

```java
//String.isBlank()
String isBlankTest = "          ";
//True
System.out.println(isBlankTest.isBlank());
```

### String.repeat()

이 함수는 주어진 String을 연속적으로 나열하는 기능을 한다.

```java
//String.repeat()
//hi?hi?hi?hi?hi?hi?hi?hi?hi?hi?
System.out.println("hi?".repeat(10));
```

### String.lines()

이 함수는 line을 기준으로 String을 분리하는 기능을 한다. 리턴형은 `Stream`이다.

```java
//String.lines()
Stream<String> lines = "foo\nbar\nbaz".lines();
lines.forEachOrdered(System.out::println);
```

## ✏️ Files.readString() und writeString()


