package eric.android.meetinghour.model;

import java.util.ArrayList;

public class HourData {
	
	private ArrayList<Integer>[] data = new ArrayList[24];
	
	public HourData(){
		for(int i=0;i<24;i++){
			data[i] = new ArrayList<Integer>();
		}
	}
	
	public HourData addTime(int time){
		for(int i=0;i<24;i++){
			data[i].add(time++);
		}
		return this;
	}
	
	public HourData removeTime(int index){
		for(int i=0;i<24;i++){
			data[i].remove(index);
		}
		return this;
	}
	
	public void print(){
		for(ArrayList<Integer> list:data){
			for(Integer time:list){
				System.out.print(time+"\t");
			}
			System.out.println();
		}
	}

	public int size() {
		return 24;
	}

	public ArrayList<Integer> get(int position) {
		return data[position];
	}

	public int length() {
		return data[0].size();
	}
}
