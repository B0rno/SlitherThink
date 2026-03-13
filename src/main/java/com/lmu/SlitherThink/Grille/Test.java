import com.lmu.SlitherThink.Grille.Matrice;

public class Test {
    public static void main(String[] argv){
        int size = 8;
        Matrice mat = new Matrice(size,size+1);
        mat.loadSolution();
        mat.cliquer(1,1,0);
        for(int i=0; i<size; i++){
            for(int j=0; j<size+1; j++){
                mat.cliquer(i,j,0);
            }
        }
        System.out.println(mat);
    }
}
