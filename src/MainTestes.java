import impl.CampeonatoBrasileiroImpl;

import java.io.IOException;
import java.nio.file.Path;

public class MainTestes {
    public static void main(String[] args) throws IOException {
        CampeonatoBrasileiroImpl cb = new CampeonatoBrasileiroImpl(2003);
<<<<<<< Updated upstream
//        System.out.println( cb.getTotalVitoriasEmCasa());
//        System.out.println(cb.getTotalVitoriasForaDeCasa());
//        System.out.println(cb.getTotalEmpates());
//        System.out.println(cb.getTotalJogosCom3OuMaisGols());
//        System.out.println(cb.getTotalJogosComMenosDe3Gols());
//        System.out.println(cb.getTodosOsTimes());
//        System.out.println(cb.getJogosPorAno());
//        System.out.println(cb.getTodosOsPlacares());
//        System.out.println(cb.getPlacarMenosRepetido());
//        System.out.println(cb.getPlacarMaisRepetido());
//        System.out.println(cb.getTotalGolsPorRodada());
//        System.out.println(cb.getMediaDeGolsPorRodada());
//        System.out.println(cb.getMediaGolsPorJogo());
//        System.out.println(cb.getVitoriasPorTime());
//        System.out.println(cb.getTodosOsJogosPorTimeComoMandantes());
//        System.out.println(cb.getTodosOsJogosPorTimeComoVisitante());
//        System.out.println(cb.getTodosOsJogosPorTime());
//        System.out.println(cb.getGolsMarcadosComoMandante());
//        System.out.println(cb.getGolsMarcadosComoVisitante());
//        System.out.println(cb.getGolsSofridosComoMandante());
//        System.out.println(cb.getGolsSofridosComoVisitante());
//        System.out.println(cb.getEmpatesVisitante());
//        System.out.println(cb.getVitoriasPorTime());
//        System.out.println(cb.getDerrotaVisitante());
//        System.out.println(cb.getDerrotasMandante());
//        System.out.println(cb.getDerrotasPorTime());
//        System.out.println(cb.getTotalDeGolsSofridosPorTime());
//        System.out.println(cb.getTotalDeGolsMarcadosPorTime());
//        System.out.println(cb.getTodosOsTimes());
//        System.out.println(cb.getSaldoDeGolsPorTime());
//        System.out.println(cb.criarTabela());
//        System.out.println(cb.getJogosParticionadosPorMandanteTrueVisitanteFalse());
    }
}







//        2;17/05/2007;18h10;Sábado;Sport;Vitória;-;Ilha do Retiro;0;0;PE;ES;- XXX
//        2;17/05/2007;18h10;Sábado;Cruzeiro;Botafogo-RJ;Cruzeiro;Mineirão;1;0;MG;RJ;MG XXX
//        2;17/05/2007;18h10;Sábado;Vasco;Portuguesa;Vasco;São Januário;3;1;RJ;SP;RJ XXX
//        2;19/05/2007;18h10;Sábado;Santos;América-RN;América-RN;Vila Belmiro;2;3;SP;RN;RN
//        2;19/05/2007;18h10;Sábado;Athlético-PR;Internacional;Athlético-PR;Kyocera Arena;2;1;PR;RS;PR
//        2;19/05/2007;18h10;Sábado;Juventude;Paraná;Paraná;Alfredo Jaconi;1;2;RS;PR;PR
//        2;20/05/2007;16h00;Domingo;Botafogo-RJ;Atlético-MG;Botafogo-RJ;Maracanã;2;1;RJ;MG;RJ
//        2;20/05/2007;16h00;Domingo;Cruzeiro;Corinthians;Corinthians;Mineirão;0;3;MG;SP;SP
//        2;20/05/2007;16h00;Domingo;Goiás;Flamengo;Flamengo;Serra Dourada;1;3;GO;RJ;RJ
//        2;20/05/2007;18h10;Domingo;Grêmio;Fluminense;Grêmio;Olímpico Monumental;2;0;RS;RJ;RS
//        2;20/05/2007;18h10;Domingo;Náutico;São Paulo;Náutico;Aflitos;1;0;PE;SP;PE
//        2;20/05/2007;16h00;Domingo;Palmeiras;Figueirense;Palmeiras;Parque Antártica;2;1;SP;SC;SP
//        2;20/05/2007;18h10;Domingo;Vasco;Sport;Vasco;São Januário;3;1;RJ;PE;RJ
=======
        //METODOS NAO IMPLEMENTADOS
        System.out.println("'MAINTESTES' FEITO COM ANO 2003\n");
        System.out.println("Todos os times:");
        cb.getTodosOsTimes().forEach(System.out::println);
        System.out.println("\nTotal gols por rodada:");
        System.out.println(cb.getTotalGolsPorRodada());
        System.out.println("\nMedia de Gols por rodada:");
        System.out.println(cb.getMediaDeGolsPorRodada());
        System.out.println("\nMedia de gols por jogo:");
        System.out.println(cb.getMediaGolsPorJogo());
        System.out.println("\nJogos particionados por visitante false/mandante true:");
        cb.getJogosParticionadosPorMandanteTrueVisitanteFalse().entrySet().forEach(System.out::println);

    }
}
>>>>>>> Stashed changes
