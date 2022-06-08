package compiladores.domain;

public class Id {
  private String id;
  private Type type;
  private boolean initialized;
  private boolean used;

  public Id(String id, Type type) {
    this.id = id;
    this.type = type;
    this.initialized = false;
    this.used = false;
  }

  public String getId() {
    return id;
  }

  public Type getType() {
    return type;
  }



  public boolean isInitialized() {
    return initialized;
  }

  public void setInitialized(boolean initialized) {
    this.initialized = initialized;
  }

  public boolean isUsed() {
    return used;
  }

  public void setUsed(boolean used) {
    this.used = used;
  }

  


}


