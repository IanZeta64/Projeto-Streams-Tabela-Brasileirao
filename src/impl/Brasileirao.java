package impl;

import dominio.PosicaoTabela;
import dominio.Resultado;
import java.util.IntSummaryStatistics;
import java.util.Map;
import java.util.Set;

public interface Brasileirao {
    Set<PosicaoTabela> getTabela();
    IntSummaryStatistics getEstatisticasPorJogo();
     Map.Entry<Resultado, Long>getPlacarMaisRepetido();
    Map.Entry<Resultado, Long> getPlacarMenosRepetido();
    Long getTotalJogosCom3OuMaisGols();
    Long getTotalJogosComMenosDe3Gols();
    Long getTotalVitoriasEmCasa();
    Long getTotalVitoriasForaDeCasa();
    Long getTotalEmpates();
}
