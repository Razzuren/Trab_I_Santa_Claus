import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class PapaiNoel extends Thread{

    //Variáveis de Dimensão
    volatile boolean TRABALHANDO = true;
    static final int  N_ELFOS = 10;
    static final int N_RENAS = 9;
    static final int ELFOS_PARA_ACORDAR = 3;

    //Variàveis de Controle
    Semaphore bomVelhinho;
    Semaphore elfosNaPorta;
    CyclicBarrier filaDeElfos;
    CyclicBarrier estabulo;
    volatile int tamanhoDaFila =0;
    volatile int ultimoElfoNaFila;
    volatile int ultimaRenaNoEstabulo;
    volatile boolean renasChegaram = false;

    //Gerador Randomico
    Random random = new Random();

    //Arrays de Threads
    ArrayList<Rena> renas;
    ArrayList<Elfo> elfos;
    ArrayList<Thread> t_renas;
    ArrayList<Thread> t_elfos;

    //Nome das Renas
    String[] nomes = new String[]{
            "Rodolfo", "Corredora", "Dançarina",
            "Empinadora", "Raposa", "Cometa",
            "Cupido", "Trovão",
            "Relâmpago", "Bernardo"
    };

    //Método que acorda o Papai Noel
    public synchronized void acordar() throws InterruptedException {
        //Notifica a thread para reiniciar
        super.notify();
        System.out.println("O bom velhinho acordou!");

        //As renas possuem prioridade para serem
        //atendidas pelo Papai Noel
        //por isso, se os elfos chegarem para
        //serem atendidos mas as renas estiverem
        //no estábulo, ele sai para entregar os presentes
        if(renasChegaram){
            System.out.println("O Papai Noel está atrelando suas renas");
            Thread.sleep(1000);
            System.out.println("O Papai Noel saiu para entregar os presentes");
            renasChegaram=false;

        } else {
            System.out.println("Papai Noel está atendendo os elfos");
            Thread.sleep(5000);
            tamanhoDaFila= 0;
            elfosNaPorta.release(3);
            bomVelhinho.release();
        }

        //Após trabalhar, o bom velhinho volta a dormir
        dormir();
    }

    //Método que coloca o Papai Noel para Dormir
    private void dormir() throws InterruptedException {
        System.out.println("O bom velhinho foi dormir");
        super.wait();
    }

    //Método que inicia a thread do Papai Noel
    @Override
    public void start(){
        //O modificador 'fair' nos semáforos
        //garante que as threads sejam atendidas
        //na ordem em que requisitaram

        bomVelhinho = new Semaphore(1,true);
        elfosNaPorta = new Semaphore(ELFOS_PARA_ACORDAR,true);

        //Barreiras de execução dos elfos e renas
        filaDeElfos = new CyclicBarrier(ELFOS_PARA_ACORDAR);
        estabulo = new CyclicBarrier(N_RENAS);

        //Arrays das threads
        renas = new ArrayList<>();
        t_renas = new ArrayList<>();
        elfos = new ArrayList<>();
        t_elfos = new ArrayList<>();

        //inicializa a thread das renas
        for(int i = 0; i < N_RENAS; i++){
            Rena r = new Rena(this,renas.size(),nomes[i]);
            Thread t = new Thread(r);
            t.start();
            renas.add(r);
            t_renas.add(t);
        }

        //inicializa a thread dos elfos
        for(int i = 0; i < N_ELFOS; i++){
            Elfo e = new Elfo(this,elfos.size());
            Thread t = new Thread(e);
            t.start();
            elfos.add(e);
            t_renas.add(t);
        }
    }

    //Método Principal
    public static void main(String[] args) {
        PapaiNoel papaiNoel = new PapaiNoel();
        papaiNoel.start();
    }

}
