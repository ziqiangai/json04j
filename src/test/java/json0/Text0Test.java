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

import static org.junit.Assert.*;

public class Text0Test {
    private static final ObjectMapper mapper = new ObjectMapper();

    private static final TypeReference<List<Text0Operation>> text0Type = new TypeReference<List<Text0Operation>>() {
    };

    @Test
    public void append() throws JsonProcessingException {
        System.out.println("hello WOrld");
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode a = mapper.createObjectNode();
        a.put("a",
                        mapper.createObjectNode()
                                .put("a", 12)
                                .put("b", 15)
                );
        ObjectNode b = mapper.createObjectNode();
        b.put("a",
                        mapper.createObjectNode()
                                .put("b", 15)
                                .put("a", 12)
                );
        ObjectNode c = mapper.createObjectNode();
        c.put("a",
                        mapper.createObjectNode()
                                .put("b", 15)
                                .put("a", 28)
                );
        assertEquals(a, b);
        assertNotEquals(a, c);
        JsonNode p = mapper.readTree("[{\"i\":\"T\",\"p\":1749},{\"d\":\"slithy \",\"p\":13291}]");
        JsonNode q = mapper.readTree("[{\"p\":1749,\"i\":\"T\"},{\"p\":13291,\"d\":\"slithy \"}]");

        assertEquals(p, q);
    }

    @Test
    public void testInvert(){
        String folder = "E:\\code\\json04j\\src\\test\\resources\\text0\\invert";
        File file = FileUtils.getFile(folder);
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
        String folder = "E:\\code\\json04j\\src\\test\\resources\\text0\\compose";
        File file = FileUtils.getFile(folder);
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
        String folder = "E:\\code\\json04j\\src\\test\\resources\\text0\\transformComponent";
        File file = FileUtils.getFile(folder);
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
        String folder = "E:\\code\\json04j\\src\\test\\resources\\text0\\apply";
        File file = FileUtils.getFile(folder);
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