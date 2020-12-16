package doctor;

import lombok.Data;

@Data
public class Test {
	private int id;
	private String datein;
	private String dateout;
	private String type;
	private String status;
	private int total;
	private Doctor doctor;
	private Patient patient;
	private Disease disease;
}
