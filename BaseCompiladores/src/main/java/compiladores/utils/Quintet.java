package compiladores.utils;

import org.apache.commons.lang3.StringUtils;

public class Quintet {
    private String label = "";
    private String arg1 = "";
    private String arg2 = "";
    private String op = "";
    private String res = "";

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public String getArg1() {
        return arg1;
    }
    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }
    public String getArg2() {
        return arg2;
    }
    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }
    public String getOp() {
        return op;
    }
    public void setOp(String op) {
        this.op = op;
    }
    public String getRes() {
        return res;
    }
    public void setRes(String res) {
        this.res = res;
    }

    @Override
    public String toString() {
        String labelString = (StringUtils.isEmpty(label)? "  ": label);

        switch(op) {
            case "lbl":
             return labelString + " " + op + " " + res;
            case "pop":
             return labelString  + " " + res + " = " + op;
            case "je":
            case "jmp":
            case "psh":
             return labelString + " " + op + " " + arg1 + " " + arg2;
            case "end":
             return labelString + " " + op;
            default:
                return  labelString + " " + res + " = " + arg1 + " " + op + " " + arg2;
        }
    }
}
