package compiladores.domain;

public enum Type {
  INT("int"), 
  FLOAT("float"), 
  STRING("string"), 
  BOOLEAN("bool"), 
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
