package dominio;

import java.util.List;

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
    public Resultado getResultado(){
        return new Resultado(mandantePlacar, visitantePlacar);
    }
    public Time ganhador(){
        return vencedor;
    }

}
