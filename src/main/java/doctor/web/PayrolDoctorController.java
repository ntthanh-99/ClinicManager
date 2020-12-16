package doctor.web;

import java.util.ArrayList;
import java.util.Arrays;
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
import doctor.Test;

@Controller
public class PayrolDoctorController {
	private RestTemplate rest = new RestTemplate();
	private Doctor doctor=null;
	@GetMapping("/tinh-luong/bac-si")
	public String showSelectDoctor(Model model) {
		return "/statistic/salary/doctor/selectDoctor";
	}
	@PostMapping("/tinh-luong/bac-si")
	public ModelAndView showDoctor(@RequestParam String keyword,Model model) {
		List<Doctor> listDoctor = Arrays.asList(rest.getForObject("http://localhost:8080/doctor/search/{keyword}", Doctor[].class,keyword));
	    ModelAndView modelAndView = new ModelAndView("/statistic/salary/doctor/selectDoctor");
	    modelAndView.addObject("listDoctor", listDoctor);
	    return modelAndView;
	}
	@GetMapping("/tinh-luong/bac-si/ket-qua")
	public String payrollDoctor(@RequestParam String id, Model model) {
		doctor=rest.getForObject("http://localhost:8080/doctor/{id}",Doctor.class,id);
		model.addAttribute("doctor", doctor);
		return "/statistic/salary/doctor/payrollDoctor";
	}
	@PostMapping("/tinh-luong/bac-si/ket-qua")
	public ModelAndView showTest(@RequestParam String keyword,Model model) {
	    ModelAndView modelAndView = new ModelAndView("/statistic/salary/doctor/payrollDoctor");
	    //setAtribute doctor
	    modelAndView.addObject("doctor", doctor);
	    modelAndView.addObject("month", keyword);
	    //set listTest
	    List<Test> listTest=Arrays.asList(rest.getForObject("http://localhost:8080/test/statistic/doctor/{keyword}/{id}", Test[].class,keyword,doctor.getId()));
	    modelAndView.addObject("listTest", listTest);
	    int count=0;
	    for(int i=0;i<listTest.size();i++) {
	    	count++;
	    }
	    
	    int addSalary=count*1000000;
	    int total=doctor.getSalary()+addSalary;
	    modelAndView.addObject("addSalary", addSalary);
	    modelAndView.addObject("total", total);
	    return modelAndView;
	}
}
