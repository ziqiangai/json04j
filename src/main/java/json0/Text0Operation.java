package json0;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Text0Operation implements Operation, Cloneable {
    public Integer p;
    public String i;
    public String d;

    @Override
    public Text0Operation clone() {
        Text0Operation t = new Text0Operation();
        t.p = p;
        t.i = i;
        t.d = d;
        return t;
    }
}
