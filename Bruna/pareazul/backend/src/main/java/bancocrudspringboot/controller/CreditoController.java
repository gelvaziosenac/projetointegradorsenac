package bancocrudspringboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import bancocrudspringboot.exception.ResourceNotFoundException;
import bancocrudspringboot.model.Credito;
import bancocrudspringboot.repository.CreditoRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class CreditoController {
    
    @Autowired
	private CreditoRepository creditoRepository;
    

    // Listar
	@GetMapping("/credito")
	@ResponseStatus(HttpStatus.OK)
	public List<Credito> getAllCadastros() {
		return this.creditoRepository.findAll();

	}

    // Inserir credito
	@PostMapping("/credito")
	@ResponseStatus(HttpStatus.CREATED)
	public Credito createCadastro(@RequestBody Credito credito) {
		return this.creditoRepository.save(credito);
	}

    // alterar credito    
	@PutMapping("/credito/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Credito> updateCadastro(@PathVariable(value = "id") Long cadastroId,
												  @Validated 
												  @RequestBody 
                Credito cadastroCaracteristicas) throws ResourceNotFoundException {
                Credito cadastro = creditoRepository.findById(cadastroId)
				.orElseThrow(() -> new ResourceNotFoundException("Cadastro não encontrado para o ID : " + cadastroId));

		cadastro.setValor(cadastroCaracteristicas.getValor());
		// cadastro.setNome(cadastroCaracteristicas.getNome());
		// cadastro.setDataexpiracao(cadastroCaracteristicas.getDataexpiracao());
		// cadastro.setCvv(cadastroCaracteristicas.getCvv());

		return ResponseEntity.ok(this.creditoRepository.save(cadastro));
	}
	
	@GetMapping("/creditousuario/{usuario}")
	@ResponseStatus(HttpStatus.OK)
	public Credito getCadastroByUsuario(@PathVariable(value = "usuario") Long usuario)
		throws ResourceNotFoundException {
		return creditoRepository.findCreditoByUsuario(usuario);
	}

	// Atualiza saldo
	@PostMapping("/atualizasaldo")
	@ResponseStatus(HttpStatus.CREATED)
	public Credito atualizaSaldo(@RequestBody Credito cadastroFrontend) {		
		long usuario = cadastroFrontend.getUsuario();
		
		// Atualizar o saldo aqui
		Credito creditoAtualBancoDados = creditoRepository.findCreditoByUsuario(usuario);		
		if(creditoAtualBancoDados != null){
			// Atualizar o saldo existente
			creditoAtualBancoDados.setValor(cadastroFrontend.getValor());

			return this.creditoRepository.save(creditoAtualBancoDados);
		}

		// Insere um novo saldo, quando nao existe nenhum saldo no banco de dados
		return this.creditoRepository.save(cadastroFrontend);
	}

}