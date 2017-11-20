public class SubtitleSeqC implements SubtitleSeq {
	
	private List<Subtitle> listOfSubs = new LinkedList<Subtitle>() ;
	
	
	@Override
	public void addSubtitle(Subtitle st) {
		if(listOfSubs.empty())
			listOfSubs.insert(st);
			else {
			Subtitle tmpSub;
			int tmpTime = toMS(st.getStartTime());
			int curTime;

			listOfSubs.findFirst();
			while(!listOfSubs.last()) {
				curTime = toMS(listOfSubs.retrieve().getStartTime());
				if(tmpTime<curTime) {
					tmpSub= listOfSubs.retrieve();
					listOfSubs.update(st);
					listOfSubs.insert(tmpSub);
					return;
				}
				listOfSubs.findNext();
			}
			curTime = toMS(listOfSubs.retrieve().getStartTime());
			if(tmpTime<curTime) {
				tmpSub = listOfSubs.retrieve();
				listOfSubs.update(st);
				listOfSubs.insert(tmpSub);
				return;
			}

			listOfSubs.insert(st);
			listOfSubs.findNext();
			}
		
	}

	@Override
	public List<Subtitle> getSubtitles(){
		return this.listOfSubs;
	}
	
	@Override
	public Subtitle getSubtitle(Time time) {
		if(this.listOfSubs.empty())
			return null;
		listOfSubs.findFirst();
		while(!listOfSubs.last()){
			if(toMS(listOfSubs.retrieve().getStartTime()) <= toMS(time) &&
					toMS(listOfSubs.retrieve().getEndTime()) >= toMS(time))
				return listOfSubs.retrieve();
			listOfSubs.findNext();
		}
		if(toMS(listOfSubs.retrieve().getStartTime()) <= toMS(time) &&
				toMS(listOfSubs.retrieve().getEndTime()) >= toMS(time))
			return listOfSubs.retrieve();
		
		return null;
	}
	//Doesn't Work
	@Override
	public List<Subtitle> getSubtitles(Time startTime, Time endTime) {
		List<Subtitle> tmpList;
		SubtitleSeq tmpSeq = new SubtitleSeqC();
		
		listOfSubs.findFirst();
		while(!listOfSubs.last()) {
			if((toMS(startTime)>=toMS(listOfSubs.retrieve().getStartTime()))&&
					(toMS(endTime)<=toMS(listOfSubs.retrieve().getEndTime())))
				tmpSeq.addSubtitle(listOfSubs.retrieve());
			else
				listOfSubs.findNext();
		}
		if((toMS(startTime)>=toMS(listOfSubs.retrieve().getStartTime()))&&
				(toMS(endTime)<=toMS(listOfSubs.retrieve().getEndTime())))
			tmpSeq.addSubtitle(listOfSubs.retrieve());
		tmpList = tmpSeq.getSubtitles();
		return tmpList;
	}

	@Override
	public List<Subtitle> getSubtitles(String str) {
	    SubtitleSeq tmpSeq = new SubtitleSeqC();
	    if (listOfSubs.empty()) {
			return null;
		}
	    else {
		    listOfSubs.findFirst();

		    while (!listOfSubs.last()) {
				if (listOfSubs.retrieve().getText().contains(str)) {
					tmpSeq.addSubtitle(listOfSubs.retrieve());
				}
				listOfSubs.findNext();
			}
		    if (listOfSubs.retrieve().getText().contains(str)) {
				tmpSeq.addSubtitle(listOfSubs.retrieve());

			}
		}
	    return tmpSeq.getSubtitles();
		
	}


	@Override
	public void remove(String str) {
		if(this.listOfSubs.empty())
			return;
		listOfSubs.findFirst();
		while(!listOfSubs.last()) {
			if(listOfSubs.retrieve().getText().contains(str)) 
				listOfSubs.remove();
			else
				listOfSubs.findNext();
		}
		if(listOfSubs.retrieve().getText().contains(str))
			listOfSubs.remove();
	}

	@Override
	public void replace(String str1, String str2) {
		if(this.listOfSubs.empty())
			return;
		listOfSubs.findFirst();
		while(!listOfSubs.last()) {
			Subtitle tmp = listOfSubs.retrieve();
			if(tmp.getText().contains(str1)) {
				tmp.setText(listOfSubs.retrieve().getText().replace(str1, str2));
			    listOfSubs.update(tmp);
			}
			listOfSubs.findNext();
		}
		Subtitle tmp = listOfSubs.retrieve();
		if(tmp.getText().equalsIgnoreCase(str1)) {
			tmp.setText(listOfSubs.retrieve().getText().replace(str1, str2));
		    listOfSubs.update(tmp);
		}
		
	}
	@Override
	public void shift(int offset) {
		if(this.listOfSubs.empty() )
			return;
		else {
			listOfSubs.findFirst();
			while(!listOfSubs.last()) {
				int msStartTime = toMS(listOfSubs.retrieve().getStartTime());
				int msEndTime = toMS(listOfSubs.retrieve().getEndTime());
			
				msStartTime += offset;
				msEndTime += offset;
			
				if(msStartTime < 0)
					msStartTime = 0;
				if(msEndTime < 0) 
					msEndTime = 0;	
			
				Time startTime = toTime(msStartTime);
				Time endTime = toTime(msEndTime);
			
				Subtitle tmp = listOfSubs.retrieve();
			
				tmp.setStartTime(startTime);
				tmp.setEndTime(endTime);
			
				
				listOfSubs.update(tmp);
				if(msEndTime == 0)
					listOfSubs.remove();
				else
					listOfSubs.findNext();
			}
			int msStartTime = toMS(listOfSubs.retrieve().getStartTime());
			int msEndTime = toMS(listOfSubs.retrieve().getEndTime());
		
			msStartTime += offset;
			msEndTime += offset;
		
			if(msStartTime < 0)
				msStartTime = 0;
			if(msEndTime < 0) 
				msEndTime = 0;
				
			Time startTime = toTime(msStartTime);
			Time endTime = toTime(msEndTime);
		
			Subtitle tmp = listOfSubs.retrieve();
		
			tmp.setStartTime(startTime);
			tmp.setEndTime(endTime);
			if(msEndTime == 0)
				listOfSubs.remove();
			else	
				listOfSubs.update(tmp);
		}
	}

	@Override
	public void cut(Time startTime, Time endTime) {
		if(this.listOfSubs.empty())
			return;
		else {
			int offset = (toMS(endTime)-toMS(startTime))*(-1);
			listOfSubs.findFirst();
			while(!listOfSubs.last()){
				while((toMS(startTime)>=toMS(listOfSubs.retrieve().getStartTime()))&&
						(toMS(endTime)<=toMS(listOfSubs.retrieve().getEndTime()))) 
							this.listOfSubs.remove();
				
				this.listOfSubs.findNext();
			}
			if((toMS(startTime)>=toMS(listOfSubs.retrieve().getStartTime()))&&
					(toMS(endTime)<=toMS(listOfSubs.retrieve().getEndTime()))) 
				this.listOfSubs.remove();
				
			shift(offset);
		}
	}
		
		private static int toMS(Time t){
			return (t.getHH()*3600000 + t.getMM()*60000 + t.getSS()*1000 + t.getMS()) ;
		}
		
		private static Time toTime(int ms){
			int RealTimeHH = ((ms/3600000)%24) ;
			int RealTimeMM = ((ms/60000)%60) ;
			int RealTimeSS = ((ms/1000)%60) ;
			int RealTimeMS = ms%1000 ;
			Time R = new TimeC();
			R.setHH(RealTimeHH);
			R.setMM(RealTimeMM);
			R.setSS(RealTimeSS);
			R.setMS(RealTimeMS);
			return R;
		}
		
		
		
		public static void main(String[] args) throws Exception {
			SubtitleSeq seq = SubtitleSeqFactory.loadSubtitleSeq("winnie-the-pooh-2011.srt");
			
			List<Subtitle> l = seq.getSubtitles();
	
			
			  l.findFirst();
			 while(!l.last()) {
				System.out.println(l.retrieve().getText());
				System.out.println();
				l.findNext();
			}
			System.out.println(l.retrieve().getText());
		}	
}
