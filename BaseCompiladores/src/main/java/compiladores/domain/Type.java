package compiladores.domain;

public enum Type {
  INT("int"), 
  FLOAT("float"), 
  DOUBLE("double"),
  VOID("void"), 
  CHAR("char");

  private String type;

  Type(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
