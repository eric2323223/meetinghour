package eric.android.meetinghour.model;

public class Hour {
	int h=0;
	
	public Hour(int h){
		if(h<0){
//			this.h = (-h)%24;
			this.h = 24 + h;
		}
		else if(h>23){
			this.h = h%24;
		}else{
			this.h = h;
		}
	}
	
	public String toString(){
//		if(this.h < 12){
//			return new Integer(h).toString() + " AM";
//		}else{
//			return new Integer(h-12).toString() + " PM";
//		}
		return new Integer(h).toString()+":00";
	}
	
	public String translate(){
//		int realhour;
		if(h<0){
			h = 24+h;
		}
		if(h>24){
			h = h-24;
		}
		if(h < 12){
			return new Integer(h).toString()+" AM";
		}else{
			return new Integer(h - 12).toString()+" PM";
		}
	}
	
	public boolean isNight(){
		if(this.h<6 || this.h > 19){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isDayLight(){
		if(this.h > 6 && this.h<19){
			return true;
		}else{
			return false;
		}
	}

}
