package crawel.pojo;

import java.util.Comparator;

public class Size implements Comparable<Size> {
	public static Comparator<Size> BRANDNAMESIZECOMPARATOR = new Comparator<Size>() {

		@Override
		public int compare(Size size1, Size size2) {

			Integer lenght1 = size1.getMetric().length();
			Integer lenght2 = size2.getMetric().length();
			// long to sort
			return lenght2.compareTo(lenght1);

		}

	};
	private String metric;
	private Double size;
	public Size(){
		
	}
	public Size(double size, String metric) {
		this.metric = metric;
		this.size = size;
	}

	

	@Override
	public int compareTo(Size o) {
		return this.getSize().compareTo(o.getSize());
	}

	public String getMetric() {
		return metric.toUpperCase();
	}

	public void setMetric(String metric) {
		this.metric = metric;
	}

	@Override
	public String toString() {
		return "Size [metric=" + metric + ", size=" + size + "]";
	}

	public Double getSize() {
		return size;
	}

	public void setSize(Double size) {
		this.size = size;
	}
}
