package json0;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;


public class Gen {

    private static ObjectMapper MAPPER = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        File file = FileUtils.getFile("E:\\json\\json0\\transformX");
        System.out.println("Hello World");
        File[] files = file.listFiles();
        ArrayNode arr = MAPPER.createArrayNode();
        int size = 0;
        int fileChunk = 0;
        for (File file1 : files) {
            JsonNode jsonNode = MAPPER.readTree(file1);
            arr.add(jsonNode);
            if (size > 10000) {
                flush(arr, fileChunk++);
                arr = MAPPER.createArrayNode();
                size = 0;
            }
            size++;
        }
        flush(arr, fileChunk++);
    }

    private static void flush(ArrayNode arr, int fileChunk) throws IOException {
        System.out.println("json size " + arr.size());
        FileUtils.write(
                FileUtils.getFile("E:\\code\\json04j\\src\\test\\resources\\json0\\transformX\\testDataChunk_" + fileChunk + ".json"),
                arr.toString(),
                Charsets.UTF_8
        );

    }
}
