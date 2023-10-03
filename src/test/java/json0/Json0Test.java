package json0;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.github.ziqiangai.json0.*;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static json0.Const.TEST_JSON_LOCATION;
import static org.junit.Assert.*;

public class Json0Test {
    public static final ObjectMapper mapper = new ObjectMapper();

    public static final TypeReference<List<Json0Operation>> json0Type = new TypeReference<List<Json0Operation>>() {
    };

    public static final TypeReference<List<Text0Operation>> text0Type = new TypeReference<List<Text0Operation>>() {
    };


    @Test
    public void benchmark() {
        List<String[]> data = new ArrayList<>();
        doBenchmark(data, "json0", "invert");
        doBenchmark(data, "json0", "compose");
        doBenchmark(data, "json0", "apply");
        doBenchmark(data, "json0", "transformComponent");
        doBenchmark(data, "json0", "transformX");
        doBenchmark(data, "json0", "text0", "transformX");
        doBenchmark(data, "text0", "invert");
        doBenchmark(data, "text0", "compose");
        doBenchmark(data, "text0", "transformComponent");
        doBenchmark(data, "text0", "apply");

        PrintTable.printTable(data);
    }

    public void doBenchmark(List<String[]> data, String module, String name, String method) {
        TestMethod ex = TestMethod.getInstance(module, name, method);
        File file = FileUtils.getFile(TEST_JSON_LOCATION, module, method);
        File[] files = file.listFiles();
        ObjectMapper mapper = new ObjectMapper();

        BootstrapTransform json0 = ex.getBoot();
        int testCount = 0;
        long take = 0;
        for (File tmp : files) {
            try {
                JsonNode jsonNode = mapper.readTree(tmp);
                assertEquals(jsonNode.getNodeType(), JsonNodeType.ARRAY);
                ArrayNode arr = (ArrayNode) jsonNode;
                for (JsonNode node : arr) {
                    List<Object> args = ex.parserArgs(node);
                    if (args != null) {
                        long now = System.currentTimeMillis();
                        ex.execute(json0, args);
                        take += (System.currentTimeMillis() - now);
                        testCount++;
                    }
                }
            } catch (IOException | Json0Exception e) {
                throw new RuntimeException(e);
            }
        }
        data.add(new String[]{name, method, testCount + "", take + " ms"});
    }

    public void doBenchmark(List<String[]> data, String module, String method) {
        doBenchmark(data, module, module, method);
    }

    @Test
    public void testInvert() {
        File file = FileUtils.getFile(TEST_JSON_LOCATION, "json0", "invert");
        File[] files = file.listFiles();
        ObjectMapper mapper = new ObjectMapper();

        Json0 json0 = new Json0();
        int i = 0;
        for (File tmp : files) {
            try {
                JsonNode jsonNode = mapper.readTree(tmp);
                assertEquals(jsonNode.getNodeType(), JsonNodeType.ARRAY);
                ArrayNode arr = (ArrayNode) jsonNode;
                for (JsonNode node : arr) {
                    JsonNode opOrigin = node.path("op");

                    List<Json0Operation> json0Operations = mapper.convertValue(opOrigin, json0Type);
                    List<Json0Operation> invert = json0.invert(json0Operations);
                    JsonNode opResult = node.path("iop");
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
        File file = FileUtils.getFile(TEST_JSON_LOCATION, "json0", "compose");
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
    public void testApply(){
        File file = FileUtils.getFile(TEST_JSON_LOCATION, "json0", "apply");
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
    public void testTransformComponent(){
        File file = FileUtils.getFile(TEST_JSON_LOCATION, "json0", "transformComponent");
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
    public void testTransformX(){
        File file = FileUtils.getFile(TEST_JSON_LOCATION, "json0", "transformX");
        File[] files = file.listFiles();
        int i = 0;
        for (File tmp : files) {
            try {
                JsonNode jsonNode = mapper.readTree(tmp);
                assertEquals(jsonNode.getNodeType(), JsonNodeType.ARRAY);
                ArrayNode arr = (ArrayNode) jsonNode;
                for (JsonNode node : arr) {
                    testTransformXDetail(node);
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
    public void testTransformXDect() throws JsonProcessingException, Json0Exception {
        String jsonStr = "{\"leftOp\":[{\"p\":[],\"t\":\"text0\",\"o\":[{\"d\":\"lli\",\"p\":6},{\"d\":\" \",\"p\":14},{\"d\":\"b\",\"p\":5}]},{\"p\":[],\"od\":\"thandthrough \",\"oi\":\"\"},{\"p\":[],\"t\":\"text0\",\"o\":[{\"p\":0,\"i\":\"tulgey \"}]}],\"rightOp\":[{\"p\":[],\"t\":\"text0\",\"o\":[{\"d\":\" \",\"p\":17}]}],\"r0\":[{\"p\":[],\"t\":\"text0\",\"o\":[{\"d\":\"blli\",\"p\":5}]},{\"p\":[],\"od\":\"thandthrough \",\"oi\":\"\"},{\"p\":[],\"t\":\"text0\",\"o\":[{\"p\":0,\"i\":\"tulgey \"}]}],\"r1\":[]}";
        JsonNode jsonNode = mapper.readTree(jsonStr);
        testTransformXDetail(jsonNode);
    }

    private void testTransformXDetail(JsonNode node) throws Json0Exception {
        JsonNode leftOp = node.path("leftOp");
        JsonNode rightOp = node.path("rightOp");

        if (leftOp.path(0).path("p").isNumber() || rightOp.path(0).path("p").isNumber()) {

            Text0 json0 = new Text0();

            List<Text0Operation> leftOpT = mapper.convertValue(leftOp, text0Type);
            List<Text0Operation> rightOpT = mapper.convertValue(rightOp, text0Type);
            JsonNode r0 = node.path("r0");
            JsonNode r1 = node.path("r1");
            List<List<Text0Operation>> lists = json0.transformX(leftOpT, rightOpT);

            assertEquals(r0, mapper.convertValue(lists.get(0), JsonNode.class));
            assertEquals(r1, mapper.convertValue(lists.get(1), JsonNode.class));

        }else {
            Json0 json0 = new Json0();

            List<Json0Operation> leftOpT = mapper.convertValue(leftOp, json0Type);
            List<Json0Operation> rightOpT = mapper.convertValue(rightOp, json0Type);
            JsonNode r0 = node.path("r0");
            JsonNode r1 = node.path("r1");
            List<List<Json0Operation>> lists = json0.transformX(leftOpT, rightOpT);

            assertEquals(r0, mapper.convertValue(lists.get(0), JsonNode.class));
            assertEquals(r1, mapper.convertValue(lists.get(1), JsonNode.class));
        }
    }

    private void testTransformComponentDetail(JsonNode node) throws Json0Exception {
        Json0 json0 = new Json0();
        JsonNode destOrigin = node.path("destOrigin");
        JsonNode c = node.path("c");
        JsonNode otherC = node.path("otherC");
        JsonNode type = node.path("type");

        List<Json0Operation> destOriginT = mapper.convertValue(destOrigin, json0Type);
        Json0Operation cT = mapper.convertValue(c, Json0Operation.class);
        Json0Operation otherCT = mapper.convertValue(otherC, Json0Operation.class);
        JsonNode dest = node.path("dest");
        List<Json0Operation> transformedOp = json0.transformComponent(destOriginT, cT, otherCT, type.asText());

        assertEquals(dest, mapper.convertValue(transformedOp, JsonNode.class));
    }

    private void testApplyDetail(JsonNode node) throws Json0Exception {
        Json0 json0 = new Json0();
        JsonNode snapshotOrigin = node.path("snapshot");
        JsonNode op = node.path("op");
        JsonNode snapshotResult = node.path("snapshotResult");

        List<Json0Operation> opT = mapper.convertValue(op, json0Type);
        JsonNode r = json0.apply(snapshotOrigin, opT);

        assertEquals(snapshotResult, r);
    }

    @Test
    public void testComposeDect() throws JsonProcessingException, Json0Exception {
        JsonNode jsonNode = mapper.readTree("{\"op1\":[{\"p\":[2],\"li\":{\"He\":null,\"thou\":{\"tree\":null}}}],\"op2\":[{\"p\":[2],\"ld\":{\"He\":null,\"thou\":{\"tree\":null}}},{\"p\":[2],\"li\":\"\"},{\"p\":[],\"od\":[26,[null,\"borogoves\"],\"\"],\"oi\":\"\"},{\"p\":[],\"t\":\"json0\",\"o\":[{\"p\":0,\"i\":\"were \"}]}],\"newOp\":[{\"p\":[2],\"li\":{\"He\":null,\"thou\":{\"tree\":null}}},{\"p\":[2],\"ld\":{\"He\":null,\"thou\":{\"tree\":null}}},{\"p\":[2],\"li\":\"\"},{\"p\":[],\"od\":[26,[null,\"borogoves\"],\"\"],\"oi\":\"\"},{\"p\":[],\"t\":\"json0\",\"o\":[{\"p\":0,\"i\":\"were \"}]}]}");
        testComposeDetail(jsonNode);

    }



    private void testComposeDetail(JsonNode node) throws Json0Exception {
        Json0 json0 = new Json0();
        JsonNode op1 = node.path("op1");
        JsonNode op2 = node.path("op2");

        List<Json0Operation> op1T = mapper.convertValue(op1, json0Type);
        List<Json0Operation> op2T = mapper.convertValue(op2, json0Type);
        JsonNode newOp = node.path("newOp");
        List<Json0Operation> composedOp = json0.compose(op1T, op2T);
        assertEquals(newOp, mapper.convertValue(composedOp, JsonNode.class));
    }

}