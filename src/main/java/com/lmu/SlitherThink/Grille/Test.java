public class Test {
    public static void main(String[] argv){
        Case case1 = new Case(2);
        System.out.println(case1);
        case1.updateTrait(0);
        case1.updateTrait(2);
        System.out.println(case1);
    }
}
