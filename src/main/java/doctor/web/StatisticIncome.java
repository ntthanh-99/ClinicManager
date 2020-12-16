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

import doctor.MedicineUsed;
import doctor.Patient;
import doctor.Prescription;
import doctor.Test;

@Controller
public class StatisticIncome {
	private RestTemplate rest = new RestTemplate();
	List<Test> listTest=null;
	List<Prescription> listPrescription= null;
	int incomeTotal=0;
	int incomeTest=0;
	int incomePrescription=0;
	@GetMapping("/thong-ke/doanh-thu")
	public String show(Model model) {
		return "statisticClinic";
	}
	@PostMapping("/thong-ke/doanh-thu")
	public ModelAndView showStatistic(@RequestParam String keyword,Model model) {
		listTest = Arrays.asList(rest.getForObject("https://clinic-rest-api.herokuapp.com/test/statistic/income/{keyword}", Test[].class,keyword));
		listPrescription = Arrays.asList(rest.getForObject("https://clinic-rest-api.herokuapp.com/prescription/statistic/income/{keyword}", Prescription[].class,keyword));
	    ModelAndView modelAndView = new ModelAndView("statisticClinic");
	    int tempTest=0;
	    int tempPrescription=0;
	    for(int i=0;i<listTest.size();i++) {
	    	tempTest+=listTest.get(i).getTotal();
	    }
	    for(int i=0;i<listPrescription.size();i++) {
	    	tempPrescription+=listPrescription.get(i).getTotal();
	    }
	    incomeTest=tempTest;
	    incomePrescription=tempPrescription;
	    incomeTotal=incomeTest+incomePrescription;
	    modelAndView.addObject("incomeTest", incomeTest);
	    modelAndView.addObject("incomePrescription", incomePrescription);
	    modelAndView.addObject("incomeTotal", incomeTotal);
	    
	    return modelAndView;
	}
	@GetMapping("/thong-ke/doanh-thu/kham-benh")
	public String showTest(Model model) {
		model.addAttribute("listTest", listTest);
		model.addAttribute("incomeTest", incomeTest);
		return "detailIncomefromTest";
	}
	@GetMapping("/thong-ke/doanh-thu/don-thuoc")
	public String showPrescription(Model model) {
		model.addAttribute("listPrescription", listPrescription);
		model.addAttribute("incomePrescription", incomePrescription);
		return "detailIncomefromPrescription";
	}
	@GetMapping("/thong-ke/doanh-thu/don-thuoc/chi-tiet")
	public String showMedicineUsed(@RequestParam String id,Model model) {
		List<MedicineUsed> listMedicineUsed = Arrays.asList(rest.getForObject("https://clinic-rest-api.herokuapp.com/medicineused/statistic/income/{id}", MedicineUsed[].class,id));
		model.addAttribute("listMedicineUsed", listMedicineUsed);
		int total=0;
		for(int i=0;i<listMedicineUsed.size();i++) {
			total+=listMedicineUsed.get(i).getAmount()*listMedicineUsed.get(i).getMedicine().getPrice();
		}
		model.addAttribute("total", total);
		return "detailMedicine";
	}
	
}
