import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.text.DateFormat;
import java.time.DayOfWeek;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Calendar.LONG;

//기록용입니다 개인 기호에 맞춰서 쓰면 됩니다.

public class VersionTest {
    private static final Logger logger = LoggerFactory.getLogger(VersionTest.class);

    @Test
    @DisplayName("1.var를 사용할 수 있다.")
    void varTypeVariable(){
        var i = 0;
        var stringTest = "kms is kms";
        var list = List.of(1,2,3,4,5);
        var httpClient = HttpClient.newBuilder().build();
        logger.debug("i: {}", i);
        logger.debug("stringTest: {}", stringTest);
        logger.debug("list: {}", list);
        logger.debug("httpClient: {}", httpClient);
    }

    @Test
    @DisplayName("2. 불변 컬렉션이 생겼다.")
    void immutableCollections(){
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        List<Integer> unmodifiedList = Collections.unmodifiableList(list);
        try{
            unmodifiedList.add(4);
        }catch (UnsupportedOperationException e){
            logger.warn("error: ",e);
        }finally {
            list.add(10);
            logger.debug("list: {}", list);
            logger.debug("unmodifiedList: {}", unmodifiedList);
        }
    }

    @Test
    @DisplayName("2.1 List.copyOf(), Set.copyOf(),Map.copyOf()")
    void immutableCollectionsFunction(){
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        List<Integer> immutable = List.copyOf(list);
        list.add(4);
        logger.debug("immutableList: {}",immutable);

        try{
            immutable.add(10);
        }catch (UnsupportedOperationException e){
            logger.warn("error: ",e);
        }
    }

    @Test
    @DisplayName("2.2 Collectors.toUnmodifiableList(), toUnmodifiableSet(), and toUnmodifiableMap()")
    void toUnmodifiedCollectors(){
        List<Integer> list = IntStream.rangeClosed(1,5).boxed().collect(Collectors.toList());
        Set<Integer> set = IntStream.rangeClosed(1,5).boxed().collect(Collectors.toSet());
        Map<Integer,String> map = IntStream.rangeClosed(1,5).boxed().collect(Collectors.toMap(Function.identity(),String::valueOf));

        list.add(8);
        set.add(8);
        map.put(8,"8");

        logger.debug("list: {}",list);
        logger.debug("set: {}",set);
        logger.debug("map: {}",map);

        //불변
        List<Integer> immutableList = IntStream.rangeClosed(1,5).boxed().collect(Collectors.toUnmodifiableList());
        Set<Integer> immutableSet = IntStream.rangeClosed(1,5).boxed().collect(Collectors.toUnmodifiableSet());
        Map<Integer,String> immutableMap = IntStream.rangeClosed(1,5).boxed().collect(Collectors.toUnmodifiableMap(Function.identity(),String::valueOf));

        //error
        //immutableList.add(4);
    }

    @Test
    @DisplayName("3. Optional.orElseThrow()등장")
    void orElseThrowTest(){

        Optional<String> result = Optional.empty();
        //result.orElseThrow();
        //result.get();
    }

    @Test
    @DisplayName("Locale 테스트")
    void LocaleTest(){
        Locale locale = Locale.forLanguageTag("de-DE-u-cu-usd-fw-wed-tz-uslax");

        Currency currency = Currency.getInstance(locale);

        Calendar calendar = Calendar.getInstance(locale);
        DayOfWeek firstDayOfWeek = DayOfWeek.of((calendar.getFirstDayOfWeek() + 5) % 7 + 1);

        DateFormat dateFormat = DateFormat.getTimeInstance(LONG, locale);
        String time = dateFormat.format(new Date());

        System.out.println("currency       = " + currency);
        System.out.println("firstDayOfWeek = " + firstDayOfWeek);
        System.out.println("time           = " + time);
    }
}
