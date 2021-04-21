package br.com.alura.forum.config.validacao;

public class ErroDeFormularioDto {

	private String campo;
	private String erro;
	
	//Getters
	public String getCampo() {
		return campo;
	}

	public String getErro() {
		return erro;
	}

	//Constructors
	public ErroDeFormularioDto(String campo, String erro) {
		super();
		this.campo = campo;
		this.erro = erro;
	}
	
	
}
