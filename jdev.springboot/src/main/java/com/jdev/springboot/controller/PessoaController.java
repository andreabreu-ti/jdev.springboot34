package com.jdev.springboot.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.jdev.springboot.model.Pessoa;
import com.jdev.springboot.model.Telefone;
import com.jdev.springboot.repository.PessoaRepository;
import com.jdev.springboot.repository.TelefoneRepository;

import jakarta.validation.Valid;

@Controller
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private TelefoneRepository telefoneRepository;

	@RequestMapping(method = RequestMethod.GET, value = "/cadastropessoa")
	public ModelAndView inicio() {

		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		modelAndView.addObject("pessoaobj", new Pessoa());

		Iterable<Pessoa> pessoaIt = pessoaRepository.findAll();
		modelAndView.addObject("pessoas", pessoaIt);

		return modelAndView;
	}

	/**
	 * Metodo para salvar no banco de dados
	 */
	@PostMapping("/salvarpessoa")
//	@RequestMapping(method = RequestMethod.POST, value = "/salvarpessoa")
	public ModelAndView salvar(@Valid Pessoa pessoa, BindingResult bindingResult) {

		pessoa.setTelefones(telefoneRepository.getTelefones(pessoa.getId()));
		
		if (bindingResult.hasErrors()) {
			
			ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");

			Iterable<Pessoa> pessoaIt = pessoaRepository.findAll();
			modelAndView.addObject("pessoas", pessoaIt);
			modelAndView.addObject("pessoaobj", pessoa);

			List<String> msg = new ArrayList<String>();

			for (ObjectError objectError : bindingResult.getAllErrors()) {

				msg.add(objectError.getDefaultMessage()); // Vem da anitações @NotEmpty

			}

			modelAndView.addObject("msg", msg);
			return modelAndView;
		}

		pessoaRepository.save(pessoa);

		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		Iterable<Pessoa> pessoaIt = pessoaRepository.findAll();
		andView.addObject("pessoas", pessoaIt);
		andView.addObject("pessoaobj", new Pessoa());

		return andView;
	}

	/**
	 * Metodo para listar os cadastros
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/listapessoas")
	public ModelAndView pessoas() {

		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		Iterable<Pessoa> pessoaIt = pessoaRepository.findAll();
		andView.addObject("pessoas", pessoaIt);
		andView.addObject("pessoaobj", new Pessoa());
		return andView;

	}

	/**
	 * Metodo par editar
	 */
	@GetMapping("/editarpessoa/{idpessoa}")
	public ModelAndView editar(@PathVariable("idpessoa") Long idpessoa) {

		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);

		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		modelAndView.addObject("pessoaobj", pessoa.get());

		return modelAndView;

	}

	/**
	 * Método para remover pessoas
	 */
	@GetMapping("/removerpessoa/{idpessoa}")
	public ModelAndView excluir(@PathVariable("idpessoa") Long idpessoa) {

		pessoaRepository.deleteById(idpessoa);

		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		modelAndView.addObject("pessoas", pessoaRepository.findAll());
		modelAndView.addObject("pessoaobj", new Pessoa());

		return modelAndView;

	}

	/**
	 * Metodo para pesquisar por nome
	 */
	@PostMapping("/pesquisarpessoa")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa) {

		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		modelAndView.addObject("pessoas", pessoaRepository.findPessoaByName(nomepesquisa));
		modelAndView.addObject("pessoaobj", new Pessoa());
		return modelAndView;
	}

	/**
	 * Metodo para telefones
	 */
	@GetMapping("/telefones/{idpessoa}")
	public ModelAndView telefones(@PathVariable("idpessoa") Long idpessoa) {

		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);

		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		modelAndView.addObject("pessoaobj", pessoa.get());
		modelAndView.addObject("telefones", telefoneRepository.getTelefones(idpessoa));

		return modelAndView;

	}

	/*
	 * Método para salvar telefones por id
	 */
	@PostMapping("/addfonePessoa/{pessoaid}")
	public ModelAndView addFonePessoa(Telefone telefone, @PathVariable("pessoaid") Long pessoaid) {

		Pessoa pessoa = pessoaRepository.findById(pessoaid).get();
		
		if (telefone != null && telefone.getNumero().isEmpty() || telefone.getTipo().isEmpty()) {
			
			ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
			modelAndView.addObject("pessoaobj", pessoa);
			modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoaid));
			
			List<String> msg = new ArrayList<String>();
			
			if (telefone.getNumero().isEmpty()) {
				msg.add("Número deve ser informado");
			}
			if (telefone.getTipo().isEmpty()) {
				msg.add("Tipo deve ser informado");
			}
			
			modelAndView.addObject("msg", msg);	
			return modelAndView;
		}
		
		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		telefone.setPessoa(pessoa);
		telefoneRepository.save(telefone);
		modelAndView.addObject("pessoaobj", pessoa);
		modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoaid));
		return modelAndView;

	}

	/**
	 * Método para remover telefone
	 */
	@GetMapping("/removertelefone/{idtelefone}")
	public ModelAndView removertelefone(@PathVariable("idtelefone") Long idtelefone) {

		Pessoa pessoa = telefoneRepository.findById(idtelefone).get().getPessoa();

		telefoneRepository.deleteById(idtelefone);

		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		modelAndView.addObject("pessoaobj", pessoa);
		modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoa.getId()));

		return modelAndView;

	}

}
