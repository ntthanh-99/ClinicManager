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

import doctor.Medicine;


@Controller
public class MedicineController {
	private RestTemplate rest = new RestTemplate();
	@GetMapping("/thuoc")
	public ModelAndView addModeltoView() {
		List<Medicine> listMedicine =Arrays.asList(rest.getForObject("http://localhost:8080/medicine",Medicine[].class));
	    ModelAndView modelAndView = new ModelAndView("/medicine/dsMedicine");
	    modelAndView.addObject("listMedicine", listMedicine);
	    return modelAndView;
	}
	@GetMapping("thuoc/them-moi")
	public String addNew(Model model) {
		Medicine medicine=new Medicine();
		model.addAttribute("medicine", medicine);
		return "/medicine/addMedicine";
	}
	@PostMapping("thuoc/luu")
	public String save(Medicine medicine) {
		rest.postForObject("http://localhost:8080/medicine",medicine, Medicine.class);
		return "redirect:/thuoc";
	}
	@PutMapping("thuoc/luu")
	public String update(Medicine medicine) {
		rest.put("http://localhost:8080/medicine/{id}",medicine, medicine.getId());
		return "redirect:/thuoc";
	}
	@RequestMapping("/thuoc/cap-nhat")
	public String edit(@RequestParam("id") Integer id,Model model) {
		Medicine medicineEdit=rest.getForObject("http://localhost:8080/medicine/{id}",Medicine.class,id);
		model.addAttribute("medicine",medicineEdit);
		return "/medicine/editMedicine";
	}
	
	@GetMapping("thuoc/xoa")
	public String delete(@RequestParam String id) {
		rest.delete("http://localhost:8080/medicine/{id}",id);
		return "redirect:/thuoc";
	}
	@GetMapping("thuoc/tim-kiem")
	public ModelAndView search(@RequestParam String keyword,Model model) {
		List<Medicine> listMedicine = Arrays.asList(rest.getForObject("http://localhost:8080/medicine/search/{keyword}", Medicine[].class,keyword));
		
	    ModelAndView modelAndView = new ModelAndView("/medicine/searchMedicineResult");
	    modelAndView.addObject("listMedicine", listMedicine);
	    return modelAndView;
	}
}

