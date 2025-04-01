
package br.com.cineflicks.principal;

import br.com.cineflicks.model.DadosEpisodio;
import br.com.cineflicks.model.DadosSerie;
import br.com.cineflicks.model.DadosTemporada;
import br.com.cineflicks.model.Episodio;
import br.com.cineflicks.service.ConsumoApi;
import br.com.cineflicks.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";

    public void exibeMenu() {
        System.out.println("Digite o nome da série:");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);

        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporada = new ArrayList<>();
        for (int i = 1; i <= dados.totalTemporadas(); i++) {
            json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporada.add(dadosTemporada);
        }
        temporada.forEach(System.out::println);

//        List<DadosEpisodio> episodiosTemporada = null;
//        for (int i = 0; i <= dados.totalTemporadas(); i++) {
//            episodiosTemporada = temporada.get(i).episodios();
//            for (int j = 0; j < episodiosTemporada.size(); j++) {
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }
        temporada.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<DadosEpisodio> dadosEpisodios = temporada.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("\n Top 5 episódios");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episodio> episodios = temporada.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);

//        System.out.println("Digite o nome do episódio");
//        var trechoTitulo = leitura.nextLine();
//
//
//        Optional<Episodio> episodioBuscado = episodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
//                .findFirst();
//
//        if (episodioBuscado.isPresent()) {
//            System.out.println("Episódio encontrado");
//            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
//            System.out.println("Episódio: " + episodioBuscado.get().getTitulo());
//        } else {
//            System.out.println("Não encontrado");
//        }

//        List<Episodio> ListaEpisodiosBuscados = episodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
//                .toList();
//
//        if (!ListaEpisodiosBuscados.isEmpty()) {
//            System.out.println("\nEpisódios encontrados:");
//            ListaEpisodiosBuscados.forEach(e -> {
//                System.out.println("\nTemporada: " + e.getTemporada() );
//                System.out.println("Episódio: " + e.getTitulo());
//            });
//        } else {
//            System.out.println("Nenhum episódio encontrado com esse nome.");
//        }

//        System.out.println("A partir de qual ano deseja ver os episódios? ");
//        var ano = leitura.nextInt();
//        leitura.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(ano, 1,1);
//
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        episodios.stream()
//                .filter(e -> e.getDalaLancamento() != null && e.getDalaLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println(
//                        "Temporada " + e.getTemporada() +
//                                "Episódio " + e.getTitulo() +
//                                "Data de lançamento" + e.getDalaLancamento()
//                ));
        //Método para mostrar a média de avaliação de cada temporada
        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println(avaliacoesPorTemporada);

        // Método para pegar um estatistica geral
        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println("Média: " + est.getAverage());
        System.out.println("Melhor: " + est.getMax());
        System.out.println("Pior :" + est.getMin());
        System.out.println("Quantidade avalidados: " + est.getCount());
    }
}

