import java.util.Random;

public class Atividade1_JavaThread {
    private int TAM;
    public static int MaxThreads = 1;
    public static int grid[][];
    public static int newgrid[][];

    // Construtor
    public Atividade1_JavaThread(int dim){
        this.TAM = dim;
        this.grid = new int[this.TAM][this.TAM];
        this.newgrid = new int[this.TAM][this.TAM];

        Random gerador = new Random(1985);
        for(int i = 0; i<this.TAM; i++) {   
            for(int j = 0; j<this.TAM; j++) {
                grid[i][j] = gerador.nextInt(2147483647) % 2;  
            }
        }
    }

    // Imprime as duas primeiras geracoes
    public void printGen(){
        for(int i=0;i<this.TAM;i++){
            for(int j = 0; j<this.TAM; j++){
                System.out.print(grid[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println("");
    }

    // Imprime matriz que indica numero de vizinhos de cada posicao
    /*public void printMatNeighbors(){    
        for(int i=0;i<this.TAM; i++){     
            for(int j = 0; j<this.TAM; j++){
                System.out.print(this.getNeighbors(i,j) + " ");
            }
            System.out.println("");
        }
    }*/

    // Define a nova geração como a geração atual
    public void newGen(){
        for(int i=0;i<this.TAM; i++){     
            for(int j = 0; j<this.TAM; j++){
                grid[i][j] = newgrid[i][j];
            }
        }
    }

    // Copia o quarto do grid gerado para a proxima geração
    public void copyGen(int offset, int quarterGen[][]){
        for(int i=0;i<(this.TAM/MaxThreads); i++){     
            for(int j = 0; j<this.TAM; j++){
                newgrid[(offset+i)][j] = quarterGen[i][j];
            }
        }
    }

    // Conta quantas celulas estao vivas na geracao
    public int contPopulation(){
        int cont = 0;

        for(int i=0;i<this.TAM; i++){     
            for(int j = 0; j<this.TAM; j++){
                if (this.grid[i][j] == 1)
                    cont++;
            }
        }

        return cont;
    }

    public static void main(String[] args) throws InterruptedException{
        int tam = 2048;
        Thread[] th;
        RunTh[] rh;
        rh = new RunTh[MaxThreads];
		th = new Thread[MaxThreads];
        Atividade1_JavaThread gen1 = new Atividade1_JavaThread(tam);
        
        long startTime = System.currentTimeMillis();
        System.out.println("Condicao Inicial: " + gen1.contPopulation() + " Celulas Vivas");
        //gen1.printGen();
        //System.out.println("");
        for(int k=0;k<2000;k++){
            for(int i=0; i<MaxThreads; i++) {
                rh[i] = new RunTh(tam, MaxThreads, (i*512), grid);
                th[i] = new Thread(rh[i]);
                th[i].start();
            }
            for(int i=0; i<MaxThreads; i++) {
                th[i].join();
                gen1.copyGen((i*512),rh[i].gridAux);
            }
            gen1.newGen();
            //gen1.printGen();
        }
        System.out.println("Ultima Geracao: " + gen1.contPopulation() + " Celulas Vivas");
        long calcTime = (System.currentTimeMillis() - startTime)/1000;
        System.out.println(calcTime + "seg");
    }
}
