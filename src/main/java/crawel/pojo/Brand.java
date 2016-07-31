package crawel.pojo;

public class Brand {
	public Brand() {

	}

	

	@Override
	public String toString() {
		return "Brand [name=" + name + "]";
	}



	public Brand(String name) {
		this.name = name;
	}

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
