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

import doctor.Doctor;
import doctor.Person;

@Controller
public class DoctorController {
	private RestTemplate rest = new RestTemplate();
	@GetMapping("/bac-si")
	public ModelAndView addModeltoView() {
		List<Doctor> listDoctors =Arrays.asList(rest.getForObject("https://clinic-rest-api.herokuapp.com/doctor",Doctor[].class));
	    ModelAndView modelAndView = new ModelAndView("dsDoctor");
	    modelAndView.addObject("listDoctor", listDoctors);
	    return modelAndView;
	}
	@GetMapping("bac-si/them-moi")
	public String addNew(Model model) {
		Person person= new Person();
		Doctor doctor = new Doctor();
		doctor.setPerson(person);
		//model.addAttribute("person",person);
		model.addAttribute("doctor", doctor);
		return "addDoctor";
	}
	@PostMapping("bac-si/luu")
	public String save(Doctor doctor) {
		Person person = new Person();
		person.setCmt(doctor.getPerson().getCmt());
		person.setName(doctor.getPerson().getName());
		person.setBirth(doctor.getPerson().getBirth());
		person.setAddress(doctor.getPerson().getAddress());
		person.setPhone(doctor.getPerson().getPhone());
		rest.postForObject("https://clinic-rest-api.herokuapp.com/person",person, Person.class);
		rest.postForObject("https://clinic-rest-api.herokuapp.com/doctor",doctor, Doctor.class);
		return "redirect:/bac-si";
	}
	@PutMapping("bac-si/luu")
	public String update(Doctor doctor) {
		Person person = new Person();
		person.setCmt(doctor.getPerson().getCmt());
		person.setName(doctor.getPerson().getName());
		person.setBirth(doctor.getPerson().getBirth());
		person.setAddress(doctor.getPerson().getAddress());
		person.setPhone(doctor.getPerson().getPhone());
		rest.put("https://clinic-rest-api.herokuapp.com/person/{id}",person, person.getCmt());
		rest.put("https://clinic-rest-api.herokuapp.com/doctor/{id}",doctor, doctor.getId());
		return "redirect:/bac-si";
	}
	@RequestMapping("/bac-si/cap-nhat")
	public String edit(@RequestParam("id") Integer id,Model model) {
		Doctor doctorEdit=rest.getForObject("https://clinic-rest-api.herokuapp.com/doctor/{id}",Doctor.class,id);
		model.addAttribute("doctor",doctorEdit);
		return "editDoctor";
	}
	
	@GetMapping("bac-si/xoa")
	public String delete(@RequestParam String id) {
		Doctor doctorDelete=rest.getForObject("https://clinic-rest-api.herokuapp.com/doctor/{id}",Doctor.class,id);
		String cmt= doctorDelete.getPerson().getCmt();
		rest.delete("https://clinic-rest-api.herokuapp.com/doctor/{id}",id);
		rest.delete("https://clinic-rest-api.herokuapp.com/person/{id}",cmt);
		return "redirect:/bac-si";
	}
	@GetMapping("bac-si/tim-kiem")
	public ModelAndView search(@RequestParam String keyword,Model model) {
		List<Doctor> listDoctor = Arrays.asList(rest.getForObject("https://clinic-rest-api.herokuapp.com/doctor/search/{keyword}", Doctor[].class,keyword));
		
	    ModelAndView modelAndView = new ModelAndView("searchDoctorResult");
	    modelAndView.addObject("listDoctor", listDoctor);
	    return modelAndView;
	}
}
