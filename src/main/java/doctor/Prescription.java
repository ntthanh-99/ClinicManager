package doctor;

import lombok.Data;

@Data
public class Prescription {
	private int id;
	private String date;
	private Test test;
	private int total;
}
