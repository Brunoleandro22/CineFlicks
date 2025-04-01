package br.com.cineflicks.service;

public interface IConverteDados {
    //Método genérico
    <T> T obterDados(String jason, Class <T> Classe);

}
