package doctor;

import lombok.Data;

@Data
public class Disease {
	private int id;
	private String name;
	private String symptom;
	private String treatment;
	private String complication;
}
