package json0;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static json0.Const.TEST_JSON_LOCATION;
import static org.junit.Assert.*;

public class Text0Test {
    private static final ObjectMapper mapper = new ObjectMapper();

    private static final TypeReference<List<Text0Operation>> text0Type = new TypeReference<List<Text0Operation>>() {
    };

    @Test
    public void testInvert(){
        File file = FileUtils.getFile(TEST_JSON_LOCATION, "text0", "invert");
        File[] files = file.listFiles();
        ObjectMapper mapper = new ObjectMapper();

        TypeReference<List<Text0Operation>> text0Type = new TypeReference<List<Text0Operation>>() {
        };
        Text0 text0 = new Text0();
        int i = 0;
        for (File tmp : files) {
            try {
                JsonNode jsonNode = mapper.readTree(tmp);
                assertEquals(jsonNode.getNodeType(), JsonNodeType.ARRAY);
                ArrayNode arr = (ArrayNode) jsonNode;
                for (JsonNode node : arr) {
                    JsonNode opOrigin = node.path("opOrigin");

                    List<Text0Operation> text0Operations = mapper.convertValue(opOrigin, text0Type);
                    List<Text0Operation> invert = text0.invert(text0Operations);
                    JsonNode opResult = node.path("op");
                    assertEquals(opResult, mapper.convertValue(invert, JsonNode.class));
                    i++;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println(tmp.getName() + " test passed");
        }
        System.out.println(i + " test passed");
    }

    @Test
    public void testCompose(){
        File file = FileUtils.getFile(TEST_JSON_LOCATION, "text0", "compose");
        File[] files = file.listFiles();
        int i = 0;
        for (File tmp : files) {
            try {
                JsonNode jsonNode = mapper.readTree(tmp);
                assertEquals(jsonNode.getNodeType(), JsonNodeType.ARRAY);
                ArrayNode arr = (ArrayNode) jsonNode;
                for (JsonNode node : arr) {
                    testComposeDetail(node);
                    i++;
                }
            } catch (IOException | Json0Exception e) {
                throw new RuntimeException(e);
            }
            System.out.println(tmp.getName() + " test passed");
        }
        System.out.println(i + " test passed");
    }

    @Test
    public void testTransformComponent(){
        File file = FileUtils.getFile(TEST_JSON_LOCATION, "text0", "transformComponent");
        File[] files = file.listFiles();
        int i = 0;
        for (File tmp : files) {
            try {
                JsonNode jsonNode = mapper.readTree(tmp);
                assertEquals(jsonNode.getNodeType(), JsonNodeType.ARRAY);
                ArrayNode arr = (ArrayNode) jsonNode;
                for (JsonNode node : arr) {
                    testTransformComponentDetail(node);
                    i++;
                }
            } catch (IOException | Json0Exception e) {
                throw new RuntimeException(e);
            }
            System.out.println(tmp.getName() + " test passed");
        }
        System.out.println(i + " test passed");
    }

    @Test
    public void testApply(){
        File file = FileUtils.getFile(TEST_JSON_LOCATION, "text0", "apply");
        File[] files = file.listFiles();
        int i = 0;
        for (File tmp : files) {
            try {
                JsonNode jsonNode = mapper.readTree(tmp);
                assertEquals(jsonNode.getNodeType(), JsonNodeType.ARRAY);
                ArrayNode arr = (ArrayNode) jsonNode;
                for (JsonNode node : arr) {
                    testApplyDetail(node);
                    i++;
                }
            } catch (IOException | Json0Exception e) {
                throw new RuntimeException(e);
            }
            System.out.println(tmp.getName() + " test passed");
        }
        System.out.println(i + " test passed");
    }

    @Test
    public void testComposeDet() throws JsonProcessingException, Json0Exception {
        JsonNode jsonNode = mapper.readTree("{\"op1\":[{\"d\":\"e\",\"p\":8}],\"op2\":[{\"d\":\"h\",\"p\":7}],\"newOp\":[{\"d\":\"he\",\"p\":7}]}");
        testComposeDetail(jsonNode);
    }

    @Test
    public void testTransformDet() throws JsonProcessingException, Json0Exception {
        JsonNode jsonNode = mapper.readTree("{\"destOrigin\":[],\"c\":{\"d\":\"oo \",\"p\":2},\"otherC\":{\"p\":4,\"i\":\"whiffling \"},\"side\":\"left\",\"dest\":[{\"d\":\"oo\",\"p\":2},{\"d\":\" \",\"p\":12}]}");
        testTransformComponentDetail(jsonNode);
    }

    private void testComposeDetail(JsonNode node) throws Json0Exception {
        Text0 text0 = new Text0();
        JsonNode op1 = node.path("op1");
        JsonNode op2 = node.path("op2");

        List<Text0Operation> op1T = mapper.convertValue(op1, text0Type);
        List<Text0Operation> op2T = mapper.convertValue(op2, text0Type);
        List<Text0Operation> composedOp = text0.compose(op1T, op2T);
        JsonNode newOp = node.path("newOp");
        assertEquals(newOp, mapper.convertValue(composedOp, JsonNode.class));
    }

    private void testTransformComponentDetail(JsonNode node) throws Json0Exception {
        Text0 text0 = new Text0();
        JsonNode destOrigin = node.path("destOrigin");
        JsonNode c = node.path("c");
        JsonNode otherC = node.path("otherC");
        JsonNode side = node.path("side");

        List<Text0Operation> destOriginT = mapper.convertValue(destOrigin, text0Type);
        Text0Operation cT = mapper.convertValue(c, Text0Operation.class);
        Text0Operation otherCT = mapper.convertValue(otherC, Text0Operation.class);
        List<Text0Operation> transformedOp = text0.transformComponent(destOriginT, cT, otherCT, side.asText());

        JsonNode dest = node.path("dest");
        assertEquals(dest, mapper.convertValue(transformedOp, JsonNode.class));
    }

    private void testApplyDetail(JsonNode node) throws Json0Exception {
        Text0 text0 = new Text0();
        JsonNode snapshotOrigin = node.path("snapshotOrigin");
        JsonNode op = node.path("op");
        JsonNode snapshotResult = node.path("snapshotResult");

        List<Text0Operation> opT = mapper.convertValue(op, text0Type);
        JsonNode r = text0.apply(snapshotOrigin, opT);

        assertEquals(snapshotResult, r);
    }

}