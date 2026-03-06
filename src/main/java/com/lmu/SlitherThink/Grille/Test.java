public class Test {
    public static void main(String[] argv){
        Matrice mat = new Matrice(8,8);
        mat.loadSolution();
        mat.cliquer(1,1,0);
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                mat.cliquer(i,j,0);
            }
        }
        System.out.println(mat);
    }
}
