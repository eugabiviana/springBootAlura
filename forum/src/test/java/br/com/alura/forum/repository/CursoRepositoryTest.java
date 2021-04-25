package br.com.alura.forum.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.alura.forum.modelo.Curso;

@RunWith(SpringRunner.class)
@DataJpaTest 
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class CursoRepositoryTest {

	@Autowired //injetando o repository para que o teste do método possa ser realizado
	private CursoRepository repository;
	
	@Autowired 
	private TestEntityManager em;
	
	@Test
	public void deveriaCarregarUmCursoAoBuscarPeloSeuNome() {
		String nomeCurso = "HTML 5"; //passo o nome do curso
		
		Curso html5 = new Curso();
		html5.setNome(nomeCurso);
		html5.setCategoria("Programacao");
		em.persist(html5);
		
		
		Curso curso = repository.findByNome(nomeCurso);
		Assert.assertNotNull(curso); //verifica se tem o curso
		Assert.assertEquals(nomeCurso, curso.getNome()); //verifica se o nome que veio na pesquisa é o mesmo que passei para a pesquisa.
	
	}
	
	@Test
	public void naoDeveriaCarregarUmCursoCujoNomeNaoEstejaCadastrado() {
		String nomeCurso = "JPA";
		Curso curso = repository.findByNome(nomeCurso);
		Assert.assertNull(curso);
	}

}

//@DataJpaTest //anotação para teste de repository
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //é para dizer como será feito o teste do db e que não deve substituir as configurações no db.
//@ActiveProfiles("test") //força o profile como sendo o profile de test


