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

import doctor.Nurse;
import doctor.Person;
@Controller
public class NurseController {
	private RestTemplate rest = new RestTemplate();
	@GetMapping("/y-ta")
	public ModelAndView addModeltoView() {
		List<Nurse> listNurses =Arrays.asList(rest.getForObject("http://localhost:8080/nurse",Nurse[].class));
	    ModelAndView modelAndView = new ModelAndView("/nurse/dsNurse");
	    modelAndView.addObject("listNurse", listNurses);
	    return modelAndView;
	}
	@GetMapping("y-ta/them-moi")
	public String addNew(Model model) {
		Person person= new Person();
		Nurse nurse = new Nurse();
		nurse.setPerson(person);
		//model.addAttribute("person",person);
		model.addAttribute("nurse", nurse);
		return "/nurse/addNurse";
	}
	@PostMapping("y-ta/luu")
	public String save(Nurse nurse) {
		Person person = new Person();
		person.setCmt(nurse.getPerson().getCmt());
		person.setName(nurse.getPerson().getName());
		person.setBirth(nurse.getPerson().getBirth());
		person.setAddress(nurse.getPerson().getAddress());
		person.setPhone(nurse.getPerson().getPhone());
		rest.postForObject("http://localhost:8080/person",person, Person.class);
		rest.postForObject("http://localhost:8080/nurse",nurse, Nurse.class);
		return "redirect:/y-ta";
	}
	@PutMapping("y-ta/luu")
	public String update(Nurse nurse) {
		Person person = new Person();
		person.setCmt(nurse.getPerson().getCmt());
		person.setName(nurse.getPerson().getName());
		person.setBirth(nurse.getPerson().getBirth());
		person.setAddress(nurse.getPerson().getAddress());
		person.setPhone(nurse.getPerson().getPhone());
		rest.put("http://localhost:8080/person/{id}",person, person.getCmt());
		rest.put("http://localhost:8080/nurse/{id}",nurse, nurse.getId());
		return "redirect:/y-ta";
	}
	@RequestMapping("/y-ta/cap-nhat")
	public String edit(@RequestParam("id") Integer id,Model model) {
		Nurse nurseEdit=rest.getForObject("http://localhost:8080/nurse/{id}",Nurse.class,id);
		model.addAttribute("nurse",nurseEdit);
		return "/nurse/editNurse";
	}
	
	@GetMapping("y-ta/xoa")
	public String delete(@RequestParam String id) {
		Nurse nurseDelete=rest.getForObject("http://localhost:8080/nurse/{id}",Nurse.class,id);
		String cmt= nurseDelete.getPerson().getCmt();
		rest.delete("http://localhost:8080/nurse/{id}",id);
		rest.delete("http://localhost:8080/person/{id}",cmt);
		return "redirect:/y-ta";
	}
	@GetMapping("y-ta/tim-kiem")
	public ModelAndView search(@RequestParam String keyword,Model model) {
		List<Nurse> listNurse = Arrays.asList(rest.getForObject("http://localhost:8080/nurse/search/{keyword}", Nurse[].class,keyword));
		
	    ModelAndView modelAndView = new ModelAndView("/nurse/searchNurseResult");
	    modelAndView.addObject("listNurse", listNurse);
	    return modelAndView;
	}
}
