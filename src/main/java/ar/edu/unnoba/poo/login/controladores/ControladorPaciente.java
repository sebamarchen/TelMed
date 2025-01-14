package ar.edu.unnoba.poo.login.controladores;

import javax.validation.Valid;

import ar.edu.unnoba.poo.login.detallesUsuario.UsuarioLogueado;
import ar.edu.unnoba.poo.login.repositorios.RepositorioTurno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ar.edu.unnoba.poo.login.entidades.Paciente;
import ar.edu.unnoba.poo.login.servicios.ServicioPaciente;

@Controller
@RequestMapping("/paciente")
public class ControladorPaciente {
	@Autowired
	private ServicioPaciente servicioPaciente;
	@Autowired
	private RepositorioTurno repturno;


	@GetMapping("/inicio")
	public String obtenerTodos(Model model) {
		model.addAttribute("pacientes", servicioPaciente.obtenerTodos());
		return "paciente/index";
	}
	
	@GetMapping("/{id}")
	public String obtenerPorId(@PathVariable Long id, Model model) {
	   model.addAttribute("paciente", servicioPaciente.obtenerPorId(id));
	   return "paciente/detalles";
	}

	@GetMapping("/historial-turnos")
	public String obtenerPorId(Model model, @AuthenticationPrincipal UsuarioLogueado usuario) {
		Paciente p = servicioPaciente.obtenerPorUsuario(usuario.getUsuario());
		model.addAttribute("paciente",p);
		model.addAttribute("turnos", repturno.findAllByPacienteOrderByFecha(p));

		return "paciente/historialturnos";
	}
	
	@GetMapping(params="dni")
	public String obtenerPorDni(@PathVariable Long dni, Model model) {
	   model.addAttribute("paciente", servicioPaciente.obtenerPorDni(dni));
	   return "paciente/detalles";
	}
	
	@PutMapping("/{id}")
	public String cambiarEstado(@PathVariable Long id) {
		servicioPaciente.cambiarEstado(id);
		return "redirect:/paciente/{id}";
	}
	
	@PutMapping("/editar/{id}")
	public String modificar(@Valid @ModelAttribute("paciente") Paciente paciente, @PathVariable Long id, Model model) {
	    servicioPaciente.modificar(paciente, id);
	    return "redirect:/paciente/inicio";
	}
}
