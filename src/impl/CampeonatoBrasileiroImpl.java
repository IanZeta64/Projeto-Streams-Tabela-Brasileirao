package impl;

import dominio.*;
import java.io.*;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
                LocalTime localTime = LocalTime.parse(horaMin[0] + ":" + horaMin[1], DateTimeFormatter.ofPattern("HH:mm"));
                DayOfWeek dayOfWeek = getDayOfWeek(lineSplit[3]);
                DataDoJogo dataDoJogo = new DataDoJogo(localDate, localTime, dayOfWeek);
                Time mandante = new Time(lineSplit[4]), visitante = new Time(lineSplit[5]), vencedor = new Time(lineSplit[6]);
                String arena = lineSplit[7], estadoMandante = lineSplit[10], estadoVisitante = lineSplit[11], estadoVencedor = lineSplit[12];
                new DataDoJogo(localDate, localTime, dayOfWeek);
                Jogo jogo = new Jogo(rodada, dataDoJogo, mandante, visitante, vencedor, arena,
                        mandantePlacar, visitantePlacar, estadoMandante, estadoVisitante, estadoVencedor);
                jogos.add(jogo);
            }

        } catch (IOException e) { //TRATAR EXCESSAO NULL
            System.out.println("Error: " + e.getMessage());
        } catch (NullPointerException e) {

        }
        return jogos;
    }

    public IntSummaryStatistics getEstatisticasPorJogo() {
        return getJogosPorAno().stream().mapToInt(Jogo::getGols).summaryStatistics();
    }

    public Map<Jogo, Double> getMediaGolsPorJogo() { // MAP<JOGO, DOUBLE>
        return getJogosPorAno().stream().collect(Collectors.toMap(Jogo::getJogo, Jogo::getMediaGolsPorJogo));


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
        return getJogosPorAno().stream().filter(jogo -> Objects.equals(jogo.mandantePlacar(), jogo.visitantePlacar()))
                .count();
    }

    public Long getTotalJogosComMenosDe3Gols() {
        return getJogosPorAno().stream().filter(jogo -> (jogo.mandantePlacar() + jogo.visitantePlacar() < 3))
                .count();
    }

    public Long getTotalJogosCom3OuMaisGols() {
        return getJogosPorAno().stream().filter(jogo -> (jogo.mandantePlacar() + jogo.visitantePlacar() > 3))
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

    public List<Time> getTodosOsTimes() { //PRIVATE
        return getJogosPorAno().stream().map(Jogo::getTimes).limit(10).flatMap(Collection::stream).toList();
    }

    public Map<Time, List<Jogo>> getTodosOsJogosPorTimeComoMandantes() { //PRIVATE
        return getJogosPorAno().stream().collect(Collectors.groupingBy(Jogo::mandante));

    }

    public Map<Time, List<Jogo>> getTodosOsJogosPorTimeComoVisitante() { //PRIVATE
        return getJogosPorAno().stream().collect(Collectors.groupingBy(Jogo::visitante));
    }

    public void getTodosOsJogosPorTime() { // INCOMPLETO - RETURN MAP <TIME, LIST<JOGOS>>
//        var a = Stream.concat(getTodosOsJogosPorTimeComoVisitante().entrySet().stream(), getTodosOsJogosPorTimeComoMandantes().entrySet().stream())
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (value1, value2) -> ));

        var mapJogosTodos = Stream.of(getTodosOsJogosPorTimeComoVisitante(), getTodosOsJogosPorTimeComoMandantes())
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (x, y) -> {x.addAll(y); return x;}
                ));

//                .flatMap(m -> m.entrySet().stream())
//                .toList().stream().collect(Collectors.toMap(Map.Entry::getKey,
//                        e-> e.getValue().stream().map(x-> e.getValue()).toList().stream().flatMap(List::stream).toList()));
        System.out.println(mapJogosTodos);
//
    }

    public Map<Time, Map<Boolean, List<Jogo>>> getJogosParticionadosPorMandanteTrueVisitanteFalse() {
        return null;
    }

    public Set<PosicaoTabela> getTabela() {
        return null;
    }

    public void criarPosicaoTabela() {
//        var empatesMandantes = getJogosPorAno().stream()
//                .collect(Collectors.partitioningBy(jogo -> jogo.mandantePlacar().equals(jogo.visitantePlacar()), Collectors.toList()))
//                .get(true).stream().collect(Collectors.groupingBy(Jogo::mandante, Collectors.counting()));
//        var empatesVisitantes = getJogosPorAno().stream()
//                .collect(Collectors.partitioningBy(jogo -> jogo.mandantePlacar().equals(jogo.visitantePlacar()), Collectors.toList()))
//                .get(true).stream().collect(Collectors.groupingBy(Jogo::visitante, Collectors.counting()));

//        var vitorias = getJogosPorAno().stream().collect(Collectors.groupingBy(Jogo::vencedor, Collectors.counting()));
//        var golsFeitosMandante = getJogosPorAno().stream().collect(Collectors.toMap(Jogo::mandante, Jogo::mandantePlacar));
//        var golsFeitoVisitante = getJogosPorAno().stream().collect(Collectors.toMap(Jogo::visitante, Jogo::visitantePlacar));
//        System.out.println(golsFeitoVisitante);
//        System.out.println(golsFeitosMandante);


    }


    private DayOfWeek getDayOfWeek(String dia) {
        DayOfWeek dayOfWeekValues;
        switch (dia) {
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

    public Map<Time, Long> vitoriasPorTime() {
        var map = getJogosPorAno().stream().collect(Collectors.groupingBy(Jogo::ganhador, Collectors.counting()));
        map.remove(new Time("-"));
        return map;

    }
    public Map<Time, Long> empatesPorTime(){
        return Stream.of(empatesMandante(), empatesVisitante()).flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum));
    }

    public Map<Time, Long> empatesMandante() {
        return getJogosPorAno().stream()
                .filter(jogo -> jogo.vencedor().equals(new Time("-")))
                .collect(Collectors.groupingBy(Jogo::mandante)).entrySet()
                .stream().collect(Collectors.toMap(Map.Entry::getKey, e -> Long.valueOf(e.getValue().stream().map(Jogo::empate).toList().size())));
    }

    public Map<Time, Long> empatesVisitante() {
        return getJogosPorAno().stream()
                .filter(jogo -> jogo.vencedor().equals(new Time("-")))
                        .collect(Collectors.groupingBy(Jogo::visitante)).entrySet()
                .stream().collect(Collectors.toMap(Map.Entry::getKey, e -> Long.valueOf(e.getValue().stream().map(Jogo::empate).toList().size())));
    }

    public Map<Integer, Integer> getTotalGolsPorRodada() { // PRIVATE
        return getJogosPorAno().stream()
                .collect(Collectors.toMap(Jogo::rodada, Jogo::getGols, Integer::sum));
    }

    public Map<Time, Long> getTotalDeGolsMarcadosPorTime() { // PRIVATE
        return Stream.of(getGolsMarcadosComoMandante(), getGolsMarcadosComoVisitante()).flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum));
    }
    public Map<Time, Long> getTotalDeGolsSofridosPorTime() { // PRIVATE
    return Stream.of(getGolsSofridosComoMandante(), getGolsSofridosComoVisitante()).flatMap(m -> m.entrySet().stream())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum));
    }

    public Map<Integer, Double> getMediaDeGolsPorRodada() { // PRIVATE
        return getJogosPorAno().stream()
                .collect(Collectors.toMap(Jogo::rodada, Jogo::getMediaGolsPorJogo, Double::sum))
                .entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue()/5.0));
    }

    public Map<Time, Long> getGolsMarcadosComoVisitante(){ //PRIVATE
        return getTodosOsJogosPorTimeComoVisitante().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e-> e.getValue().stream()
                        .collect(Collectors.summingLong(n -> n.visitantePlacar()))));
    }
    public Map<Time, Long> getGolsMarcadosComoMandante(){ //PRIVATE
        return getTodosOsJogosPorTimeComoMandantes().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e-> e.getValue().stream()
                        .collect(Collectors.summingLong(n -> n.mandantePlacar()))));

    }
    public Map<Time, Long> getGolsSofridosComoVisitante(){
        return getTodosOsJogosPorTimeComoVisitante().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e-> e.getValue().stream()
                        .collect(Collectors.summingLong(n -> n.mandantePlacar()))));

    }

    public Map<Time, Long> getGolsSofridosComoMandante(){
        return getTodosOsJogosPorTimeComoMandantes().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e-> e.getValue().stream()
                        .collect(Collectors.summingLong(n -> n.visitantePlacar()))));
    }

}