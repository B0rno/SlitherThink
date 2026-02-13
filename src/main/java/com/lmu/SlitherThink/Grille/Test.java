public class Test {
    public static void main(String[] argv){
        Matrice mat = new Matrice(5,5);
        mat.cliquer(1,1,2);
        mat.cliquer(2,2,3);
        System.out.println(mat);
    }
}
