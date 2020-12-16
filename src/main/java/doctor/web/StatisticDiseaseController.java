package doctor.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import doctor.Disease;
import doctor.DiseaseStat;
import doctor.Doctor;
import doctor.Patient;
import doctor.Person;
import doctor.Test;

@Controller
public class StatisticDiseaseController {
	private RestTemplate rest = new RestTemplate();
	private String month;
	ArrayList<Test> listTest=null;
	ArrayList<DiseaseStat> listDiseaseStat=null;
	@GetMapping("/thong-ke/benh")
	public String show(Model model) {
		return "statisticDisease";
	}
	@PostMapping("/thong-ke/benh")
	public ModelAndView statistic(@RequestParam String keyword,Model model) {
		this.month=keyword;
		// lấy ds khám bệnh trong tháng
		List<Test> listTesttmp = Arrays.asList(rest.getForObject("https://clinic-rest-api.herokuapp.com/test/statistic/disease/{keyword}", Test[].class,keyword));
		// đếm số lần mặc bệnh
		//List k xóa đc nên convert sang ArrayList 
		listTest=new ArrayList<>(listTesttmp);
		ArrayList<Test> listTest2=new ArrayList<>(listTesttmp);
		//
		listDiseaseStat = new ArrayList<>();
		for(int i=0;i<listTest2.size();i++) {
			DiseaseStat diseaseStat=new DiseaseStat();
			diseaseStat.setId(listTest2.get(i).getDisease().getId());
			diseaseStat.setName(listTest2.get(i).getDisease().getName());
			int amount=1;
			for(int j=i+1;j<listTest2.size();j++) {
				if(listTest2.get(i).getDisease().getId()==listTest2.get(j).getDisease().getId()) {
					amount++;
					listTest2.remove(j);
					j--;
				}
			}
			diseaseStat.setAmount(amount);
			listDiseaseStat.add(diseaseStat);	
		}
		//
	    ModelAndView modelAndView = new ModelAndView("statisticDisease");
	    modelAndView.addObject("listDiseaseStat", listDiseaseStat);
	    return modelAndView;
	}
	@GetMapping("/thong-ke/benh/chi-tiet")
	public ModelAndView detail(@RequestParam String name,Model model) {
		ModelAndView modelAndView = new ModelAndView("detailDisease");
		// lấy lại thông tin bệnh
		for(int i=0;i<listDiseaseStat.size();i++) {
			if(listDiseaseStat.get(i).getName().equals(name)) {
				modelAndView.addObject("diseaseStat", listDiseaseStat.get(i));
				break;
			}
		
		
		
		}
		// lấy lại
		ArrayList<Test> detailDisease=new ArrayList<>();
		for(int i=0;i<listTest.size();i++) {
			if(listTest.get(i).getDisease().getName().equals(name)) {
				detailDisease.add(listTest.get(i));
			}
		}
	    modelAndView.addObject("listTest", detailDisease);
	    return modelAndView;
	}
	
}
