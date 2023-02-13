import dominio.PosicaoTabela;
import dominio.Resultado;
import impl.Brasileirao;
import impl.CampeonatoBrasileiroImpl;
import java.io.IOException;
import java.util.IntSummaryStatistics;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Aplicacao {

    public static void main(String[] args) throws IOException {

    int resposta;
    do {
        int ano;
        do {
            System.out.println("Digite o ano do Brasileirão a ser pesquisado: ");
            ano = new Scanner(System.in).nextInt();
            if (ano < 2003 || ano > 2020) {
                System.out.println("Ano digitado inválido. Por favor, digite novamente.");
            }
        } while (ano < 2003 || ano > 2020);

        //obter a implementação: (ponto extra - abstrair para interface)
        Brasileirao resultados = new CampeonatoBrasileiroImpl(ano);

        // imprimir estatisticas
        imprimirEstatisticas(resultados);

        // imprimir tabela ordenada
        imprimirTabela(resultados.getTabela());
        do {
            System.out.println("Deseja continuar pesquisando?\n1-Sim\n2-Não");
            resposta = new Scanner(System.in).nextInt();
            if (resposta < 1 || resposta > 2) {
                System.out.println("Resposta digitada inválido. Por favor, digite novamente.");
            }
        }while(resposta < 1 || resposta > 2);

    }while(resposta == 1);
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
        for (PosicaoTabela posicao : posicoes) {
            System.out.println(String.format("%2s", colocacao) +". " + posicao);
            colocacao++;
        }
        System.out.println();
        System.out.println();
    }
}