package wsd.com.wsd.comparators;


import java.util.Comparator;

import wsd.com.wsd.models.TimeSlot;

public class TimeSlotsComparator implements Comparator<TimeSlot> {
    @Override
    public int compare(TimeSlot currentEvent, TimeSlot proposalEvent) {
        int slot1Beg = currentEvent.getBegin().getInterval();
        int slotProposalBeg = proposalEvent.getBegin().getInterval();
        int slot1End = currentEvent.getEnd().getInterval();
        int slotProposalEnd = proposalEvent.getEnd().getInterval();

        if(slot1Beg==slotProposalBeg && slot1End==slotProposalEnd){
            return 0;
        }
        if(slot1Beg<slotProposalBeg && slot1End<slotProposalEnd && slotProposalBeg>=slot1End){
            return -1;
        }
        if(slot1Beg>=slotProposalEnd){
            return 1;
        }
        else return 0;
    }
}
