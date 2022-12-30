public interface DefaultMethodTestInterface {

    default void printThisInterface(){
        System.out.println("DefaultMethodTestInterface입니다.");
    }

    static void printTest(){
        System.out.println("testestsetst");
    }
}
