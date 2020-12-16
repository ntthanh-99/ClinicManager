package doctor;

import lombok.Data;

@Data
public class Patient {
	private int id;
	private String position;
	private int exp;
	private String level;
	private String specialty;
	private int salary;
	private Person person;
}
