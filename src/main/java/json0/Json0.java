package json0;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;

public class Json0 implements BootstrapTransform<Json0Operation> {

    private Map<String, BootstrapTransform> subtypes;
    private static final ObjectMapper JSON0_MAPPER = new ObjectMapper();

    public Json0() {
        this.subtypes = new HashMap<>();
        this.subtypes.put("text0", new Text0());
    }

    @Override
    public void append(List<Json0Operation> dest, Json0Operation c) throws Json0Exception {
        c = c.clone();
        if (dest.isEmpty()) {
            dest.add(c);
            return;
        }

        Json0Operation last = dest.get(dest.size() - 1);

        if (
                (c.si != null || c.sd != null) && (last.si != null || last.sd != null)
        ) {
            convertFromText(c);
            convertFromText(last);
        }

        if (pathMatches(c.p, last.p, false)) {
            if (c.t != null && last.t != null && c.t.equals(last.t) && this.subtypes.containsKey(c.t)) {
                BootstrapTransform subBootstrapTransform = subtypes.get(c.t);
                last.o = subBootstrapTransform.compose(last.o, c.o);

                if (c.si != null || c.sd != null) {
                    List<Object> p = c.p;

                    for (int i = 0; i < last.o.size() - 1; i++) {
                        Text0Operation lastOp = last.o.remove(last.o.size() - 1);
                        List<Text0Operation> cos = new ArrayList<>();
                        cos.add(lastOp);
                        c.o = cos;
                        List<Object> tmp = new ArrayList<>();
                        for (int j = 0; j < p.size(); j++) {
                            tmp.add(p.get(j));
                        }
                        c.p = tmp;
                        convertToText(c);
                        dest.add(c);
                    }

                    convertToText(last);
                }
            }else if (last.na != null && c.na != null) {
                Json0Operation lastOp = dest.get(dest.size() - 1);
                lastOp.p = last.p;
                JsonNode na = last.na;
                if (na.isInt() && c.na.isInt()) {
                    lastOp.na = IntNode.valueOf(na.asInt() + c.na.asInt());
                }else {
                    lastOp.na = DoubleNode.valueOf(na.asDouble() + c.na.asDouble());
                }
            } else if (last.li != null && c.li == null && this.isEq(c.ld, last.li)) {
                if (last.ld != null) {
                    last.li = null;
                }else {
                    dest.remove(dest.size() - 1);
                }
            } else if (last.od != null && last.oi == null && c.oi != null && c.od == null) {
                last.oi = c.oi;
            } else if (last.oi != null && c.od != null) {
                if (c.oi != null) {
                    last.oi = c.oi;
                } else if (last.od != null) {
                    last.oi = null;
                }else {
                    dest.remove(dest.size() - 1);
                }
            } else if (c.lm != null && c.lm.equals(c.p.get(c.p.size() - 1))) {
                // don't do anything
            }else {
                dest.add(c);
            }
        }else {
            if ((c.si != null || c.sd != null) && (last.si != null || last.sd != null)) {
                convertToText(c);
                convertToText(last);
            }

            dest.add(c);
        }
    }


    @Override
    public List<Json0Operation> compose(List<Json0Operation> op1, List<Json0Operation> op2) throws Json0Exception {
        checkValidOp(op1);
        checkValidOp(op2);

        List<Json0Operation> newOp = new ArrayList<>(op1.size());
        for (Json0Operation operation : op1) {
            newOp.add(operation.clone());
        }

        for (Json0Operation operation : op2) {
            this.append(newOp, operation);
        }
        return newOp;
    }

    @Override
    public List<Json0Operation> invert(List<Json0Operation> c) {
        List<Json0Operation> iop = new ArrayList<>();
        List<Json0Operation> op_ = new ArrayList<>();
        for (int i = 0; i < c.size(); i++) {
            op_.add(c.get(i));
        }
        for (int i = op_.size() - 1; i >= 0; i--) {
            iop.add(this.invertComponent(op_.get(i)));
        }
        return iop;
    }

    @Override
    public void checkValidOp(List<Json0Operation> op) throws Json0Exception {
        for (int i = 0; i < op.size(); i++) {
            if (!this.isArray(op.get(i).p)) {
                throw new Json0Exception("Missing path");
            }
        }
    }

    @Override
    public List<Json0Operation> transformComponent(List<Json0Operation> dest, Json0Operation c, Json0Operation otherC, String type) throws Json0Exception {
        c = c.clone();

        Integer common = commonLengthForOps(otherC, c);
        Integer common2 = commonLengthForOps(c, otherC);
        int cplength = c.p.size();
        int otherCplength = otherC.p.size();

        if (c.na != null || c.t != null) {
            cplength++;
        }

        if (otherC.na != null || otherC.t != null) {
            otherCplength++;
        }

        if (common2 != null && otherCplength > cplength &&
                Objects.equals(
                        listGetDefaultNull(c.p, common2),
                        listGetDefaultNull(otherC.p, common2)
                )
        ) {
            if (c.ld != null) {
                Json0Operation oc = otherC.clone();
                List<Object> tmp = new ArrayList<>();
                for (int i = cplength; i < oc.p.size(); i++) {
                    tmp.add(oc.p.get(i));
                }
                oc.p = tmp;
                ArrayList<Json0Operation> ops = new ArrayList<>();
                ops.add(oc);
                c.ld = this.apply(c.ld.deepCopy(), ops);
            } else if (c.od != null) {
                Json0Operation oc = otherC.clone();
                List<Object> tmp = new ArrayList<>();
                for (int i = cplength; i < oc.p.size(); i++) {
                    tmp.add(oc.p.get(i));
                }
                oc.p = tmp;
                ArrayList<Json0Operation> ops = new ArrayList<>();
                ops.add(oc);
                c.od = this.apply(c.od.deepCopy(), ops);
            }
        }

        if (common != null) {
            boolean commonOperand = cplength == otherCplength;

            Json0Operation oc = otherC;
            if ((c.si != null || c.sd != null) && (otherC.si != null || otherC.sd != null)) {
                convertFromText(c);
                oc = otherC.clone();
                convertFromText(oc);
            }

            if (oc.t != null && this.subtypes.containsKey(oc.t)) {
                if (c.t != null && c.t.equals(oc.t)) {
                    BootstrapTransform bootstrapTransform = this.subtypes.get(c.t);
                    List<Text0Operation> res = bootstrapTransform.transform(c.o, oc.o, type);

                    if (c.si != null || c.sd != null) {
                        List<Object> p = c.p;
                        for (int i = 0; i < res.size(); i++) {
                            c.o = new ArrayList<>();
                            c.o.add(res.get(i));
                            c.p = new ArrayList<>();
                            for (int k = 0; k < p.size(); k++) {
                                c.p.add(p.get(k));
                            }
                            convertToText(c);
                            this.append(dest, c);
                        }
                    } else if (res.size() > 0) {
                        c.o = res;
                        this.append(dest, c);
                    }

                    return dest;
                }
            } else if (otherC.na != null) {
                // this case is handled below
            } else if (otherC.li != null && otherC.ld != null) {
                Object o1 = otherC.p.get(common);
                Object o2 = c.p.get(common);
                if (Objects.equals(o1, o2)) {
                    if (!commonOperand) {
                        return dest;
                    } else if (c.ld != null) {
                        if (c.li != null && "left".equals(type)) {
                            c.ld = otherC.li.deepCopy();
                        } else {
                            return dest;
                        }
                    }
                }
            } else if (otherC.li != null) {
                if (c.li != null && c.ld == null && commonOperand && Objects.equals(c.p.get(common), otherC.p.get(common))) {
                    if ("right".equals(type)) {
                        Integer o = (Integer) c.p.get(common);
                        c.p.set(common, o + 1);
                    }
                } else if ((Integer) otherC.p.get(common) <= (Integer) c.p.get(common)) {
                    Integer o = (Integer) c.p.get(common);
                    c.p.set(common, o + 1);
                }

                if (c.lm != null) {
                    if (commonOperand) {
                        // otherC edits the same list we edit
                        if ((Integer) otherC.p.get(common) <= (Integer) c.lm)
                            c.lm = (Integer) c.lm + 1;
                        // changing c.from is handled above.
                    }
                }
            } else if (otherC.ld != null) {
                if (c.lm != null) {
                    if (commonOperand) {
                        if (otherC.p.get(common) == c.p.get(common)) {
                            return dest;
                        }
                        Integer p = (Integer) otherC.p.get(common);
                        Integer from = (Integer) c.p.get(common);
                        Integer to = (Integer) c.lm;
                        if (p < to || (p == to && from < to)) {
                            c.lm = to - 1;
                        }
                    }
                }

                if ((Integer) otherC.p.get(common) < (Integer) c.p.get(common)) {
                    c.p.set(common, ((Integer)c.p.get(common)) - 1);
                } else if ((Integer) otherC.p.get(common) == (Integer) c.p.get(common)) {
                    if (otherCplength < cplength) {
                        return dest;
                    } else if (c.ld != null) {
                        if (c.li != null) {
                            c.ld = null;
                        } else {
                            return dest;
                        }
                    }
                }
            } else if (otherC.lm != null) {
                if (c.lm != null && cplength == otherCplength) {
                    // lm vs lm, here we go!
                    Integer from = (Integer) c.p.get(common);
                    Integer to = (Integer) c.lm;
                    Integer otherFrom = (Integer) otherC.p.get(common);
                    Integer otherTo = (Integer) otherC.lm;
                    if (otherFrom != otherTo) {
                        // if otherFrom == otherTo, we don't need to change our op.

                        // where did my thing go?
                        if (from == otherFrom) {
                            // they moved it! tie break.
                            if ("left".equals(type)) {
                                c.p.set(common, otherTo);
                                if (from == to) // ugh
                                    c.lm = otherTo;
                            } else {
                                return dest;
                            }
                        } else {
                            // they moved around it
                            if (from > otherFrom) c.p.set(common, ((Integer) c.p.get(common)) - 1);
                            if (from > otherTo) c.p.set(common, ((Integer) c.p.get(common)) + 1);
                            else if (from.equals(otherTo)) {
                                if (otherFrom > otherTo) {
                                    c.p.set(common, ((Integer) c.p.get(common)) + 1);
                                    if (from.equals(to)) // ugh, again
                                        c.lm = ((Integer) c.lm) + 1;
                                }
                            }

                            // step 2: where am i going to put it?
                            if (to > otherFrom) {
                                c.lm = ((Integer) c.lm) - 1;
                            } else if (to == otherFrom) {
                                if (to > from)
                                    c.lm = ((Integer) c.lm) - 1;
                            }
                            if (to > otherTo) {
                                c.lm = ((Integer) c.lm) + 1;
                            } else if (to == otherTo) {
                                // if we're both moving in the same direction, tie break
                                if ((otherTo > otherFrom && to > from) ||
                                        (otherTo < otherFrom && to < from)) {
                                    if ("right".equals(type)) {
                                        c.lm = ((Integer) c.lm) + 1;
                                    }
                                } else {
                                    if (to > from) c.lm = ((Integer) c.lm) + 1;
                                    else if (to == otherFrom) c.lm = ((Integer) c.lm) - 1;
                                }
                            }
                        }
                    }
                } else if (c.li != null && c.ld == null && commonOperand) {
                    // li
                    Integer from = (Integer) otherC.p.get(common);
                    Integer to = (Integer) otherC.lm;
                    Integer p = (Integer) c.p.get(common);
                    if (p > from) {
                        c.p.set(common, p - 1);
                    }
                    if (p > to) {
                        Integer newP = (Integer) c.p.get(common);
                        c.p.set(common, newP + 1);
                    };
                } else {
                    // ld, ld+li, si, sd, na, oi, od, oi+od, any li on an element beneath
                    // the lm
                    //
                    // i.e. things care about where their item is after the move.
                    Integer from = (Integer) otherC.p.get(common);
                    Integer to = (Integer) otherC.lm;
                    Integer p = (Integer) c.p.get(common);
                    if (p == from) {
                        c.p.set(common, to);
                    } else {
                        if (p > from) {
                            c.p.set(common, p - 1);
                        };
                        if (p > to) {
                            Integer newP = (Integer) c.p.get(common);
                            c.p.set(common,  newP + 1);
                        } else if (p.equals(to) && from > to) {
                            c.p.set(common, p + 1);
                        };
                    }
                }
            } else if (otherC.oi != null && otherC.od != null) {
                if (Objects.equals(
                        listGetDefaultNull(c.p, common),
                        listGetDefaultNull(otherC.p, common)
                )) {
                    if (c.oi != null && commonOperand) {
                        // we inserted where someone else replaced
                        if ("right".equals(type)) {
                            // left wins
                            return dest;
                        } else {
                            // we win, make our op replace what they inserted
                            c.od = otherC.oi;
                        }
                    } else {
                        // -> noop if the other component is deleting the same object (or any parent)
                        return dest;
                    }
                }
            } else if (otherC.oi != null) {
                if (c.oi != null && (Objects.equals(
                        listGetDefaultNull(c.p, common),
                        listGetDefaultNull(otherC.p, common)
                ))) {

                    // left wins if we try to insert at the same place
                    if ("left".equals(type)) {
                        Json0Operation tmp = new Json0Operation();
                        tmp.p = c.p;
                        tmp.od = otherC.oi;
                        this.append(dest, tmp);
                    } else {
                        return dest;
                    }
                }
            } else if (otherC.od != null) {
                if (Objects.equals(
                        listGetDefaultNull(c.p, common),
                        listGetDefaultNull(otherC.p, common)
                )) {
                    if (!commonOperand)
                        return dest;
                    if (c.oi != null) {
                        c.od = null;
                    } else {
                        return dest;
                    }
                }
            }
        }

        this.append(dest, c);
        return dest;
    }

    @Override
    public JsonNode apply(JsonNode snapshot, List<Json0Operation> op) throws Json0Exception {
        checkValidOp(op);

        List<Json0Operation> newOps = new ArrayList<>(op.size());

        for (Json0Operation operation : op) {
            newOps.add(operation.clone());
        }
        ObjectNode container = JSON0_MAPPER.createObjectNode();
        container.set("data", snapshot);

        op = newOps;
        for (int i = 0; i < op.size(); i++) {
            Json0Operation c = op.get(i);
            if (c.si != null || c.sd != null) {
                convertFromText(c);
            }

            JsonNode parent = null;
            Object parentKey = null;
            JsonNode elem = container;
            Object key = "data";

            for (int j = 0; j < c.p.size(); j++) {
                Object p = c.p.get(j);
                parent = elem;
                parentKey = key;
                if (key instanceof String) {
                    elem = elem.path((String) key);
                }else {
                    elem = elem.path((Integer) key);
                }
                key = p;

                if (elem.isArray() && !(key instanceof Integer)) {
                    throw new Json0Exception("List index must be a number");
                }

                if (elem.isObject() && !(key instanceof String)) {
                    throw new Json0Exception("Object key must be a string");
                }

                if (parent.isMissingNode() || parent.isNull()) {
                    throw new Json0Exception("Path invalid");
                }
            }

            if (c.t != null && c.o != null && this.subtypes.containsKey(c.t)) {
                if (key instanceof String) {
                    String key_s = (String) key;
                    JsonNode applied = this.subtypes.get(c.t).apply(elem.path(key_s), c.o);
                    ((ObjectNode) elem).set(key_s, applied);
                } else if (key instanceof Integer) {
                    Integer key_int = (Integer) key;
                    JsonNode applied = this.subtypes.get(c.t).apply(elem.path(key_int), c.o);
                    ((ArrayNode) elem).set(key_int, applied);
                }
            }
            else if (c.na != null) {
                this.handleNaApply(key, elem, c);
            }
            else if (c.li != null && c.ld != null)
            {
                this.checkList(elem);
                if (key instanceof Integer) {
                    ((ArrayNode) elem).set((Integer) key, c.li);
                } else if (key instanceof String) {
                    ((ObjectNode) elem).set((String) key, c.li);
                }
            }
            else if (c.li != null)
            {
                this.checkList(elem);
                splice((ArrayNode) elem, (Integer) key, 0, c.li);
            }
            else if (c.ld != null)
            {
                this.checkList(elem);
                splice((ArrayNode) elem, (Integer) key, 1);
            }
            else if (c.lm != null)
            {
                if (!(c.lm instanceof Integer)) {
                    throw new Json0Exception("List move target index must be a number");
                }
                this.checkList(elem);
                if (c.lm != key) {
                    JsonNode e = null;
                    if (key instanceof String) {
                        e = elem.path((String) key);
                    }else{
                        e = elem.path((Integer) key);
                    }
                    splice((ArrayNode) elem, (Integer) key, 1);
                    splice((ArrayNode) elem, (Integer) c.lm, 0, e);
                }
            }
            else if (c.oi != null)
            {
                this.checkObj(elem);
                ((ObjectNode) elem).set((String) key, c.oi);
            }
            else if (c.od != null)
            {
                this.checkObj(elem);
                ((ObjectNode) elem).remove((String) key);
            }
            else
            {
                throw new Json0Exception("invalid / missing instruction in op");
            }
        }
        return container.path("data");
    }

    @Override
    public String getName() {
        return "json0";
    }

    public static void splice(ArrayNode array, int startIndex, int deleteCount, JsonNode... elements) {
        for (int i = 0; i < deleteCount; i++) {
            array.remove(startIndex);
        }

        if (elements != null) {
            for (JsonNode element : elements) {
                array.insert(startIndex++, element);
            }
        }
    }

    private void checkList(JsonNode elem) throws Json0Exception {
        if (!elem.isArray()) {
            throw new Json0Exception("Referenced element not a list");
        }
    }

    private void checkObj(JsonNode elem) throws Json0Exception {
        if (!elem.isObject()) {
            throw new Json0Exception("Referenced element not an object (it was " + elem.toString() + ")");
        }
    }

    private void handleNaApply(Object key, JsonNode elem, Json0Operation c) throws Json0Exception {
        if (!c.na.isNumber()) {
            throw new Json0Exception("Number addition is not a number");
        }
        JsonNode el = null;
        if (key instanceof String) {
            String key_s = (String) key;
            el = elem.path(key_s);
        } else if (key instanceof Integer) {
            Integer key_int = (Integer) key;
            el = elem.path(key_int);
        }
        if (!el.isNumber()) {
            throw new Json0Exception("Referenced element not a number");
        }
        JsonNode newVal = null;
        if (el.isInt() && c.na.isInt()) {
            newVal = IntNode.valueOf(el.asInt() + c.na.asInt());
        }else {
            newVal = DoubleNode.valueOf(el.asDouble() + c.na.asDouble());
        }
        if (key instanceof String) {
            String key_s = (String) key;
            ((ObjectNode) elem).set(key_s, newVal);
        } else if (key instanceof Integer) {
            Integer key_int = (Integer) key;
            ((ArrayNode) elem).set(key_int, newVal);
        }

    }



    private void convertFromText(Json0Operation c) {
        c.t = "text0";
        Text0Operation o = new Text0Operation();
        Object lastP = (c.p == null || c.p.isEmpty()) ? null : c.p.remove(c.p.size() - 1);
        if (lastP instanceof Integer) {
            o.p = (Integer) lastP;
        }
        if (c.si != null) {
            o.i = c.si;
        }
        if (c.sd != null) {
            o.d = c.sd;
        }
        ArrayList<Text0Operation> subOps = new ArrayList<>();
        subOps.add(o);
        c.o = subOps;
    }


    private void convertToText(Json0Operation c) {
        Text0Operation operation = c.o.get(0);
        c.p.add(operation.p);
        if (operation.i != null) {
            c.si = operation.i;
        }
        if (operation.d != null) {
            c.sd = operation.d;
        }
        c.o = null;
        c.t = null;
    }


    private boolean pathMatches(List<Object> p1, List<Object> p2, boolean ignoreLast) {
        if (p1.size() != p2.size()) {
            return false;
        }

        for (int i = 0; i < p1.size(); i++) {
            if (!p1.get(i).equals(p2.get(i)) && (!ignoreLast || i != p1.size() - 1)) {
                return false;
            }
        }

        return true;
    }

    private Json0Operation invertComponent(Json0Operation c) {
        Json0Operation c_ = new Json0Operation();
        c_.p = c.p;

        if (c.t != null && this.subtypes.containsKey(c.t)) {
            c_.t = c.t;
            c_.o = this.subtypes.get(c.t).invert(c.o);
        }
        if (c.si != null) {
            c_.sd = c.si;
        }
        if (c.sd != null) {
            c_.si = c.sd;
        }
        if (c.oi != null) {
            c_.od = c.oi;
        }
        if (c.od != null) {
            c_.oi = c.od;
        }
        if (c.li != null) {
            c_.ld = c.li;
        }
        if (c.ld != null) {
            c_.li = c.ld;
        }
        if (c.na != null) {
            if (c.na.isInt()) {
                c_.na = IntNode.valueOf(-c.na.asInt());
            }else {
                c_.na = DoubleNode.valueOf(0 - c.na.asDouble());
            }
        }
        if (c.lm != null) {
            c_.lm = c.p.get(c.p.size() - 1);
            List<Object> objects = new ArrayList<>();
            for (int i = 0; i < c.p.size() - 1; i++) {
                objects.add(c.p.get(i));
            }

            objects.add(c.lm);
            c_.p = objects;
        }

        return c_;
    }


    private boolean isEq(JsonNode ld, JsonNode li) {
        if (ld == li){
            return true;
        } else if (ld == null || li == null){
            return false;
        }else if (ld.isNull() && li.isNull()) {
            return true;
        } else if (ld.isTextual() && li.isTextual()) {
            return ld.asText().equals(li.asText());
        } else if (!ld.isNumber() || !li.isNumber()) {
            return false;
        } else if (ld.isInt() && li.isInt()) {
            return ld.asInt() == li.asInt();
        } else {
            return ld.asDouble() == li.asDouble();
        }
    }

    private boolean isArray(List<Object> p) {
        return p != null;
    }

    private Object listGetDefaultNull(List l, Integer index) {
        if (l == null) {
            return null;
        }
        if (index == null || index < 0 || index >= l.size()) {
            return null;
        }
        return l.get(index);
    }

    private Integer commonLengthForOps(Json0Operation a, Json0Operation b) {
        int alen = a.p.size();
        int blen = b.p.size();
        if (a.na != null || a.t != null) {
            alen++;
        }

        if (b.na != null || b.t != null) {
            blen++;
        }

        if (alen == 0) {
            return -1;
        }
        if (blen == 0) {
            return null;
        }

        alen--;
        blen--;

        for (int i = 0; i < alen; i++) {
            Object p = a.p.get(i);
            if (i >= blen || !Objects.equals(p, b.p.get(i))) {
                return null;
            }
        }
        return alen;
    }
}
