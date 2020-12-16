package doctor;

import lombok.Data;

@Data
public class Medicine {
	private int id;
	private String name;
	private String effect;
	private String instruction;
	private String contraindication;
	private int price;
}
