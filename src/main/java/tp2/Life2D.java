package tp2;

import java.util.BitSet;

public class Life2D {

    BitSet arr[];
    private int M;

    public Life2D(int M){
        this.M = M;
        arr = init(M);
    }

    public void run(long generations){
        while (generations > 0){
            singleGeneration();
            generations--;
        }
    }

    private void singleGeneration(){
        BitSet ans[] = init(M);
        for(int i = 0; i < M; i++){
            for (int j = 0; j < M; j++){
                int alive = countAlive(arr, i, j, M);
                if (arr[i].get(j)) {
                    //Cell is alive
                    if(alive == 2 || alive == 3)
                        ans[i].set(j);
                } else if(alive == 3) {
                    //Cell is dead and exactly 3 neighbours are alive
                    ans[i].set(j);
                }
            }
        }
        print();
        arr = ans;
    }

    public void set(int i, int j){
        checkConstraints(i, j);
        arr[i].set(j);
    }

    private void checkConstraints(int i, int j){
        if(isOutOfBounds(i, j, M))
            throw new IndexOutOfBoundsException();
    }

    private static boolean isOutOfBounds(int i, int j, int M){
        return i < 0 || i >= M || j < 0 || j >= M;
    }

    private static int countAlive(BitSet set[], int i, int j, int M){
        int ans = 0;
        for(int x = i-1; x <= i+1; x++)
            for(int y = j-1; y <= j+1; y++)
                if((!(i == x && j == y)) && !isOutOfBounds(x, y, M) && set[x].get(y))
                    ans ++;

        return ans;
    }

    private static BitSet[] init(int M){
        BitSet ans[] = new BitSet[M];
        for(int i = 0; i < M; i++)
            ans[i] = new BitSet(M);
        return ans;
    }

    public void print(){
        for(int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                if (arr[i].get(j))
                    System.out.print("O ");
                else
                    System.out.print("X ");
            }
            System.out.println();
        }
        System.out.println();
    }

}
