package compiladores.domain;

import java.util.List;

public class Function extends Id {

  private List<Type> parameters;

  public Function(String id, Type type, List<Type> parameters) {
    super(id, type);
    this.parameters = parameters;
  }

  public List<Type> getParameters() {
    return parameters;
  }

  public String toString() {
    return "Function " + this.getId() + " of type " + this.getType() + " with parameters " + this.getParameters()
        + " Used: " + this.isUsed() + " Initialized: "
        + this.isInitialized();
  }

  public void setParameters(List<Type> parameters) {
    this.parameters = parameters;
  }

}
