package nl.inholland;

import java.io.Serializable;

public class Item implements Serializable {

  private static final long serialVersionUID = 1L;

  private String description;
  private boolean complete;

  public Item(String description, boolean complete) {
    this.description = description;
    this.complete = complete;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isComplete() {
    return complete;
  }

  public void setComplete(boolean complete) {
    this.complete = complete;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Item{");
    sb.append("description='").append(description).append('\'');
    sb.append(", complete=").append(complete);
    sb.append('}');
    return sb.toString();
  }
}
