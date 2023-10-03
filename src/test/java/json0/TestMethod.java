package json0;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.ziqiangai.json0.*;

import java.util.Arrays;
import java.util.List;

import static json0.Json0Test.*;


public interface TestMethod<T extends BootstrapTransform> {

    List<Object> parserArgs(JsonNode node);

    void execute(T executor, List<Object> args) throws Json0Exception;

    default T getBoot(){
        return (T) new Json0();
    }


    static TestMethod getInstance(String module, String name, String method) {
        String s = module + name + method;
        if (s.equals("json0" + "json0" + "invert")) {
            return new TestMethod<Json0>() {
                @Override
                public List<Object> parserArgs(JsonNode node) {
                    JsonNode opOrigin = node.path("op");
                    List<Json0Operation> json0Operations = mapper.convertValue(opOrigin, json0Type);
                    return Arrays.asList(json0Operations);
                }

                @Override
                public void execute(Json0 executor, List<Object> args) {
                    executor.invert((List<Json0Operation>) args.get(0));
                }
            };
        }
        if (s.equals("json0" + "json0" + "compose")){
            return new TestMethod<Json0>() {
                @Override
                public List<Object> parserArgs(JsonNode node) {
                    JsonNode op1 = node.path("op1");
                    JsonNode op2 = node.path("op2");

                    List<Json0Operation> op1T = mapper.convertValue(op1, json0Type);
                    List<Json0Operation> op2T = mapper.convertValue(op2, json0Type);
                    return Arrays.asList(op1T, op2T);
                }

                @Override
                public void execute(Json0 executor, List<Object> args) throws Json0Exception {
                    executor.compose((List<Json0Operation>) args.get(0), (List<Json0Operation>) args.get(1));
                }
            };
        }
        if(s.equals("json0" + "json0" + "apply")){
            return new TestMethod<Json0>() {
                @Override
                public List<Object> parserArgs(JsonNode node) {
                    JsonNode snapshotOrigin = node.path("snapshot");
                    JsonNode op = node.path("op");

                    List<Json0Operation> opT = mapper.convertValue(op, json0Type);
                    return Arrays.asList(snapshotOrigin, opT);
                }

                @Override
                public void execute(Json0 executor, List<Object> args) throws Json0Exception {
                    executor.apply((JsonNode) args.get(0), (List<Json0Operation>) args.get(1));
                }
            };
        }
        if(s.equals("json0" + "json0" + "transformComponent")){
            return new TestMethod<Json0>() {
                @Override
                public List<Object> parserArgs(JsonNode node) {
                    JsonNode destOrigin = node.path("destOrigin");
                    JsonNode c = node.path("c");
                    JsonNode otherC = node.path("otherC");
                    JsonNode type = node.path("type");

                    List<Json0Operation> destOriginT = mapper.convertValue(destOrigin, json0Type);
                    Json0Operation cT = mapper.convertValue(c, Json0Operation.class);
                    Json0Operation otherCT = mapper.convertValue(otherC, Json0Operation.class);
                    return Arrays.asList(destOriginT, cT, otherCT, type.asText());
                }

                @Override
                public void execute(Json0 executor, List<Object> args) throws Json0Exception {
                    executor.transformComponent((List<Json0Operation>) args.get(0), (Json0Operation) args.get(1), (Json0Operation) args.get(2), (String) args.get(3));
                }
            };
        }
        if(s.equals("json0" + "json0" + "transformX")){
            return new TestMethod<Json0>() {
                @Override
                public List<Object> parserArgs(JsonNode node) {
                    JsonNode leftOp = node.path("leftOp");
                    JsonNode rightOp = node.path("rightOp");

                    if (leftOp.path(0).path("p").isNumber() || rightOp.path(0).path("p").isNumber()) {
                        return null;
                    } else {
                        List<Json0Operation> leftOpT = mapper.convertValue(leftOp, json0Type);
                        List<Json0Operation> rightOpT = mapper.convertValue(rightOp, json0Type);
                        return Arrays.asList(leftOpT, rightOpT);
                    }

                }

                @Override
                public void execute(Json0 executor, List<Object> args) throws Json0Exception {
                    executor.transformX((List<Json0Operation>) args.get(0), (List<Json0Operation>) args.get(1));
                }
            };
        }
        if(s.equals("json0" + "text0" + "transformX")){
            return new TestMethod<Text0>() {
                @Override
                public List<Object> parserArgs(JsonNode node) {
                    JsonNode leftOp = node.path("leftOp");
                    JsonNode rightOp = node.path("rightOp");

                    if (leftOp.path(0).path("p").isNumber() || rightOp.path(0).path("p").isNumber()) {
                        List<Text0Operation> leftOpT = mapper.convertValue(leftOp, text0Type);
                        List<Text0Operation> rightOpT = mapper.convertValue(rightOp, text0Type);
                        return Arrays.asList(leftOpT, rightOpT);
                    }else {
                        return null;
                    }

                }

                @Override
                public void execute(Text0 executor, List<Object> args) throws Json0Exception {
                    executor.transformX((List<Text0Operation>) args.get(0), (List<Text0Operation>) args.get(1));
                }

                @Override
                public Text0 getBoot() {
                    return new Text0();
                }
            };
        }
        if (s.equals("text0" + "text0" + "invert")){
            return new TestMethod<Text0>() {
                @Override
                public List<Object> parserArgs(JsonNode node) {
                    JsonNode opOrigin = node.path("opOrigin");
                    List<Text0Operation> text0Operations = mapper.convertValue(opOrigin, text0Type);
                    return Arrays.asList(text0Operations);
                }

                @Override
                public void execute(Text0 executor, List<Object> args) throws Json0Exception {
                    executor.invert((List<Text0Operation>) args.get(0));
                }

                @Override
                public Text0 getBoot() {
                    return new Text0();
                }
            };
        }
        if (s.equals("text0" + "text0" + "compose")){
            return new TestMethod<Text0>() {
                @Override
                public List<Object> parserArgs(JsonNode node) {
                    JsonNode op1 = node.path("op1");
                    JsonNode op2 = node.path("op2");

                    List<Text0Operation> op1T = mapper.convertValue(op1, text0Type);
                    List<Text0Operation> op2T = mapper.convertValue(op2, text0Type);
                    return Arrays.asList(op1T, op2T);
                }

                @Override
                public void execute(Text0 executor, List<Object> args) throws Json0Exception {
                    executor.compose((List<Text0Operation>) args.get(0), (List<Text0Operation>) args.get(1));
                }

                @Override
                public Text0 getBoot() {
                    return new Text0();
                }
            };
        }
        if (s.equals("text0" + "text0" + "transformComponent")){
            return new TestMethod<Text0>() {
                @Override
                public List<Object> parserArgs(JsonNode node) {
                    JsonNode destOrigin = node.path("destOrigin");
                    JsonNode c = node.path("c");
                    JsonNode otherC = node.path("otherC");
                    JsonNode side = node.path("side");

                    List<Text0Operation> destOriginT = mapper.convertValue(destOrigin, text0Type);
                    Text0Operation cT = mapper.convertValue(c, Text0Operation.class);
                    Text0Operation otherCT = mapper.convertValue(otherC, Text0Operation.class);
                    return Arrays.asList(destOriginT, cT, otherCT, side.asText());
                }

                @Override
                public void execute(Text0 executor, List<Object> args) throws Json0Exception {
                    executor.transformComponent((List<Text0Operation>) args.get(0), (Text0Operation) args.get(1), (Text0Operation) args.get(2), (String) args.get(3));
                }

                @Override
                public Text0 getBoot() {
                    return new Text0();
                }
            };
        }
        if (s.equals("text0" + "text0" + "apply")){
            return new TestMethod<Text0>() {
                @Override
                public List<Object> parserArgs(JsonNode node) {
                    JsonNode snapshotOrigin = node.path("snapshotOrigin");
                    JsonNode op = node.path("op");

                    List<Text0Operation> opT = mapper.convertValue(op, text0Type);
                    return Arrays.asList(snapshotOrigin, opT);
                }

                @Override
                public void execute(Text0 executor, List<Object> args) throws Json0Exception {
                    executor.apply((JsonNode) args.get(0), (List<Text0Operation>) args.get(1));
                }

                @Override
                public Text0 getBoot() {
                    return new Text0();
                }
            };
        }
        return null;
    }

}
