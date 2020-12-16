package doctor;

import lombok.Data;

@Data
public class Nurse {
	private int id;
	
	private String level;
	private int exp;
	private int salary;
	private Person person;
}
