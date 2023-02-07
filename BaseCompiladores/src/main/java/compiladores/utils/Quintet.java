package compiladores.utils;

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
        return "label= " + label + " res= " + res + " arg1= " + arg1 + " op= " + op + " arg2= " + arg2 ;
    }
}
