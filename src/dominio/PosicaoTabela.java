package dominio;

public record PosicaoTabela(Time time,
                            Long vitorias,
                            Long derrotas,
                            Long empates,
                            Long golsPositivos,
                            Long golsSofridos,
                            Long saldoDeGols,
                            Long jogos) {


    @Override
    public String toString() {
        return  String.format("│ %-15s │ %-3s │ %-3s │ %-3s │ %-3s │ %-3s │ %-3s │ %-3s │ %-2s │",
                time, (vitorias*3+empates), vitorias, derrotas, empates, golsPositivos, golsSofridos, saldoDeGols, jogos);
    }

}

