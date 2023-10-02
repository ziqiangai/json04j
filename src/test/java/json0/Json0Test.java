package json0;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class Json0Test {
    private static AtomicInteger COUNTER = new AtomicInteger();

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final TypeReference<List<Json0Operation>> text0Type = new TypeReference<List<Json0Operation>>() {
    };

    @Test
    public void testOpClone() {
    }

    @Test
    public void testInvert() {
        String folder = "E:\\code\\json04j\\src\\test\\resources\\json0\\invert";
        File file = FileUtils.getFile(folder);
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

                    List<Json0Operation> text0Operations = mapper.convertValue(opOrigin, text0Type);
                    List<Json0Operation> invert = json0.invert(text0Operations);
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
        String folder = "E:\\code\\json04j\\src\\test\\resources\\json0\\compose";
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
    public void testApply(){
        String folder = "E:\\code\\json04j\\src\\test\\resources\\json0\\apply";
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
    public void testTransformComponent(){
        String folder = "E:\\code\\json04j\\src\\test\\resources\\json0\\transformComponent";
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

    private void testTransformComponentDetail(JsonNode node) throws Json0Exception {
        System.out.println("test: " + COUNTER.getAndIncrement());
        Json0 text0 = new Json0();
        JsonNode destOrigin = node.path("destOrigin");
        JsonNode c = node.path("c");
        JsonNode otherC = node.path("otherC");
        JsonNode type = node.path("type");

        List<Json0Operation> destOriginT = mapper.convertValue(destOrigin, text0Type);
        Json0Operation cT = mapper.convertValue(c, Json0Operation.class);
        Json0Operation otherCT = mapper.convertValue(otherC, Json0Operation.class);
        JsonNode dest = node.path("dest");
        List<Json0Operation> transformedOp = text0.transformComponent(destOriginT, cT, otherCT, type.asText());

        assertEquals(dest, mapper.convertValue(transformedOp, JsonNode.class));
    }

    private void testApplyDetail(JsonNode node) throws Json0Exception {
        Json0 text0 = new Json0();
        JsonNode snapshotOrigin = node.path("snapshot");
        JsonNode op = node.path("op");
        JsonNode snapshotResult = node.path("snapshotResult");

        List<Json0Operation> opT = mapper.convertValue(op, text0Type);
        JsonNode r = text0.apply(snapshotOrigin, opT);

        assertEquals(snapshotResult, r);
    }

    @Test
    public void testComposeDect() throws JsonProcessingException, Json0Exception {
        JsonNode jsonNode = mapper.readTree("{\"op1\":[{\"p\":[2],\"li\":{\"He\":null,\"thou\":{\"tree\":null}}}],\"op2\":[{\"p\":[2],\"ld\":{\"He\":null,\"thou\":{\"tree\":null}}},{\"p\":[2],\"li\":\"\"},{\"p\":[],\"od\":[26,[null,\"borogoves\"],\"\"],\"oi\":\"\"},{\"p\":[],\"t\":\"text0\",\"o\":[{\"p\":0,\"i\":\"were \"}]}],\"newOp\":[{\"p\":[2],\"li\":{\"He\":null,\"thou\":{\"tree\":null}}},{\"p\":[2],\"ld\":{\"He\":null,\"thou\":{\"tree\":null}}},{\"p\":[2],\"li\":\"\"},{\"p\":[],\"od\":[26,[null,\"borogoves\"],\"\"],\"oi\":\"\"},{\"p\":[],\"t\":\"text0\",\"o\":[{\"p\":0,\"i\":\"were \"}]}]}");
        testComposeDetail(jsonNode);

    }



    private void testComposeDetail(JsonNode node) throws Json0Exception {
        Json0 text0 = new Json0();
        JsonNode op1 = node.path("op1");
        JsonNode op2 = node.path("op2");

        List<Json0Operation> op1T = mapper.convertValue(op1, text0Type);
        List<Json0Operation> op2T = mapper.convertValue(op2, text0Type);
        JsonNode newOp = node.path("newOp");
        List<Json0Operation> composedOp = text0.compose(op1T, op2T);
        assertEquals(newOp, mapper.convertValue(composedOp, JsonNode.class));
    }

}