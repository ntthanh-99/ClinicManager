package doctor.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import doctor.DiseaseStat;
import doctor.Doctor;
import doctor.Patient;
import doctor.Test;
@Controller
public class StatisticPatientController {
	private RestTemplate rest = new RestTemplate();
	private Patient patient;
	@GetMapping("/thong-ke/benh-nhan")
	public String show(Model model) {
		return "selectPatient";
	}
	@PostMapping("/thong-ke/benh-nhan")
	public ModelAndView showPatient(@RequestParam String keyword,Model model) {
		List<Patient> listPatient = Arrays.asList(rest.getForObject("https://clinic-rest-api.herokuapp.com/patient/search/{keyword}", Patient[].class,keyword));
	    ModelAndView modelAndView = new ModelAndView("selectPatient");
	    modelAndView.addObject("listPatient", listPatient);
	    return modelAndView;
	}
	@GetMapping("/thong-ke/benh-nhan/ket-qua")
	public String payrollDoctor(@RequestParam String id, Model model) {
		patient=rest.getForObject("https://clinic-rest-api.herokuapp.com/patient/{id}",Patient.class,id);
		model.addAttribute("patient", patient);
		List<Test> listTest = Arrays.asList(rest.getForObject("https://clinic-rest-api.herokuapp.com/test/statistic/patient/{id}", Test[].class,id));
		// Đảo ngược mảng
		Collections.reverse(listTest);
		
		model.addAttribute("listTest", listTest);
		return "statisticPatient";
	}
}