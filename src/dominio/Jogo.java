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
    public List<Time> getTimes(){
       return List.of(mandante, visitante);
    }
    public Integer getGols(){
        return mandantePlacar + visitantePlacar;
    }
    public Double getMediaGolsPorJogo(){
        return getGols()/2.0;
    }
    public Resultado getResultado(){
        return new Resultado(mandantePlacar, visitantePlacar);
    }
    public Time ganhador(){
        return vencedor;
    }

    public boolean empate(){
        boolean empate = false;
       if (getResultado().mandante().equals(getResultado().visitante())){
           empate = true;

       }
       return empate;
    }
    public Jogo getJogo(){
        return this;
    }

}
