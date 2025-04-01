package br.com.cineflicks.service;

import br.com.cineflicks.model.DadosTemporada;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class ConverteDados implements IConverteDados {
    private ObjectMapper mapper = new ObjectMapper();


//    Genéricos em Java permitem criar classes, interfaces e métodos que trabalham com tipos genéricos, tornando o código
//    mais flexível e reutilizável. Eles usam parâmetros de tipo, representados por < >, para lidar com diferentes tipos de dados
//    de forma independente.
//    Por exemplo, uma classe genérica chamada "ConverteDados" pode armazenar valores de qualquer tipo.
    @Override
    public <T> T obterDados(String json, Class<T> classe) {
        try {
            return mapper.readValue(json, classe);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}


//ist<DadosTemporada> temporadas = new ArrayList<>();
//        for (int i = 1; i <= dados.totalTemporadas(); i++) {
//json = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&season=" + i + "&apikey=6585022c");
//DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
//            temporadas.add(dadosTemporada);
//
//        }
//                temporadas.forEach(System.out::println);