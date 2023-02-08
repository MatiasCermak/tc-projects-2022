package compiladores.utils.optimizer.impl;

import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import compiladores.utils.Quintet;
import compiladores.utils.Quintets;
import compiladores.utils.ThreeAddressCodeManager;
import compiladores.utils.optimizer.IOptimizer;

public class LabelSortingOptimizer implements IOptimizer {

    @Override
    public void process(Quintets tac) {
        System.out.println("Running -> " + this.getClass().getSimpleName());
        Iterator<Quintet> quintetIterator = tac.iterator();
        String label = "";
        while(quintetIterator.hasNext()) {
            Quintet quintet = quintetIterator.next();
            if(ThreeAddressCodeManager.LBL.equals(quintet.getOp())) {
                label = quintet.getLabel();
                quintetIterator.remove();
            } else if(StringUtils.isNotEmpty(label)) {
                quintet.setLabel(label);
                label = StringUtils.EMPTY;
            }
        }
    }
    
}
