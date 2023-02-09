package dominio;

import java.util.Comparator;

public record PosicaoTabela(Time time,

                            Long vitorias,
                            Long derrotas,
                            Long empates,
                            Long golsPositivos,
                            Long golsSofridos,
                            Long saldoDeGols,
                            Long jogos) {

    public Long pontos() {
        return vitorias()*3 + empates();
    }

    @Override
    public String toString() {
        return  time +
                ", pontos=" + ((vitorias*3) + empates)+ // desenvolver forma de obter a pontuação
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

