public class MultiImplementClass implements Interface1,Interface2{
    @Override
    public void method1(String str) {

    }

    @Override
    public void method2() {

    }

    @Override
    public void log(String str) {
        System.out.println("MultiImplementClass logging::"+str);
        Interface1.print("abc");
    }
}
