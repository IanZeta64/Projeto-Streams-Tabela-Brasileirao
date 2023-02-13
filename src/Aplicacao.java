import dominio.PosicaoTabela;
import dominio.Resultado;
import impl.Brasileirao;
import impl.CampeonatoBrasileiroImpl;
import java.io.IOException;
import java.util.*;

public class Aplicacao {

    public static void main(String[] args) throws IOException {

        boolean continuar;
        do {
            int ano = getAno();

            Brasileirao resultados = new CampeonatoBrasileiroImpl(ano);
            System.out.printf("%15s .::BRASILEIRÃO %d::. %15s %n","", ano, "");
            System.out.printf( "%38s %n%n","──────────────────────");

            imprimirEstatisticas(resultados);

            imprimirTabela(resultados.getTabela());

            continuar = getResposta();
        }while(continuar);
    }

    private static void imprimirEstatisticas(Brasileirao brasileirao) {
        IntSummaryStatistics statistics = brasileirao.getEstatisticasPorJogo();

        System.out.println("Estatisticas (Total de gols) - " + statistics.getSum());
        System.out.println("Estatisticas (Total de jogos) - " + statistics.getCount());
        System.out.println("Estatisticas (Media de gols) - " + statistics.getAverage());

        Map.Entry<Resultado, Long> placarMaisRepetido = brasileirao.getPlacarMaisRepetido();

        System.out.println("Estatisticas (Placar mais repetido) - "
                + placarMaisRepetido.getKey() + " (" +placarMaisRepetido.getValue() + " jogo(s))");

        Map.Entry<Resultado, Long> placarMenosRepetido = brasileirao.getPlacarMenosRepetido();

        System.out.println("Estatisticas (Placar menos repetido) - "
                + placarMenosRepetido.getKey() + " (" +placarMenosRepetido.getValue() + " jogo(s))");

        Long jogosCom3OuMaisGols = brasileirao.getTotalJogosCom3OuMaisGols();
        Long jogosComMenosDe3Gols = brasileirao.getTotalJogosComMenosDe3Gols();

        System.out.println("Estatisticas (3 ou mais gols) - " + jogosCom3OuMaisGols);
        System.out.println("Estatisticas (-3 gols) - " + jogosComMenosDe3Gols);

        Long totalVitoriasEmCasa = brasileirao.getTotalVitoriasEmCasa();
        Long vitoriasForaDeCasa = brasileirao.getTotalVitoriasForaDeCasa();
        Long empates = brasileirao.getTotalEmpates();

        System.out.println("Estatisticas (Vitorias Fora de casa) - " + vitoriasForaDeCasa);
        System.out.println("Estatisticas (Vitorias Em casa) - " + totalVitoriasEmCasa);
        System.out.println("Estatisticas (Empates) - " + empates);
    }

    public static void imprimirTabela(Set<PosicaoTabela> posicoes) {
        System.out.println();
        System.out.println("## TABELA CAMPEONADO BRASILEIRO: ##");
        int colocacao = 1;

        System.out.println("┌─────┬─────────────────┬─────┬─────┬─────┬─────┬─────┬─────┬─────┬────┐");
        for (PosicaoTabela posicao : posicoes) {
            System.out.println(String.format("│ %2s", colocacao) +"° " + posicao);
            colocacao++;
        }
        System.out.println("└─────┴─────────────────┴─────┴─────┴─────┴─────┴─────┴─────┴─────┴────┘");

        System.out.println();
        System.out.println();
    }

    private static boolean getResposta() {
        boolean continuar;
        int resposta;
        do {
            System.out.println("Deseja continuar pesquisando?\n1-Sim\n2-Não");
            try{
                resposta = new Scanner(System.in).nextInt();
            }catch (InputMismatchException | NumberFormatException e) {
                System.err.println("Erro: " + e);
                resposta = 0;
            }
            if (resposta < 1 || resposta > 2) {
                System.out.println("Resposta digitada inválido. Por favor, digite novamente.");
            }
            continuar = resposta == 1;
        }while(resposta < 1 || resposta > 2);
        return continuar;
    }

    private static int getAno() {
        int ano = 0;
        do{
            System.out.println("Digite o ano do brasileirao a ser pesquiado: ");
            try {
                ano = new Scanner(System.in).nextInt();
            }catch (InputMismatchException | NumberFormatException e){
                System.err.println("Erro: " + e);
            }
            if (ano < 2003 || ano > 2020) System.out.println("Valor digitado inválido. Digite um valor valido.");
        } while (ano < 2003 || ano > 2020);
        return ano;
    }
}