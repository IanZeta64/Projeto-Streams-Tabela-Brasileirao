package impl;

import dominio.*;
import java.io.*;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.DoubleToIntFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CampeonatoBrasileiroImpl {

    private Map<Integer, List<Jogo>> brasileirao;
    private List<Jogo> jogos;
    private Predicate<Jogo> filtro;

    public CampeonatoBrasileiroImpl(Path arquivo, int ano) throws IOException {
        this.jogos = lerArquivo(arquivo);
        this.filtro = (jogo) -> jogo.data().data().getYear() == ano;
        this.brasileirao = jogos.stream()
                .filter(filtro) //filtrar por ano
                .collect(Collectors.groupingBy(
                        Jogo::rodada,
                        Collectors.mapping(Function.identity(), Collectors.toList())));

    }

    public Map<Integer, List<Jogo>> getBrasileirao() {
        return Collections.unmodifiableMap(brasileirao);
    }

    public List<Jogo> getJogos() {
        return Collections.unmodifiableList(jogos);
    }

    public List<Jogo> getJogosPorAno() {
        return getBrasileirao().values().stream().flatMap(Collection::stream).toList();
    }

    public List<Jogo> lerArquivo(Path file) throws IOException {
        List<Jogo> jogos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(String.valueOf(file)))) {
            String line = br.readLine();
            while (line != null) {
                    line = br.readLine();
                    var lineSplit = line.split(";");
                    Integer rodada = Integer.valueOf(lineSplit[0]), mandantePlacar = Integer.valueOf(lineSplit[8]), visitantePlacar = Integer.valueOf(lineSplit[9]);
                    LocalDate localDate = LocalDate.parse(lineSplit[1], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    var horaMin = lineSplit[2].contains("h") ? lineSplit[2].split("h") : lineSplit[2].split(":");
                    LocalTime localTime = LocalTime.parse(horaMin[0]+":"+horaMin[1], DateTimeFormatter.ofPattern("HH:mm"));
                    DayOfWeek dayOfWeek = getDayOfWeek(lineSplit[3]);
                    DataDoJogo dataDoJogo = new DataDoJogo(localDate, localTime, dayOfWeek);
                    Time mandante = new Time(lineSplit[4]), visitante = new Time(lineSplit[5]), vencedor = new Time(lineSplit[6]);
                    String arena = lineSplit[7], estadoMandante = lineSplit[10], estadoVisitante = lineSplit[11], estadoVencedor = lineSplit[12];
                    new DataDoJogo(localDate, localTime, dayOfWeek);
                    Jogo jogo = new Jogo(rodada, dataDoJogo, mandante, visitante, vencedor, arena,
                            mandantePlacar,visitantePlacar, estadoMandante, estadoVisitante,estadoVencedor);
                    jogos.add(jogo);
                }

        }
        catch (IOException e) { //TRATAR EXCESSAO NULL
            System.out.println("Error: " + e.getMessage());
        }
        catch (NullPointerException e){

        }
        return jogos;
    }

    public IntSummaryStatistics getEstatisticasPorJogo() {
        return getJogosPorAno().stream().mapToInt(Jogo::getGols).summaryStatistics();
    }

    public Map<Jogo, Integer> getMediaGolsPorJogo() {

        return null;
    }

    public IntSummaryStatistics GetEstatisticasPorJogo() {
        return null;
    }


    public Long getTotalVitoriasEmCasa() {
       return getJogosPorAno().stream().filter(jogo -> jogo.mandantePlacar() > jogo.visitantePlacar())
                       .count();
    }

    public Long getTotalVitoriasForaDeCasa() {
        return getJogosPorAno().stream().filter(jogo -> jogo.mandantePlacar() < jogo.visitantePlacar())
                .count();
    }

    public Long getTotalEmpates() {
        return  getJogosPorAno().stream().filter(jogo -> Objects.equals(jogo.mandantePlacar(), jogo.visitantePlacar()))
                .count();
    }

    public Long getTotalJogosComMenosDe3Gols() {
        return  getJogosPorAno().stream().filter(jogo -> (jogo.mandantePlacar() + jogo.visitantePlacar() < 3))
                .count();
    }

    public Long getTotalJogosCom3OuMaisGols() {
        return  getJogosPorAno().stream().filter(jogo -> (jogo.mandantePlacar() + jogo.visitantePlacar() > 3))
                .count();
    }

    public Map<Resultado, Long> getTodosOsPlacares() {
        return getJogosPorAno().stream().collect(Collectors.groupingBy(Jogo::getResultado, Collectors.counting()));
    }

    public Map.Entry<Resultado, Long> getPlacarMaisRepetido() {
//        var a = getJogosPorAno().stream()
//                .collect(Collectors.groupingBy(Jogo::getResultado, Collectors.counting())).keySet();
//        var b = getJogosPorAno().stream()
//                .collect(Collectors.groupingBy(Jogo::getResultado, Collectors.counting())).values().stream().toList();
//        var value = getTodosOsPlacares().values().stream().max((n1,n2) -> n1.compareTo(n2)).get();
//        var key = getTodosOsPlacares().entrySet().stream().filter( n -> n.getValue().equals(value)).map(Map.Entry::getKey).toList().get(0);
        return getTodosOsPlacares().entrySet().stream().max(Map.Entry.comparingByValue()).get();

    }

    public Map.Entry<Resultado, Long> getPlacarMenosRepetido() {
        return getTodosOsPlacares().entrySet().stream().min(Map.Entry.comparingByValue()).get();
    }

    public List<Time> getTodosOsTimes() { //private
        return  getJogosPorAno().stream().map(Jogo::getTimes).limit(10).flatMap(Collection::stream).toList();
    }

    public Map<Time, List<Jogo>> getTodosOsJogosPorTimeComoMandantes() { //PRIVATE
        return getJogosPorAno().stream().collect(Collectors.groupingBy(Jogo::mandante));

    }

    public Map<Time, List<Jogo>> getTodosOsJogosPorTimeComoVisitante() { //PRIVATE
        return  getJogosPorAno().stream().collect(Collectors.groupingBy(Jogo::visitante));
    }

    public void getTodosOsJogosPorTime() { // INCOMPLETO
        var a = Stream.concat(getTodosOsJogosPorTimeComoVisitante().entrySet().stream(), getTodosOsJogosPorTimeComoMandantes().entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (value1, value2) -> value1 ));
        System.out.println(a);
    }

    public Map<Time, Map<Boolean, List<Jogo>>> getJogosParticionadosPorMandanteTrueVisitanteFalse() {
        return null;
    }

    public Set<PosicaoTabela> getTabela() {
        return null;
    }

    public void criarPosicaoTabela(){
//        var empatesMandantes = getJogosPorAno().stream()
//                .collect(Collectors.partitioningBy(jogo -> jogo.mandantePlacar().equals(jogo.visitantePlacar()), Collectors.toList()))
//                .get(true).stream().collect(Collectors.groupingBy(Jogo::mandante, Collectors.counting()));
//        var empatesVisitantes = getJogosPorAno().stream()
//                .collect(Collectors.partitioningBy(jogo -> jogo.mandantePlacar().equals(jogo.visitantePlacar()), Collectors.toList()))
//                .get(true).stream().collect(Collectors.groupingBy(Jogo::visitante, Collectors.counting()));
        var vitorias = getJogosPorAno().stream().collect(Collectors.groupingBy(Jogo::vencedor, Collectors.counting()));
        var golsFeitos = getJogosPorAno().stream().map((Jogo::mandante));


    }


    private DayOfWeek getDayOfWeek(String dia) {
        DayOfWeek dayOfWeekValues;
        switch (dia){
            case "Segunda-feira" -> dayOfWeekValues = DayOfWeek.MONDAY;
            case "Terça-feira" -> dayOfWeekValues = DayOfWeek.TUESDAY;
            case "Quarta-feira" -> dayOfWeekValues = DayOfWeek.WEDNESDAY;
            case "Quinta-feira" -> dayOfWeekValues = DayOfWeek.THURSDAY;
            case "Sexta-feira" -> dayOfWeekValues = DayOfWeek.FRIDAY;
            case "Sábado" -> dayOfWeekValues = DayOfWeek.SATURDAY;
            default -> dayOfWeekValues = DayOfWeek.SUNDAY;
        }
        return dayOfWeekValues;

    }

    private Map<Integer, Integer> getTotalGolsPorRodada() {
        return null;
    }

    private Map<Time, Integer> getTotalDeGolsPorTime() {
        return null;
    }

    private Map<Integer, Double> getMediaDeGolsPorRodada() {
        return null;
    }


}