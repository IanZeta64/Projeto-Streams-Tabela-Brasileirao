package dominio;

import java.util.Comparator;

public record PosicaoTabela(Time time,

                            Long pontos,
                            Long vitorias,
                            Long derrotas,
                            Long empates,
                            Long golsPositivos,
                            Long golsSofridos,
                            Long saldoDeGols,
                            Long jogos) {
//public class PosicaoTabela implements Comparator {
//     Time time;
//    Long pontos;
//    Long vitorias;
//    Long derrotas;
//    Long empates;
//    Long golsPositivos;
//    Long golsSofridos;
//    Long saldoDeGols;
//    Long jogos;
//
//    public PosicaoTabela(Time time, Long pontos, Long vitorias, Long derrotas, Long empates, Long golsPositivos, Long golsSofridos, Long saldoDeGols, Long jogos) {
//        this.time = time;
//        this.pontos = pontos;
//        this.vitorias = vitorias;
//        this.derrotas = derrotas;
//        this.empates = empates;
//        this.golsPositivos = golsPositivos;
//        this.golsSofridos = golsSofridos;
//        this.saldoDeGols = saldoDeGols;
//        this.jogos = jogos;
//    }





    @Override
    public String toString() {
        return  time +
                ", pontos=" + pontos + // desenvolver forma de obter a pontuação
                ", vitorias=" + vitorias +
                ", derrotas=" + derrotas +
                ", empates=" + empates +
                ", golsPositivos=" + golsPositivos +
                ", golsSofridos=" + golsSofridos +
                ", saldoDeGols=" + saldoDeGols +
                ", jogos=" + jogos +
                '}';
    }


}

