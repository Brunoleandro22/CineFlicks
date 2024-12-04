package br.com.cineflicks.service;

public interface IConverteDados {
    <T> T obterDados(String jason, Class <T> Classe);

}
