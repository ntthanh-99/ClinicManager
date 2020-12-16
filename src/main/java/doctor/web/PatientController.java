package doctor.web;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import doctor.Patient;
import doctor.Person;

@Controller
public class PatientController {
	private RestTemplate rest = new RestTemplate();
	@GetMapping("/benh-nhan")
	public ModelAndView addModeltoView() {
		List<Patient> listPatients =Arrays.asList(rest.getForObject("https://clinic-rest-api.herokuapp.com/patient",Patient[].class));
	    ModelAndView modelAndView = new ModelAndView("dsPatient");
	    modelAndView.addObject("listPatient", listPatients);
	    return modelAndView;
	}
	@GetMapping("benh-nhan/them-moi")
	public String addNew(Model model) {
		Person person= new Person();
		Patient patient = new Patient();
		patient.setPerson(person);
		//model.addAttribute("person",person);
		model.addAttribute("patient", patient);
		return "addPatient";
	}
	@PostMapping("benh-nhan/luu")
	public String save(Patient patient) {
		Person person = new Person();
		person.setCmt(patient.getPerson().getCmt());
		person.setName(patient.getPerson().getName());
		person.setBirth(patient.getPerson().getBirth());
		person.setAddress(patient.getPerson().getAddress());
		person.setPhone(patient.getPerson().getPhone());
		rest.postForObject("https://clinic-rest-api.herokuapp.com/person",person, Person.class);
		rest.postForObject("https://clinic-rest-api.herokuapp.com/patient",patient, Patient.class);
		return "redirect:/benh-nhan";
	}
	@PutMapping("benh-nhan/luu")
	public String update(Patient patient) {
		Person person = new Person();
		person.setCmt(patient.getPerson().getCmt());
		person.setName(patient.getPerson().getName());
		person.setBirth(patient.getPerson().getBirth());
		person.setAddress(patient.getPerson().getAddress());
		person.setPhone(patient.getPerson().getPhone());
		rest.put("https://clinic-rest-api.herokuapp.com/person/{id}",person, person.getCmt());
		rest.put("https://clinic-rest-api.herokuapp.com/patient/{id}",patient, patient.getId());
		return "redirect:/benh-nhan";
	}
	@RequestMapping("/benh-nhan/cap-nhat")
	public String edit(@RequestParam("id") Integer id,Model model) {
		Patient patientEdit=rest.getForObject("https://clinic-rest-api.herokuapp.com/patient/{id}",Patient.class,id);
		model.addAttribute("patient",patientEdit);
		return "editPatient";
	}
	
	@GetMapping("benh-nhan/xoa")
	public String delete(@RequestParam String id) {
		Patient patientDelete=rest.getForObject("https://clinic-rest-api.herokuapp.com/patient/{id}",Patient.class,id);
		String cmt= patientDelete.getPerson().getCmt();
		rest.delete("https://clinic-rest-api.herokuapp.com/patient/{id}",id);
		rest.delete("https://clinic-rest-api.herokuapp.com/person/{id}",cmt);
		return "redirect:/benh-nhan";
	}
	@GetMapping("benh-nhan/tim-kiem")
	public ModelAndView search(@RequestParam String keyword,Model model) {
		List<Patient> listPatient = Arrays.asList(rest.getForObject("https://clinic-rest-api.herokuapp.com/patient/search/{keyword}", Patient[].class,keyword));
		
	    ModelAndView modelAndView = new ModelAndView("searchPatientResult");
	    modelAndView.addObject("listPatient", listPatient);
	    return modelAndView;
	}
}
