package compiladores.domain;

public class Variable extends Id { 

  private String value;

  public Variable(String id, Type type) {
    super(id, type);
  }

  public String toString() {
    return "Variable " + this.getId() + " de tipo " + this.getType();
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
