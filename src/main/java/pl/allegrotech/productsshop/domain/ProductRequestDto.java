package pl.allegrotech.productsshop.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductRequestDto {

  private final String id;
  private final String name;

  @JsonCreator
  public ProductRequestDto(@JsonProperty("id") String id, @JsonProperty("name") String name) {
    this.id = id;
    this.name = name;
  }

  @JsonIgnore
  public boolean isValid() {
    return !Strings.isNullOrEmpty(name);
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return "ProductRequestDto{" + "id='" + id + '\'' + ", name='" + name + '\'' + '}';
  }
}
