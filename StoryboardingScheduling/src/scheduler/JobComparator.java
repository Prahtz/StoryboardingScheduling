package scheduler;
import java.util.Comparator;

public class JobComparator implements Comparator<Job> {
	public int compare(Job j1, Job j2) {
		if(j1.getValue() > j2.getValue())
			return -1;
		if(j1.getValue() == j2.getValue()) 
			if(j1.getArrivalTime() < j2.getArrivalTime())
				return -1;
			else if(j1.getArrivalTime() == j2.getArrivalTime()) 
				if(j1.getIndex() < j2.getIndex())
					return -1;
				else if(j1.getIndex() == j2.getIndex())
					return 0;
				else
					return 1;
			else
				return 1;
		return 1;
	}
	
}