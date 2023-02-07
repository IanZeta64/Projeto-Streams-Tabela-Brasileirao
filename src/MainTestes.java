import impl.CampeonatoBrasileiroImpl;

import java.io.IOException;
import java.nio.file.Path;
import java.time.DayOfWeek;

public class MainTestes {
    public static void main(String[] args) throws IOException {
        Path file = Path.of("campeonato-brasileiro.csv");
        CampeonatoBrasileiroImpl cb = new CampeonatoBrasileiroImpl(file, 2017);
        System.out.println( cb.getTotalVitoriasEmCasa());
        System.out.println(cb.getTotalVitoriasForaDeCasa());
        System.out.println(cb.getTotalEmpates());
        System.out.println(cb.getTotalJogosCom3OuMaisGols());
        System.out.println(cb.getTotalJogosComMenosDe3Gols());
        System.out.println(cb.getTodosOsTimes());
        System.out.println(cb.getJogosPorAno());
        System.out.println(cb.getTodosOsPlacares());
        System.out.println(cb.getPlacarMenosRepetido());
        System.out.println(cb.getPlacarMaisRepetido());
        System.out.println(cb.getTodosOsJogosPorTimeComoMandantes());
        System.out.println(cb.getTodosOsJogosPorTimeComoVisitante());
        cb.getTodosOsJogosPorTime();
    }
}
