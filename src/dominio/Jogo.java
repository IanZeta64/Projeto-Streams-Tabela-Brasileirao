package dominio;

import java.util.List;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.function.Function;
import java.util.logging.Logger;

public record Jogo(Integer rodada,
                   DataDoJogo data,
                   Time mandante,
                   Time visitante,
                   Time vencedor,
                   String arena,
                   Integer mandantePlacar,
                   Integer visitantePlacar,
                   String estadoMandante,
                   String estadoVisitante,
                   String estadoVencedor){

    @Override
    public String toString() {
        return "Jogo{" +
                "rodada=" + rodada +
                ", mandante=" + mandante +
                ", visitante=" + visitante +
                ", vencedor=" + vencedor +
                '}';
    }
}
