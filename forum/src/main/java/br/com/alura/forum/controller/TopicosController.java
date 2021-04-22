package br.com.alura.forum.controller;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

@RestController //em API Rest TEM que ter a Annotation @ResponseBody antes de todos os métodos para que eles retornem no navegador. A @RestController é para que não seja necessário usar @ResponseBody todas as vezes.
@RequestMapping("/topicos")
public class TopicosController {
	
	@Autowired
	private TopicoRepository topicoRepository; //injetando a classe repository no controller
	
	@Autowired
	private CursoRepository cursoRepository;
	
	@GetMapping //lógica que traz todos os tópicos
	@Cacheable(value  = "listaDeTopicos") //mostra para o servidor que é para usar o cache
	public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso, @PageableDefault(sort = "id", direction = Direction.ASC, page = 0, size = 10) Pageable paginacao){	
						
		if (nomeCurso == null) {
			Page<Topico> topicos = topicoRepository.findAll(paginacao);
			return TopicoDto.converter(topicos);
		}else {
			Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso, paginacao);
			return TopicoDto.converter(topicos);
		}
				
	}
	
	@PostMapping
	@Transactional
	@CacheEvict(value = "listaDeTopicos", allEntries = true) //ele atualiza a modificação na memória cache
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) { //essa annotation é para buscar a informação na página (no caso, no que o usuário está digitando) e não na url (no parametro de busca /topicos?blabla)
		Topico topico = form.converter(cursoRepository);
		topicoRepository.save(topico);
		
		//cria o endereço URL de acordo com o topico criado:
		URI uri = uriBuilder.path("/topico/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico)); 
	}

	@GetMapping("/{id}") //lógica para detalhar um tópico
	public ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id) {
		Optional<Topico> topico = topicoRepository.findById(id);
		if (topico.isPresent()) {
			return ResponseEntity.ok(new DetalhesDoTopicoDto(topico.get()));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{id}")
	@Transactional //avisa ao String que é para commitar ao final da transação
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {
		Optional<Topico> optional = topicoRepository.findById(id);
		if (optional.isPresent()) {
			Topico topico = form.atualizar(id, topicoRepository);
			return ResponseEntity.ok(new TopicoDto(topico)); //o ok é o corpo que será devolvido como resposta pelo servidor.
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Topico> optional = topicoRepository.findById(id);
		if (optional.isPresent()) {
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.notFound().build();
	}	
	
}

//O Optional (uma melhoria do código), é usado no Get, Put e Delete para melhorar a resposta do servidor ao usuário. Sem ele, se o usuário buscar por um id que não existe, vem a resposta 500 e erro imenso. Com ele, o retorno é 400: not found e body vazio.
//@PageDefault: mostra a paginação de forma crescente, caso o parâmetro não seja passado.
//Usar o cache, guarda o parâmetro na memória cache e faz a pesquisa no banco de dados mais rápida, pois ele não lê o código linha a linha na segunda requisição.
//O cache deve ser usado apenas em informações que raramente são atualizadas, pois a atualização pode tornar o programa lento.





