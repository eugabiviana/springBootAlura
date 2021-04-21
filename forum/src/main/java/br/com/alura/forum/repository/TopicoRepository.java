package br.com.alura.forum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.forum.modelo.Topico;

public interface TopicoRepository extends JpaRepository<Topico, Long>{ //o Long é o tipo da variável do Id!

	List<Topico> findByCursoNome(String nomeCurso); 
	
	

}
