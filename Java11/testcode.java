package com.example.javaversiontest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

@SpringBootTest
class JavaVersionTestApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    @DisplayName("1. 람다에 var를 명시적으로 쓸 수 있게 되었다.")
    void LambdaParameters(){
        var arrInteger = new Integer[]{5, 9, 3, 6, 2, 4, 8, 7, 1, null};
        //기본 명시적 타입
        long cnt = Arrays.stream(arrInteger).filter(
                (Integer x) -> (x != null && x > 5)).count();
        System.out.println(cnt);

        //기본 암묵적 타입
        cnt = Arrays.stream(arrInteger).filter(
                x -> (x != null && x > 5)).count();
        System.out.println(cnt);

        //var를 쓴 명시적 타입
        cnt = Arrays.stream(arrInteger).filter(
                (var x) -> (x!= null && x> 5)).count();
        System.out.println(cnt);

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

}

