import java.util.Scanner;
import java.nio.charset.Charset;
import java.io.*;
import java.nio.file.Files;
import java.io.File;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Codelength {
    public static final String REGEX_RULE = "[^A-Za-z]+";
    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);

        String filename = scanner.nextLine();
        File infile = new File(filename);

        try {
            // Process the text and get the processed text
            String text = processText(infile);
            // Compute the length of the encoded text for each number of characters
            int singleChars = computeEncodedTextLength(text, 1);
            int doubleChars = computeEncodedTextLength(text, 2);
            int tripleChars = computeEncodedTextLength(text, 3);
            // Output that will be tested
            System.out.printf("%d %d %d\n", singleChars, doubleChars, tripleChars);

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
    private static String processText(File infile) throws IOException {
        String text = Files.readString(infile.toPath(), Charset.defaultCharset());

        text = text.replaceAll(REGEX_RULE,  "").toLowerCase();
        return addCharacters(text);
    }

    /**
     * Check and computes the length of the encoded text for a given c
     *
     * @param text Processed text
     * @param n    number of characters to consider
     * @return Length of the encoded text
     */
    private static int computeEncodedTextLength(String text, int n) {
        HashMap<String, Integer> frequencyTable = generateFrequencyTable(text, n);
        PriorityQueue<HuffmanNode> queue = generatePriorityQueue(frequencyTable);
        HuffmanNode root = generateHuffmanTree(queue);
        HashMap<String, String> codes = generateHuffmanCodes(root);
        return getEncodedStringLength(codes, text, n).length();
    }

    /**
     * Generate the frequency table
     * @param text Processed text
     * @param c Number of characters
     * @return Frequency table
     */
    private static HashMap<String, Integer> generateFrequencyTable(String text, int c) {
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
            String character = text.substring(i, i + n);
            String code = codes.get(character);
            builder.append(code);
        }
        return builder.toString();
    }

    /**
     * Generate the Huffman codes
     * @param root Huffman tree
     * @return Huffman codes
     */
    private static HashMap<String, String> generateHuffmanCodes(HuffmanNode root) {
        HashMap<String, String> codes = new HashMap<>();
        computeCodes(root, codes, "");
        return codes;
    }

    /**
     * Compute the Huffman codes
     * @param root Huffman tree
     * @param codes Huffman codes
     * @param string String
     */
    private static void computeCodes(HuffmanNode root, HashMap<String, String> codes, String string) {
        if (root == null) {
            return;
        }
        if (root.getLeft() == null && root.getRight() == null) {
            codes.put(root.getLetter(), string);
            return;
        }
        computeCodes(root.getLeft(), codes, string + "0");
        computeCodes(root.getRight(), codes, string + "1");
    }

    /**
     * Generate the Huffman tree
     * @param queue Priority queue
     * @return Huffman tree
     */
    private static HuffmanNode generateHuffmanTree(PriorityQueue<HuffmanNode> queue) {

        if (queue.size() == 1) {
            HuffmanNode node = queue.peek();
            HuffmanNode node1 = new HuffmanNode(node.value);
            node1.setLeft(node);
            return node1;
        }

        while (queue.size() > 1) {
            HuffmanNode node1 = queue.poll();
            HuffmanNode node2 = queue.poll();
            HuffmanNode newNode = new HuffmanNode(node1.getValue() + node2.getValue());
            newNode.setLeft(node1);
            newNode.setRight(node2);
            queue.add(newNode);
        }
        return queue.peek();
    }

    /**
     * Generate the priority queue
     * @param frequencyTable Frequency table
     * @return Priority queue
     */
    private static PriorityQueue<HuffmanNode> generatePriorityQueue(HashMap<String, Integer> frequencyTable) {
        // Basically it is a min heap priority queue
        PriorityQueue<HuffmanNode> queue = new PriorityQueue<>();
        for (String entry : frequencyTable.keySet()) {
            HuffmanNode node = new HuffmanNode(frequencyTable.get(entry), entry);
            queue.add(node);
        }
        return queue;
    }

    /**
     * Add characters to the text to make it divisible by 6 (as the assignment requires)
     * @param text Text
     * @return Text with added characters
     */
    private static String addCharacters(String text) {
        int remainLength = text.length() % 6;
        if (remainLength != 0) {
            remainLength = 6 - remainLength;
        }
        // We add z to the end of the text to make it divisible by 6
        StringBuilder textBuilder = new StringBuilder(text);
        for (int i = 0; i < remainLength; i++) {
            textBuilder.append('z');
        }
        text = textBuilder.toString();
        return text;
    }
}

class HuffmanNode implements Comparable<HuffmanNode> {
    public final int value;
    public final String letter;
    public HuffmanNode right, left;

    public HuffmanNode(int value, String letter) {
        this.value = value;
        this.letter = letter;
    }

    public HuffmanNode(int value) {
        this.value = value;
        this.letter = null;
    }

    public int getValue() {
        return value;
    }

    public String getLetter() {
        return letter;
    }

    public HuffmanNode getRight() {
        return right;
    }

    public void setRight(HuffmanNode right) {
        this.right = right;
    }

    public HuffmanNode getLeft() {
        return left;
    }

    public void setLeft(HuffmanNode left) {
        this.left = left;
    }

    @Override
    public int compareTo(HuffmanNode o) {
        return Integer.compare(this.value, o.value);
    }

    @Override
    public String toString() {
        return String.format("[Value: %d, Symbol: %s]", value, letter);
    }
}