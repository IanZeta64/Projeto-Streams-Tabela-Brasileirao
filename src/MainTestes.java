import impl.CampeonatoBrasileiroImpl;

import java.io.IOException;
import java.nio.file.Path;

public class MainTestes {
    public static void main(String[] args) throws IOException {
        CampeonatoBrasileiroImpl cb = new CampeonatoBrasileiroImpl(2003);
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
