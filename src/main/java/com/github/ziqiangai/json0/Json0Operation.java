package com.github.ziqiangai.json0;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
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
    public String toString() {
        StringBuilder sb = new StringBuilder("Json0Operation{");

        if (p != null) {
            sb.append("p=").append(p);
        }

        if (t != null) {
            if (sb.length() > 12) {
                sb.append(", ");
            }
            sb.append("t='").append(t).append('\'');
        }

        if (si != null) {
            if (sb.length() > 12) {
                sb.append(", ");
            }
            sb.append("si='").append(si).append('\'');
        }

        if (sd != null) {
            if (sb.length() > 12) {
                sb.append(", ");
            }
            sb.append("sd='").append(sd).append('\'');
        }

        if (o != null) {
            if (sb.length() > 12) {
                sb.append(", ");
            }
            sb.append("o=").append(o);
        }

        if (na != null) {
            if (sb.length() > 12) {
                sb.append(", ");
            }
            sb.append("na=").append(na);
        }

        if (ld != null) {
            if (sb.length() > 12) {
                sb.append(", ");
            }
            sb.append("ld=").append(ld);
        }

        if (li != null) {
            if (sb.length() > 12) {
                sb.append(", ");
            }
            sb.append("li=").append(li);
        }

        if (od != null) {
            if (sb.length() > 12) {
                sb.append(", ");
            }
            sb.append("od=").append(od);
        }

        if (oi != null) {
            if (sb.length() > 12) {
                sb.append(", ");
            }
            sb.append("oi=").append(oi);
        }

        if (lm != null) {
            if (sb.length() > 12) {
                sb.append(", ");
            }
            sb.append("lm=").append(lm);
        }

        sb.append('}');
        return sb.toString();
    }

    @Override
    public Json0Operation clone() {
        Json0Operation cloned = new Json0Operation();
        if (this.p != null) {
            cloned.p = new ArrayList<>(this.p.size());
            cloned.p.addAll(this.p);
        }else {
            cloned.p = new ArrayList<>();
        }
        if (this.o != null) {
            cloned.o = new ArrayList<>(this.o.size());
            for (Text0Operation text0Operation : this.o) {
                cloned.o.add(text0Operation.clone());
            }
        }
        cloned.t = this.t;
        cloned.si = this.si;
        cloned.sd = this.sd;
        if (this.na != null) {
            cloned.na = this.na.deepCopy();
        }
        if (this.ld != null) {
            cloned.ld = this.ld.deepCopy();
        }
        if (this.li != null) {
            cloned.li = this.li.deepCopy();
        }
        if (this.od != null) {
            cloned.od = this.od.deepCopy();
        }
        if (this.oi != null) {
            cloned.oi = this.oi.deepCopy();
        }
        if (this.lm instanceof Integer) {
            cloned.lm = this.lm;
        }

        return cloned;
    }

}
