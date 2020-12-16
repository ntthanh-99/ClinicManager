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
	    ModelAndView modelAndView = new ModelAndView("/doctor/dsDoctor");
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
		return "/doctor/addDoctor";
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
		rest.postForObject("http://localhost:8080/doctor",doctor, Doctor.class);
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
		rest.put("http://localhost:8080/person/{id}",person, person.getCmt());
		rest.put("http://localhost:8080/doctor/{id}",doctor, doctor.getId());
		return "redirect:/bac-si";
	}
	@RequestMapping("/bac-si/cap-nhat")
	public String edit(@RequestParam("id") Integer id,Model model) {
		Doctor doctorEdit=rest.getForObject("http://localhost:8080/doctor/{id}",Doctor.class,id);
		model.addAttribute("doctor",doctorEdit);
		return "/doctor/editDoctor";
	}
	
	@GetMapping("bac-si/xoa")
	public String delete(@RequestParam String id) {
		Doctor doctorDelete=rest.getForObject("http://localhost:8080/doctor/{id}",Doctor.class,id);
		String cmt= doctorDelete.getPerson().getCmt();
		rest.delete("http://localhost:8080/doctor/{id}",id);
		rest.delete("http://localhost:8080/person/{id}",cmt);
		return "redirect:/bac-si";
	}
	@GetMapping("bac-si/tim-kiem")
	public ModelAndView search(@RequestParam String keyword,Model model) {
		List<Doctor> listDoctor = Arrays.asList(rest.getForObject("http://localhost:8080/doctor/search/{keyword}", Doctor[].class,keyword));
		
	    ModelAndView modelAndView = new ModelAndView("/doctor/searchDoctorResult");
	    modelAndView.addObject("listDoctor", listDoctor);
	    return modelAndView;
	}
}
