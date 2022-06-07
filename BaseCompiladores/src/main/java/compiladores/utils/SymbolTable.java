package compiladores.utils;

import java.util.LinkedList;
import java.util.LinkedHashMap;
import java.util.List;

import java.util.Map;
import compiladores.domain.Id;

public class SymbolTable {
  private static List<Map<String, Id>> scopes;
  private static SymbolTable instance;

  private SymbolTable() {
    this.scopes = new LinkedList<Map<String, Id>>();
    this.addScope();
  }

  public static SymbolTable getInstance() {
    if (instance == null) {
      instance = new SymbolTable();
    }
    return instance;
  }

  public void addScope() {
    this.scopes.add(new LinkedHashMap<String, Id>());
  }

  public Map<String, Id> removeScope() {
    return this.scopes.remove(this.scopes.size() - 1);
  }

  public void addId(Id id) {
    this.scopes.get(this.scopes.size() - 1).put(id.getId(), id);
  }

  public Id getId(String id) {
    for (Map<String, Id> scope : this.scopes) {
      if (scope.containsKey(id)) {
        return scope.get(id);
      }
    }
    return null;
  }

  public Id getLocalId(String id) {
    if (this.scopes.get(this.scopes.size() - 1).containsKey(id)) {
      return this.scopes.get(this.scopes.size() - 1).get(id);
    }
    return null;
  }

  public boolean containsId(String id) {
    for (Map<String, Id> scope : this.scopes) {
      if (scope.containsKey(id)) {
        return true;
      }
    }
    return false;
  }

  public boolean containsLocalId(String id) {
    if (this.scopes.get(this.scopes.size() - 1).containsKey(id)) {
      return true;
    }
    return false;
  }
  
  public void print() {
    for (Map<String, Id> scope : this.scopes) {
      for (Map.Entry<String, Id> entry : scope.entrySet()) {
        System.out.println(entry.getKey() + ": " + entry.getValue());
      }
    }
  }

}
