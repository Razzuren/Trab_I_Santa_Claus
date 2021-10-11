import java.util.Random;
import java.util.concurrent.BrokenBarrierException;

public class Rena implements Runnable{

    //Variáveis de Instância
    int id;
    String nome;
    PapaiNoel papaiNoel;

    //Gerador Randomico
    Random random = new Random();

    //Construtor
    public Rena(PapaiNoel papaiNoel, int id, String nome){
        this.papaiNoel = papaiNoel;
        this.id = id;
        this.nome = nome;

    }

    @Override
    public void run() {
        //Enquato o Papai Noel não se aposentar
        // a rena trabalha e tira férias

        while(papaiNoel.TRABALHANDO){

            //Rena retorna das férias
            try {
                Thread.sleep(4000+random.nextInt(10000));
                System.out.println(nome + " voltou das férias");

                //Atualiza o id da última rena que retornou
                papaiNoel.ultimaRenaNoEstabulo = id;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //A rena vai esperar no estábulo
            try {
                papaiNoel.estabulo.await();

                //Quando as nove renas chegarem,
                //atualiza o verificador para verdadeiro
                //se ja não foi atualizado
                if(!papaiNoel.renasChegaram)
                    papaiNoel.renasChegaram = true;

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

            //Se a rena foi a última que chegou, ela acorda o Papai Noel
            if(id == papaiNoel.ultimaRenaNoEstabulo){
                try {
                    System.out.println(nome + " está indo acordar o Papai Noel.");
                    papaiNoel.acordar();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //Após entregar os presentes, a rena
            //retona ao estábulo e sai de férias.
            try {
                papaiNoel.estabulo.await();
                System.out.println(nome + " saiu de férias.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

        }
    }
}
