import java.util.Scanner;

import java.nio.charset.Charset;
import java.io.*;
import java.nio.file.Files;
import java.io.File;

public class Anchored {

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);

        String filename = scanner.nextLine();
        File infile = new File(filename);

        try {
            Scanner input = new Scanner(infile);
            String s1, s2;
            s1 = input.nextLine();
            s2 = input.nextLine();

            // Split the strings
            String[] segments1 = s1.split("\\*");
            String[] segments2 = s2.split("\\*");

            int result = 0;

            for (int i = 0; i < Math.min(segments1.length, segments2.length); i++) {
                int lcs = longestCommonSubsequence(segments1[i], segments2[i]);
                result += 1 + lcs;
            }

            System.out.println(result);

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            scanner.close();
        }
    }

    /**
     * Find the longest common subsequence between two strings s1 and s2
     * @param s1 The first string
     * @param s2 The second string
     * @return The longest common subsequence
     */
    private static int longestCommonSubsequence(String s1, String s2) {

        int[][] dp = fillTable(s1, s2);

        return reconstructLCS(dp, s1, s2);
    }

    /**
     * Fill the DP table for the longest common subsequence
     * @param s1 The first string
     * @param s2 The second string
     * @return The DP table
     */
    private static int[][] fillTable(String s1, String s2) {
        int n = s1.length();
        int m = s2.length();

        int[][] dp = new int[n+1][m+1];

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (s1.charAt(i-1) == s2.charAt(j-1)) {
                    dp[i][j] = 1 + dp[i-1][j-1];
                } else if (s1.charAt(i-1) == '*') {
                    dp[i][j] = dp[i][j-1];
                } else if (s2.charAt(j-1) == '*') {
                    dp[i][j] = dp[i-1][j];
                } else {
                    dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]);
                }
            }
        }
        return dp;
    }

    /**
     * Reconstruct the longest common subsequence from the DP table
     *
     * @param dp The DP table
     * @param s1 The first string
     * @param s2 The second string
     * @return The longest common subsequence
     */
    private static int reconstructLCS(int[][] dp, String s1, String s2) {
        int n = s1.length();
        int m = s2.length();

        int lcsLength = 0;
        int i = n, j = m;
        while (i > 0 && j > 0) {
            if (s1.charAt(i-1) == s2.charAt(j-1)) {
                lcsLength++;
                i--;
                j--;
            } else if (dp[i-1][j] > dp[i][j-1]) {
                i--;
            } else {
                j--;
            }
        }

        return lcsLength;
    }
}

