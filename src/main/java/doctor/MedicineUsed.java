package doctor;

import lombok.Data;

@Data
public class MedicineUsed {
	private int id;
	private int amount;
	private Medicine medicine;
}
