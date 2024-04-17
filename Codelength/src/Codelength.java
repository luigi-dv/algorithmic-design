import java.util.Scanner;
import java.nio.charset.Charset;
import java.io.*;
import java.nio.file.Files;
import java.io.File;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Codelength {
    public static final String REGEX_PATTERN = "[^A-Za-z]+";
    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);

        String filename = scanner.nextLine();
        File infile = new File(filename);

        try {
            // Process the text and get the processed text
            String text = preprocessText(infile);
            text = addPaddingToText(text);
            // Compute the length of the encoded text for each number of characters
            int singleCharLength = calculateEncodedLength(text, 1);
            int doubleCharLength = calculateEncodedLength(text, 2);
            int tripleCharLength = calculateEncodedLength(text, 3);
            // Output that will be tested
            System.out.printf("%d %d %d\n", singleCharLength, doubleCharLength, tripleCharLength);

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            scanner.close();
        }
    }

    /**
     * Process the text and return the processed text
     * @param infile File containing text
     * @return Processed text
     * @throws IOException if file is not found
     */
    private static String preprocessText(File infile) throws IOException {
        String content = Files.readString(infile.toPath(), Charset.defaultCharset());
        content = content.replaceAll(REGEX_PATTERN, "").toLowerCase();
        return content;
    }

    /**
     * Check and computes the length of the encoded text for a given c
     *
     * @param text Processed text
     * @param n    number of characters to consider
     * @return Length of the encoded text
     */
    private static int calculateEncodedLength(String text, int n) {
        HashMap<String, Integer> frequencyMap = generateFrequencyMap(text, n);
        PriorityQueue<HuffmanNode> priorityQueue = generatePriorityQueue(frequencyMap);
        HuffmanNode root = buildHuffmanTree(priorityQueue);
        HashMap<String, String> huffmanCodes = generateHuffmanCodes(root);
        String encodedString = encodeText(huffmanCodes, text, n);
        return getEncodedStringLength(huffmanCodes, encodedString, n).length();
    }


    /**
     * Generate the frequency table map
     * @param text Processed text
     * @param c Number of characters
     * @return Frequency table
     */
    private static HashMap<String, Integer> generateFrequencyMap(String text, int c) {
        HashMap<String, Integer> frequency = new HashMap<>();
        int n = text.length();
        for (int i = 0; i < n; i += c) {
            String subString = text.substring(i, i + c);
            if (frequency.containsKey(subString)) {
                frequency.put(subString, frequency.get(subString) + 1);
            } else {
                frequency.put(subString, 1);
            }
        }
        return frequency;
    }

    /**
     * Get the encoded string length
     * @param codes Huffman codes
     * @param text Processed text
     * @param n Number of characters
     * @return Encoded string length
     */
    private static String getEncodedStringLength(HashMap<String, String> codes, String text, int n) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i += n) {
            if (i + n > text.length()) {
                n = text.length() - i;
            }
            String character = text.substring(i, i + n);
            String code = codes.get(character);
            builder.append(code);
        }
        return builder.toString();
    }

    /**
     * Generate the Huffman tree
     * @param priorityQueue Priority queue
     * @return Huffman tree
     */
    private static HuffmanNode buildHuffmanTree(PriorityQueue<HuffmanNode> priorityQueue) {

        if(priorityQueue.size() == 1){
            HuffmanNode node = priorityQueue.poll();
            HuffmanNode newNode = new HuffmanNode(node.frequency);
            newNode.left = node;
            return newNode;
        }

        while (priorityQueue.size() > 1) {
            HuffmanNode left = priorityQueue.poll();
            HuffmanNode right = priorityQueue.poll();
            HuffmanNode parent = new HuffmanNode(left.frequency + right.frequency);
            parent.left = left;
            parent.right = right;
            priorityQueue.offer(parent);
        }

        return priorityQueue.poll();
    }


    /**
     * Generate the Huffman codes
     * @param root Huffman tree
     * @return Huffman codes
     */
    private static HashMap<String, String> generateHuffmanCodes(HuffmanNode root) {
        HashMap<String, String> huffmanCodes = new HashMap<>();
        generateCodes(root, "", huffmanCodes);
        return huffmanCodes;
    }

    /**
     * Add the Huffman codes to the map
     * @param node Huffman node
     * @param code Code
     * @param huffmanCodes Huffman codes
     */
    private static void generateCodes(HuffmanNode node, String code, HashMap<String, String> huffmanCodes) {
        if (node == null) return;
        if (node.left == null && node.right == null) {
            huffmanCodes.put(node.value, code);
            return;
        }
        generateCodes(node.left, code + "0", huffmanCodes);
        if (node.right != null) {
            generateCodes(node.right, code + "1", huffmanCodes);
        }
    }

    /**
     * Generate the priority queue
     * @param frequencyMap Frequency Map
     * @return Priority queue
     */
    private static PriorityQueue<HuffmanNode> generatePriorityQueue(HashMap<String, Integer> frequencyMap) {
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>();
        for (String key : frequencyMap.keySet()) {
            priorityQueue.offer(new HuffmanNode(key, frequencyMap.get(key)));
        }
        return priorityQueue;
    }

    /**
     * Encode the text using Huffman codes
     * @param huffmanCodes Huffman codes
     * @param text Processed text
     * @param n Number of characters
     * @return Encoded text
     */
    private static String encodeText(HashMap<String, String> huffmanCodes, String text, int n) {
        StringBuilder encodedText = new StringBuilder();
        for (int i = 0; i < text.length(); i += n) {
            int endIndex = i + n;
            String substring = text.substring(i, endIndex);
            String code = huffmanCodes.get(substring);
            if (code != null) {
                encodedText.append(code);
            }
        }
        return encodedText.toString();
    }

    /**
     * Add characters to the text to make it divisible by n
     *
     * @param processedText Text
     * @return Text with added characters
     */
    private static String addPaddingToText(String processedText) {
        int paddingLength = 6 - (processedText.length() % 6);
        if (paddingLength != 6) {
            for (int i = 0; i < paddingLength; i++) {
                processedText += 'z';
            }
        }
        return processedText;
    }
}

class HuffmanNode implements Comparable<HuffmanNode> {
    String value;
    int frequency;
    HuffmanNode left;
    HuffmanNode right;

    public HuffmanNode(int frequency) {
        this.frequency = frequency;
        this.value = "";
        this.left = null;
        this.right = null;
    }

    public HuffmanNode(String value, int frequency) {
        this.value = value;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
    }

    @Override
    public int compareTo(HuffmanNode node) {
        return this.frequency - node.frequency;
    }
}
