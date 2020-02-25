package scheduler;
public class Job {
	
	private int index;
	private int arrivalTime;
	private int value;
	private int length;

	public Job(int index, int arrivalTime, int value, int length) {
		this.index = index;
		this.arrivalTime = arrivalTime;
		this.value = value;
		this.length = length;
	}
	
	public Job() {
		this.index = -1;
		this.arrivalTime = -1;
		this.value = -1;
		this.length = -1;
	}
	
	public Job(Job j) {
		this.index = j.index;
		this.arrivalTime = j.arrivalTime;
		this.value = j.value;
		this.length = j.length;
	}
	
	public boolean isEmpty() {
		return arrivalTime == -1 && value == -1 && length == -1;
	}

	public int getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(int arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Job) {
			Job j = (Job) o;
			return j.getIndex() == this.index;
		}
		return false;	
	}
	
	public String toString() {

		return index + " " + arrivalTime + " " + value + " " + length;
	}
	
	public Job clone() {
		return new Job(this.index, this.arrivalTime, this.value, this.length);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}