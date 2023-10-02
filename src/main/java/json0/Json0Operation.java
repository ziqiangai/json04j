package json0;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Json0Operation implements Operation,Cloneable{
    public List<Object> p;

    public String t;
    public String si;
    public String sd;
    public List<Text0Operation> o;
    public JsonNode na;
    public JsonNode ld;
    public JsonNode li;
    public JsonNode od;
    public JsonNode oi;
    public Object lm;


    @Override
    public Json0Operation clone() {
        try {
            return (Json0Operation) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

}
