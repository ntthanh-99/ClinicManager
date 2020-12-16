package doctor;

import lombok.Data;

@Data
public class Support {
	private int id;
	private Nurse nurse;
	private Test test;
}
