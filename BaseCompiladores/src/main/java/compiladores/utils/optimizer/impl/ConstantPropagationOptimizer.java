package compiladores.utils.optimizer.impl;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import compiladores.utils.Quintet;
import compiladores.utils.Quintets;
import compiladores.utils.optimizer.IOptimizer;

public class ConstantPropagationOptimizer implements IOptimizer {

    private HashMap<String, String> variableValue = new HashMap<String, String>();

    @Override
    public void process(Quintets tac) {
        System.out.println("Running -> " + this.getClass().getSimpleName());
        Iterator<Quintet> quintetIterator = tac.iterator();

        while (quintetIterator.hasNext()) {
            Quintet quintet = quintetIterator.next();
            
            if (variableValue.get(quintet.getArg1()) != null) {
                quintet.setArg1(variableValue.get(quintet.getArg1()));
            }

            if (variableValue.get(quintet.getArg2()) != null) {
                quintet.setArg2(variableValue.get(quintet.getArg2()));
            }

            if (!StringUtils.isAnyEmpty(quintet.getArg1(), quintet.getArg2(), quintet.getOp())) {


                try {
                    Double arg1 = Double.parseDouble(quintet.getArg1());
                    Double arg2 = Double.parseDouble(quintet.getArg2());
                    Double valuedExp = evaulateExp(arg1, quintet.getOp(), arg2);
                    variableValue.put(quintet.getRes(), valuedExp.toString());
                    quintetIterator.remove();
                } catch (Exception e) {
                    continue;
                }

            }
        }
    }

    private Double evaulateExp(Double arg1, String operation, Double arg2) {
        switch (operation) {
            case "+":
                return arg1 + arg2;
            case "-":
                return arg1 - arg2;
            case "*":
                return arg1 * arg2;
            case "/":
                return arg1 / arg2;
            case "==":
                return (double) (arg1 == arg2 ? 1 : 0);
            case "!=":
                return (double) (arg1 != arg2 ? 1 : 0);
            case "<":
                return (double) (arg1 < arg2 ? 1 : 0);
            case ">":
                return (double) (arg1 > arg2 ? 1 : 0);
            case ">=":
                return (double) (arg1 >= arg2 ? 1 : 0);
            case "<=":
                return (double) (arg1 <= arg2 ? 1 : 0);
            case "&&":
                return (double) ((arg1 > 0) && (arg2 > 0) ? 1 : 0);
            case "||":
                return (double) ((arg1 > 0) || (arg2 > 0) ? 1 : 0);
            default:
                break;
        }
        return (double) 0;
    }

}
