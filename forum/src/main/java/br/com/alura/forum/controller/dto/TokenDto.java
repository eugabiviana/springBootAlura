package br.com.alura.forum.controller.dto;

public class TokenDto {

	private String token;
	private String tipo;

	//Constructor
	public TokenDto(String token, String tipo) {
		this.token = token;
		this.tipo = tipo;
	}

	//Getters
	public String getToken() {
		return token;
	}

	public String getTipo() {
		return tipo;
	}

}
