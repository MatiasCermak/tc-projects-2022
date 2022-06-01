package compiladores.domain;

public class Variable extends Id {
  public Variable(String id, Type type) {
    super(id, type);
  }

  public String toString() {
    return "Variable " + this.getId() + " de tipo " + this.getType();
  }

}
