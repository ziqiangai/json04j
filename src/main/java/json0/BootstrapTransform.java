package json0;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface BootstrapTransform<T extends Operation> {

    default void transformComponentX(T left, T right, List<T> destLeft, List<T> destRight) throws Json0Exception {
        transformComponent(destLeft, left, right, "left");
        transformComponent(destRight, right, left, "left");
    }

    default List<List<T>> transformX(List<T> leftOp, List<T> rightOp) throws Json0Exception {
        checkValidOp(leftOp);
        checkValidOp(rightOp);
        List<T> newRightOp = new ArrayList<>();
        for (int i = 0; i < rightOp.size(); i++) {
            T rightComponent = rightOp.get(i);
            List<T> newLeftOp = new ArrayList<>();
            int k = 0;
            while (k < leftOp.size()) {
                List<T> nextC = new ArrayList();
                transformComponentX(leftOp.get(k), rightComponent, newLeftOp, nextC);
                k++;

                if (nextC.size() == 1) {
                    rightComponent = nextC.get(0);
                } else if (nextC.size() == 0) {
                    for (int j = k; j < leftOp.size(); j++) {
                        append(newLeftOp, leftOp.get(j));
                    }
                    rightComponent = null;
                    break;
                }else {
                    List<List<T>> pair = transformX(leftOp.subList(k, leftOp.size()), nextC);
                    for (int l = 0; l < pair.get(0).size(); l++) {
                        append(newLeftOp, pair.get(0).get(l));
                    }

                    for (int r = 0; r < pair.get(1).size(); r++) {
                        append(newRightOp, pair.get(1).get(r));
                    }
                    rightComponent = null;
                    break;
                }
            }

            if (rightComponent != null) {
                append(newRightOp, rightComponent);
            }
            leftOp = newLeftOp;
        }
        return Arrays.asList(leftOp, newRightOp);
    }

    default List<T> transform(List<T> op, List<T> otherOp, String type) throws Json0Exception {
        if (!("left".equals(type) || "right".equals(type))) {
            throw new Json0Exception("type must be 'left' or 'right'");
        }
        if (otherOp.size() == 0) {
            return op;
        }

        if (op.size() ==  1 && otherOp.size() == 1) {
            return transformComponent(new ArrayList<>(), op.get(0), otherOp.get(0), type);
        }

        if ("left".equals(type)) {
            return transformX(op, otherOp).get(0);
        }else {
            return transformX(otherOp, op).get(1);
        }

    }

    void append(List<T> dest, T c) throws Json0Exception;

    List<T> compose(List<T> op1, List<T> op2) throws Json0Exception;

    List<T> invert(List<T> op);

    void checkValidOp(List<T> op) throws Json0Exception;

    List<T> transformComponent(List<T> dest, T c, T otherC, String side) throws Json0Exception;

    JsonNode apply(JsonNode snapshot, List<T> op) throws Json0Exception;
}
