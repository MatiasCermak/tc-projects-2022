package compiladores.utils.optimizer.impl;

import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import compiladores.utils.Quintet;
import compiladores.utils.Quintets;
import compiladores.utils.optimizer.IOptimizer;

public class TempVariableDisposingOptimizer implements IOptimizer{

    @Override
    public void process(Quintets tac) {
        System.out.println("Running -> " + this.getClass().getSimpleName());
        
        for(int i = 0; i < tac.size(); i++) {
            
            Quintet quintet = tac.get(i);

            if(StringUtils.isEmpty(quintet.getOp()) && StringUtils.startsWith(quintet.getArg1(), "T") && StringUtils.isNotEmpty(quintet.getRes())) {
                tac.get(i-1).setRes(quintet.getRes());        
            }

        }

        Iterator<Quintet> quintetIterator = tac.iterator();

        while(quintetIterator.hasNext()) {
            Quintet quintet = quintetIterator.next();
            if(StringUtils.isEmpty(quintet.getOp()) && StringUtils.startsWith(quintet.getArg1(), "T") && StringUtils.isNotEmpty(quintet.getRes())) {
                quintetIterator.remove();       
            }
        }
        
    }
    
}
