package io.github.ziqiangai.json0;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.util.ArrayList;
import java.util.List;

public class Text0 implements BootstrapTransform<Text0Operation>{

    @Override
    public void append(List<Text0Operation> newOp, Text0Operation c) {
        if ((c.i != null && c.i.isEmpty()) || (c.d != null && c.d.isEmpty())) {
            return;
        }
        if (newOp.size() == 0) {
            newOp.add(c);
        }else {
            Text0Operation last = newOp.get(newOp.size() - 1);
            if (last.i != null && c.i != null && last.p <= c.p && c.p <= last.p + last.i.length()) {
                Text0Operation tmp = newOp.get(newOp.size() - 1);
                tmp.i = strInject(last.i, c.p - last.p, c.i);
                tmp.p = last.p;
            } else if (last.d != null && c.d != null && c.p <= last.p && last.p <= c.p + c.d.length()) {
                Text0Operation tmp = newOp.get(newOp.size() - 1);
                tmp.d = strInject(c.d, last.p - c.p, last.d);
                tmp.p = c.p;
            }else {
                newOp.add(c);
            }
        }
    }

    @Override
    public List<Text0Operation> compose(List<Text0Operation> op1, List<Text0Operation> op2) throws Json0Exception {
        checkValidOp(op1);
        checkValidOp(op2);
        List<Text0Operation> newOp = new ArrayList<>();
        for (int i = 0; i < op1.size(); i++) {
            newOp.add(op1.get(i));
        }
        for (int i = 0; i < op2.size(); i++) {
            append(newOp, op2.get(i));
        }
        return newOp;
    }

    @Override
    public List<Text0Operation> invert(List<Text0Operation> op) {
        List<Text0Operation> newList = new ArrayList<>();
        for (int i = 0; i < op.size(); i++) {
            newList.add(op.get(i));
        }
        List<Text0Operation> result = new ArrayList<>(op.size());
        for (int i = newList.size() - 1; i >= 0; i--) {
            result.add(invertComponent(newList.get(i)));
        }
        return result;
    }

    private Text0Operation invertComponent(Text0Operation c) {
        Text0Operation tmp = new Text0Operation();
        if (c.i != null) {
            tmp.d = c.i;
            tmp.p = c.p;
        }else {
            tmp.i = c.d;
            tmp.p = c.p;
        }
        return tmp;
    }

    @Override
    public void checkValidOp(List<Text0Operation> op) throws Json0Exception {
        for (int i = 0; i < op.size(); i++) {
            checkValidComponent(op.get(i));
        }
    }

    @Override
    public List<Text0Operation> transformComponent(List<Text0Operation> dest, Text0Operation c, Text0Operation otherC, String side) throws Json0Exception {
        checkValidComponent(c);
        checkValidComponent(otherC);

        if (c.i != null) {
            Text0Operation tmp = new Text0Operation();
            tmp.i = c.i;
            tmp.p = transformPosition(c.p, otherC, "right".equals(side));
            append(dest, tmp);
        }else {
            if (otherC.i != null) {
                String s = c.d;
                if (c.p < otherC.p) {
                    Text0Operation tmp = new Text0Operation();
                    tmp.d = s.substring(0, Math.min(otherC.p - c.p, s.length()));
                    tmp.p = c.p;
                    append(dest, tmp);
                    int i = otherC.p - c.p;
                    if (i >= s.length()) {
                        s = "";
                    }else {
                        s = s.substring(i);
                    }
                }
                if (s != null && !s.isEmpty()) {
                    Text0Operation tmp = new Text0Operation();
                    tmp.d = s;
                    tmp.p = c.p + otherC.i.length();
                    append(dest, tmp);
                }
            }else {
                if (c.p >= otherC.p + otherC.d.length()) {
                    Text0Operation tmp = new Text0Operation();
                    tmp.d = c.d;
                    tmp.p = c.p - otherC.d.length();
                    append(dest, tmp);
                } else if (c.p + c.d.length() <= otherC.p) {
                    append(dest, c);
                }else {
                    Text0Operation newC = new Text0Operation();
                    newC.p = c.p;
                    newC.d = "";

                    if (c.p < otherC.p) {
                        newC.d = c.d.substring(0, otherC.p - c.p);
                    }
                    if (c.p + c.d.length() > otherC.p + otherC.d.length()) {
                        newC.d += c.d.substring(otherC.p + otherC.d.length() - c.p);
                    }
                    int intersectStart = Math.max(c.p, otherC.p);
                    int intersectEnd = Math.min(c.p + c.d.length(), otherC.p + otherC.d.length());
                    int start = intersectStart - c.p;
                    int end = intersectEnd - c.p;
                    String cIntersect = "";
                    if (start < end) {
                        cIntersect = c.d.substring(start, Math.min(end, c.d.length()));
                    }
                    String otherIntersect = otherC.d.substring(intersectStart - otherC.p, intersectEnd - otherC.p);
                    if (!cIntersect.equals(otherIntersect)) {
                        throw new Json0Exception("Delete ops delete different text in the same region of the document");
                    }

                    if (!newC.d.isEmpty()) {
                        newC.p = transformPosition(newC.p, otherC, false);
                        append(dest, newC);
                    }
                }

            }
        }
        return dest;
    }


    @Override
    public JsonNode apply(JsonNode snapshot, List<Text0Operation> op) throws Json0Exception {
        if (!snapshot.isTextual()) {
            throw new Json0Exception("text0 operations cannot be applied to type: " + snapshot.getNodeType().toString());
        }
        checkValidOp(op);
        StringBuilder sb = new StringBuilder(snapshot.asText());
        for (int i = 0; i < op.size(); i++) {
            Text0Operation component = op.get(i);
            if (component.i != null) {
                sb.insert(component.p, component.i);
            } else {
                int len = component.d.length();
                String deleted = sb.substring(component.p, component.p + len);
                if (!component.d.equals(deleted)) {
                    throw new Json0Exception("Delete component '" + component.d + "' does not match deleted text '" + deleted + "'");
                }
                sb.delete(component.p, component.p + len);
            }
        }
        return new TextNode(sb.toString());
    }

    @Override
    public String getName() {
        return "text0";
    }

    private String strInject(String s1, int pos, String s2) {
        return s1.substring(0, pos) + s2 + s1.substring(pos);
    }

    private void checkValidComponent(Text0Operation c) throws Json0Exception {
        if (c.p == null) {
            throw new Json0Exception("component missing position field");
        }
        if ((c.i != null) == (c.d != null)) {
            throw new Json0Exception("component needs an i or d field");
        }
        if (c.p < 0) {
            throw new Json0Exception("position cannot be negative");
        }
    }

    private Integer transformPosition(Integer pos, Text0Operation c, boolean insertAfter) {
        if (c.i != null) {
            if (c.p < pos || (c.p.equals(pos) && insertAfter)) {
                return pos + c.i.length();
            }else {
                return pos;
            }
        }else {
            if (pos <= c.p) {
                return pos;
            } else if (pos <= c.p + c.d.length()) {
                return c.p;
            }else {
                return pos - c.d.length();
            }
        }
    }
}
