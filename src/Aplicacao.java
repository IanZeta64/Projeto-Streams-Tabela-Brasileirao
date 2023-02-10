import dominio.PosicaoTabela;
import dominio.Resultado;
import impl.CampeonatoBrasileiroImpl;
import java.io.IOException;
import java.util.IntSummaryStatistics;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Aplicacao {

    public static void main(String[] args) throws IOException {


        // obter a implementação: (ponto extra - abstrair para interface)
        System.out.println("Digite o ano do Brasileirao a ser pesquisado: ");
        int ano = new Scanner(System.in).nextInt();
        CampeonatoBrasileiroImpl resultados = new CampeonatoBrasileiroImpl(ano);

        // imprimir estatisticas
        imprimirEstatisticas(resultados);

        // imprimir tabela ordenada
        imprimirTabela(resultados.getTabela());

    }

    private static void imprimirEstatisticas(CampeonatoBrasileiroImpl brasileirao) {
        IntSummaryStatistics statistics = brasileirao.getEstatisticasPorJogo();

        System.out.println("Estatisticas (Total de gols) - " + statistics.getSum()); // -OK
        System.out.println("Estatisticas (Total de jogos) - " + statistics.getCount()); // - OK
        System.out.println("Estatisticas (Media de gols) - " + statistics.getAverage()); // - OK

        Map.Entry<Resultado, Long> placarMaisRepetido = brasileirao.getPlacarMaisRepetido();

        System.out.println("Estatisticas (Placar mais repetido) - "
                + placarMaisRepetido.getKey() + " (" +placarMaisRepetido.getValue() + " jogo(s))"); // - OK

        Map.Entry<Resultado, Long> placarMenosRepetido = brasileirao.getPlacarMenosRepetido();

        System.out.println("Estatisticas (Placar menos repetido) - "
                + placarMenosRepetido.getKey() + " (" +placarMenosRepetido.getValue() + " jogo(s))"); // - OK

        Long jogosCom3OuMaisGols = brasileirao.getTotalJogosCom3OuMaisGols(); // OK
        Long jogosComMenosDe3Gols = brasileirao.getTotalJogosComMenosDe3Gols(); // OK

        System.out.println("Estatisticas (3 ou mais gols) - " + jogosCom3OuMaisGols); // - OK
        System.out.println("Estatisticas (-3 gols) - " + jogosComMenosDe3Gols); // - OK

        Long totalVitoriasEmCasa = brasileirao.getTotalVitoriasEmCasa(); // - OK
        Long vitoriasForaDeCasa = brasileirao.getTotalVitoriasForaDeCasa(); // - OK
        Long empates = brasileirao.getTotalEmpates(); // - OK

        System.out.println("Estatisticas (Vitorias Fora de casa) - " + vitoriasForaDeCasa);  // - OK
        System.out.println("Estatisticas (Vitorias Em casa) - " + totalVitoriasEmCasa); // - OK
        System.out.println("Estatisticas (Empates) - " + empates); // - OK
    }

    public static void imprimirTabela(Set<PosicaoTabela> posicoes) { // - OK
        System.out.println();
        System.out.println("## TABELA CAMPEONADO BRASILEIRO: ##");
        int colocacao = 1;
        for (PosicaoTabela posicao : posicoes) {
            System.out.println(colocacao +". " + posicao);
            colocacao++;
        }
        System.out.println();
        System.out.println();
    }
}