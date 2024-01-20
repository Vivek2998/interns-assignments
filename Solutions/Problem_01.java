// Write an interactive program to implement ‘Approximate Search’ for a given set of N
// strings. That is, given a word, find the top k strings in the set among those that are
// similar.

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class ApproximateSearch {
    private static Map<String, String> stringSet = new HashMap<>();

    public static void main(String[] args) {
        
        readAndParseFile("Solution.txt");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Input >> ");
            String userInput = scanner.nextLine().trim();

            if (userInput.equals("exit")) {
                break;
            }

            List<String> suggestions = findApproximateStrings(userInput, 3); // Change 3 to the desired 'k'

            System.out.println("Output >> " + suggestions);
        }

        System.out.println("Exiting the program.");
    }

    private static void readAndParseFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split("\\s+");
                for (String word : words) {
                    stringSet.putIfAbsent(word.toLowerCase(), word);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> findApproximateStrings(String input, int k) {
        PriorityQueue<Map.Entry<String, Integer>> minHeap = new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));

        for (String word : stringSet.keySet()) {
            int distance = levenshteinDistance(input, word);
            if (minHeap.size() < k) {
                minHeap.offer(new AbstractMap.SimpleEntry<>(word, distance));
            } else if (minHeap.peek().getValue() < distance) {
                minHeap.poll();
                minHeap.offer(new AbstractMap.SimpleEntry<>(word, distance));
            }
        }

        List<String> suggestions = new ArrayList<>();
        while (!minHeap.isEmpty()) {
            suggestions.add(stringSet.get(minHeap.poll().getKey()));
        }
        Collections.reverse(suggestions);

        return suggestions;
    }

    private static int levenshteinDistance(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = min(dp[i - 1][j - 1] + costOfSubstitution(s1.charAt(i - 1), s2.charAt(j - 1)),
                            dp[i - 1][j] + 1,
                            dp[i][j - 1] + 1);
                }
            }
        }

        return dp[m][n];
    }

    private static int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }

    private static int min(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }
}
