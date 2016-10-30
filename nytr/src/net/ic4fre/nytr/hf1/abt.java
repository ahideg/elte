package net.ic4fre.nytr.hf1;

import java.util.Set;

interface AbtNode {
  Object data();
  AbtNode[] children();
  AbtNode parent();
}

class AbtBuilder {
  void setRoot() {}
  AbtNode build() {
    return null;
  }
}

abstract class Type {}

class Int extends Type {
  int toInt() {
    return 0;
  }
}

class Str extends Type {
  @Override
  public String toString() {
    return "";
  }
}

class Variable {
  private final String name = "";
  private final Type type = null;
  
  String getName() {
    return name;
  }
  
  Type getType() {
    return type;
  }  
}

class Context {
  Context(final Set<Variable> m) {
  }
}

class Operator {
}