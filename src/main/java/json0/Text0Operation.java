package json0;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Text0Operation implements Operation{
    public Integer p;
    public String i;
    public String d;
}
