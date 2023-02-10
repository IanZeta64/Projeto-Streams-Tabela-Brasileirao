package impl;

import dominio.*;
import java.io.*;
import java.nio.file.Files;
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

public class CampeonatoBrasileiroImpl{

    private final Map<Integer, List<Jogo>> brasileirao;
    private final List<Jogo> jogos;

    public CampeonatoBrasileiroImpl(int ano) throws IOException {
        this.jogos = lerArquivo(Path.of("campeonato-brasileiro.csv"));
        Predicate<Jogo> filtro = (jogo) -> jogo.data().data().getYear() == ano;
        if(ano < 2020) {
            this.brasileirao = jogos.stream()
                    .filter(filtro) //filtrar por ano
                    .collect(Collectors.groupingBy(
                            Jogo::rodada,
                            Collectors.mapping(Function.identity(), Collectors.toList())));
        }else {
            this.brasileirao = Stream.of(jogos.stream().filter(filtro).collect(Collectors.groupingBy(
                    Jogo::rodada, Collectors.mapping(Function.identity(), Collectors.toList()))),
                    jogos.stream().filter((jogo) -> jogo.data().data().getYear() == 2021)
                    .collect(Collectors.groupingBy(Jogo::rodada, Collectors.mapping(Function.identity(), Collectors.toList()))))
                    .flatMap(map -> map.entrySet().stream()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                    (value1, value2) -> {value1.addAll(value2);return value1;}));
        }
    }

    public Map<Integer, List<Jogo>> getBrasileirao() {
        return Collections.unmodifiableMap(brasileirao);
    }

    public List<Jogo> getJogosPorAno() {
        return getBrasileirao().values().stream().flatMap(Collection::stream).toList();
    }

    public List<Jogo> lerArquivo(Path file) throws IOException {
        List<Jogo> jogos = new ArrayList<>();
        try {
            jogos = Files.readAllLines(file).stream().skip(1).map(strings -> {
                String[] lineSplit = strings.split(";");
                LocalTime localTime = LocalTime.parse((lineSplit[2].equals("")) ? "00:00" : lineSplit[2].replace("h", ":"));
                LocalDate localDate = LocalDate.parse(lineSplit[1], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                DayOfWeek dayOfWeek = getDayOfWeek(lineSplit[3]);
                DataDoJogo dataDoJogo = new DataDoJogo(localDate, localTime, dayOfWeek);

                Integer rodada = Integer.valueOf(lineSplit[0]), mandantePlacar = Integer.valueOf(lineSplit[8]), visitantePlacar = Integer.valueOf(lineSplit[9]);
                Time mandante = new Time(lineSplit[4]), visitante = new Time(lineSplit[5]), vencedor = new Time(lineSplit[6]);
                String arena = lineSplit[7], estadoMandante = lineSplit[10], estadoVisitante = lineSplit[11], estadoVencedor = lineSplit[12];

                return new Jogo(rodada, dataDoJogo, mandante, visitante, vencedor, arena,
                        mandantePlacar, visitantePlacar, estadoMandante, estadoVisitante, estadoVencedor);
            }).toList();
        }catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return jogos;
    }

    public IntSummaryStatistics getEstatisticasPorJogo() {
        return getJogosPorAno().stream().mapToInt(jogo-> jogo.visitantePlacar()+ jogo.mandantePlacar()).summaryStatistics();
    }

    public Map<Jogo, Double> getMediaGolsPorJogo() {
        return  getJogosPorAno().stream()
                .collect(Collectors.toMap(jogo -> jogo, jogo -> (jogo.mandantePlacar() + jogo.visitantePlacar())/2.0D));
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
        return getJogosPorAno().stream()
                .collect(Collectors.groupingBy(jogo-> new Resultado(jogo.mandantePlacar(), jogo.visitantePlacar()), Collectors.counting()));
    }

    public Map.Entry<Resultado, Long> getPlacarMaisRepetido() {
        return getTodosOsPlacares().entrySet().stream().max(Map.Entry.comparingByValue()).stream().toList().get(0);
    }

    public Map.Entry<Resultado, Long> getPlacarMenosRepetido() {
        return getTodosOsPlacares().entrySet().stream().min(Map.Entry.comparingByValue()).stream().toList().get(0);
    }

    public List<Time> getTodosOsTimes() { //PRIVATE
        return getJogosPorAno().stream().map(Jogo::mandante).collect(Collectors.toSet()).stream().toList();
    }

    public Map<Time, List<Jogo>> getTodosOsJogosPorTimeComoMandantes() { //PRIVATE
        return getJogosPorAno().stream().collect(Collectors.groupingBy(Jogo::mandante));

    }

    public Map<Time, List<Jogo>> getTodosOsJogosPorTimeComoVisitante() { //PRIVATE
        return getJogosPorAno().stream().collect(Collectors.groupingBy(Jogo::visitante));
    }

    public Map<Time, List<Jogo>> getTodosOsJogosPorTime() {
        return Stream.of(getTodosOsJogosPorTimeComoVisitante(), getTodosOsJogosPorTimeComoMandantes())
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (value1, value2) -> {value1.addAll(value2); return value1;}));
    }

    public Map<Time, Map<Boolean, List<Jogo>>> getJogosParticionadosPorMandanteTrueVisitanteFalse() {
        return getTodosOsJogosPorTime().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e->e.getValue().stream()
                        .collect(Collectors.partitioningBy(x-> x.mandante().equals(e.getKey())))));

    }

    public Map<Time, Long> getQuantidadeJogosPorTime() {
        return getTodosOsJogosPorTime().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, r -> Long.valueOf(r.getValue().size())));
    }

    public Set<PosicaoTabela> getTabela() {
        return Collections.unmodifiableSet(criarTabela());
    }
    public Set<PosicaoTabela> criarTabela(){
        var mapTabela = Stream.of( getVitoriasPorTime(), getDerrotasPorTime(),
                        getEmpatesPorTime(), getTotalDeGolsMarcadosPorTime(), getTotalDeGolsSofridosPorTime(), getSaldoDeGolsPorTime(),
                        getQuantidadeJogosPorTime()).flatMap(map -> map.entrySet().stream())
                        .collect(Collectors.toMap(Map.Entry::getKey,
                        e-> new ArrayList<>(List.of(e.getValue())),
                        (value1, value2) -> {value1.addAll(value2); return value1;}));

        return mapTabela.entrySet().stream().map(e-> new PosicaoTabela(e.getKey(), e.getValue().get(0),
                        e.getValue().get(1), e.getValue().get(2), e.getValue().get(3), e.getValue().get(4),
                        e.getValue().get(5), e.getValue().get(6)))
                .sorted(Comparator.comparing((PosicaoTabela p) -> ((p.vitorias()*3)+p.empates())).thenComparing(PosicaoTabela::vitorias)
                        .thenComparing(PosicaoTabela::saldoDeGols).thenComparing(PosicaoTabela::golsPositivos).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));
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
    public Map<Time, Long> getPontosPorTime(){ // PRIVATE
        return Stream.of(getVitoriasPorTime().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                e-> e.getValue()*3)), getEmpatesPorTime()).flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue, Long::sum));
    }

    public Map<Time, Long> getDerrotaVisitante(){
        return getJogosPorAno().stream().filter(jogo -> jogo.vencedor().equals(jogo.mandante()))
                .collect(Collectors.groupingBy(Jogo::visitante, Collectors.counting()));
    }
    public Map<Time, Long> getDerrotasMandante(){
        return getJogosPorAno().stream().filter(jogo -> jogo.vencedor().equals(jogo.visitante()))
                .collect(Collectors.groupingBy(Jogo::mandante, Collectors.counting()));
    }

    public Map<Time, Long> getDerrotasPorTime(){
        return Stream.of(getDerrotasMandante(), getDerrotaVisitante()).flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum));
    }

    public Map<Time, Long> getVitoriasPorTime() {
        return getJogosPorAno().stream().filter(jogo-> !jogo.vencedor().equals(new Time("-")))
                .collect(Collectors.groupingBy(Jogo::vencedor, Collectors.counting()));


    }
    public Map<Time, Long> getEmpatesPorTime(){
        return Stream.of(getEmpatesMandante(), getEmpatesVisitante()).flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum));
    }

    public Map<Time, Long> getEmpatesMandante() {
        return getJogosPorAno().stream().filter(jogo -> jogo.vencedor().equals(new Time("-")))
                .collect(Collectors.groupingBy(Jogo::mandante, Collectors.counting()));
    }

    public Map<Time, Long> getEmpatesVisitante() {
        return  getJogosPorAno().stream().filter(jogo -> jogo.vencedor().equals(new Time("-")))
                        .collect(Collectors.groupingBy(Jogo::visitante, Collectors.counting()));
    }

    public Map<Integer, Integer> getTotalGolsPorRodada() { // PRIVATE
        return getJogosPorAno().stream()
                .collect(Collectors.toMap(Jogo::rodada, jogo-> jogo.mandantePlacar()+jogo.visitantePlacar(), Integer::sum));
    }

    public Map<Time, Long> getTotalDeGolsMarcadosPorTime() { // PRIVATE
        return Stream.of(getGolsMarcadosComoMandante(), getGolsMarcadosComoVisitante()).flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum));
    }
    public Map<Time, Long> getTotalDeGolsSofridosPorTime() { // PRIVATE
    return Stream.of(getGolsSofridosComoMandante(), getGolsSofridosComoVisitante()).flatMap(m -> m.entrySet().stream())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum));
    }

    public Map<Time, Long> getSaldoDeGolsPorTime(){
        return Stream.of(getTotalDeGolsMarcadosPorTime(),
                        getTotalDeGolsSofridosPorTime().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e-> e.getValue()*-1)) )
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum));
    }

    public Map<Integer, Double> getMediaDeGolsPorRodada() { // PRIVATE
        return getJogosPorAno().stream()
                .collect(Collectors.toMap(Jogo::rodada, jogo -> (jogo.mandantePlacar()+ jogo.visitantePlacar())/10.0, Double::sum));


    }

    public Map<Time, Long> getGolsMarcadosComoVisitante(){ //PRIVATE
        return getTodosOsJogosPorTimeComoVisitante().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e-> e.getValue().stream().mapToLong(Jogo::visitantePlacar).sum()));
    }
    public Map<Time, Long> getGolsMarcadosComoMandante(){ //PRIVATE
        return getTodosOsJogosPorTimeComoMandantes().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e-> e.getValue().stream().mapToLong(Jogo::mandantePlacar).sum()));

    }
    public Map<Time, Long> getGolsSofridosComoVisitante(){
        return getTodosOsJogosPorTimeComoVisitante().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e-> e.getValue().stream().mapToLong(Jogo::mandantePlacar).sum()));

    }

    public Map<Time, Long> getGolsSofridosComoMandante(){
        return getTodosOsJogosPorTimeComoMandantes().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e-> e.getValue().stream().mapToLong(Jogo::visitantePlacar).sum()));
    }

}