import java.util.Random;
import java.util.concurrent.BrokenBarrierException;

public class Elfo implements Runnable{

    //Variàveis de Instância
    int id;
    PapaiNoel papaiNoel;

    //Gerador Randomico
    Random random = new Random();

    //Construtor
    public Elfo(PapaiNoel papaiNoel, int id) {
        this.papaiNoel = papaiNoel;
        this.id = id;
    }

    @Override
    public void run() {
        while(papaiNoel.TRABALHANDO){
            //Enquato o Papai Noel não se aposentar
            // o elfo trabalha

            //O Elfo trabalha até ter algum problema
            try {
                Thread.sleep(random.nextInt(10000));
                System.out.println("O Elfo "+ id + "precisa de ajuda");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //O elfo tem um problema e vai à
            //porta do Papai Noel
            //e espera na fila
            try {
                papaiNoel.elfosNaPorta.acquire();
                papaiNoel.tamanhoDaFila++;
                System.out.println("O Elfo "+ id + "está na porta.");
                papaiNoel.ultimoElfoNaFila = id;
                papaiNoel.filaDeElfos.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

            //Se o elfo for o último da fila, ele acorda o Papai Noel
            if(id == papaiNoel.ultimoElfoNaFila){
                try {
                    papaiNoel.bomVelhinho.acquire();
                    papaiNoel.acordar();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
