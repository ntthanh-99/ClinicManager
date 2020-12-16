package doctor.web;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import doctor.Nurse;
import doctor.Support;



@Controller
public class PayrollNurseController {
	private RestTemplate rest = new RestTemplate();
	private Nurse nurse=null;
	@GetMapping("/tinh-luong/y-ta")
	public String showSelectNurse(Model model) {
		return "selectNurse";
	}
	@PostMapping("/tinh-luong/y-ta")
	public ModelAndView showNurse(@RequestParam String keyword,Model model) {
		List<Nurse> listNurse = Arrays.asList(rest.getForObject("https://clinic-rest-api.herokuapp.com/nurse/search/{keyword}", Nurse[].class,keyword));
	    ModelAndView modelAndView = new ModelAndView("selectNurse");
	    modelAndView.addObject("listNurse", listNurse);
	    return modelAndView;
	}
	@GetMapping("/tinh-luong/y-ta/ket-qua")
	public String payrollDoctor(@RequestParam String id, Model model) {
		nurse=rest.getForObject("https://clinic-rest-api.herokuapp.com/nurse/{id}",Nurse.class,id);
		model.addAttribute("nurse", nurse);
		return "payrollNurse";
	}
	@PostMapping("/tinh-luong/y-ta/ket-qua")
	public ModelAndView showTest(@RequestParam String keyword,Model model) {
	    ModelAndView modelAndView = new ModelAndView("payrollNurse");
	    //setAtribute Nurse
	    modelAndView.addObject("nurse", nurse);
	    modelAndView.addObject("month", keyword);
	    //set listSupport
	    List<Support> listSupport=Arrays.asList(rest.getForObject("https://clinic-rest-api.herokuapp.com/support/statistic/nurse/{keyword}/{id}", Support[].class,keyword,nurse.getId()));
	    modelAndView.addObject("listSupport", listSupport);
	    int count=0;
	    for(int i=0;i<listSupport.size();i++) {
	    	count++;
	    }
	    
	    int addSalary=count*200000;
	    int total=nurse.getSalary()+addSalary;
	    modelAndView.addObject("addSalary", addSalary);
	    modelAndView.addObject("total", total);
	    return modelAndView;
	}
}
