import java.util.Comparator;

import components.map.Map;
import components.map.Map.Pair;
import components.map.Map1L;
import components.queue.Queue;
import components.queue.Queue1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * WordCounter sorts the word occurrences in a given input file and outputs a
 * HTML file displaying the name of the input file in a heading followed by a
 * table listing the words and their corresponding counts. The words should
 * appear in alphabetical order
 *
 * @author Kate Tang
 */
public final class WordCounter {

    /**
     * No argument constructor
     */
    public WordCounter() {
    }

    /**
     * Defines the set of separator characters.
     *
     * @param s
     *            the {@code Set} to store separator characters
     */
    public static void defineSeparators(Set<Character> s) {
        s.add(' ');
        s.add('.');
        s.add(',');
        s.add(':');
        s.add(';');
        s.add('?');
        s.add('!');
        s.add('"');
        s.add('(');
        s.add(')');
        s.add('-');
    }

    //from 2221 lab
    /**
     * Compare {@code String}s in lexicographic order.
     */
    public static class StringLT implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }

    /**
     * Returns the first "word" (maximal length string of characters not in
     * {@code separators}) or "separator string" (maximal length string of
     * characters in {@code separators}) in the given {@code text} starting at
     * the given {@code position}.
     *
     * @param text
     *            the {@code String} from which to get the word or separator
     *            string
     * @param position
     *            the starting index
     * @param separators
     *            the {@code Set} of separator characters
     * @return the first word or separator string found in {@code text} starting
     *         at index {@code position}
     * @requires 0 <= position < |text|
     * @ensures <pre>
     * nextWordOrSeparator =
     *   text[position, position + |nextWordOrSeparator|)  and
     * if entries(text[position, position + 1)) intersection separators = {}
     * then
     *   entries(nextWordOrSeparator) intersection separators = {}  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      intersection separators /= {})
     * else
     *   entries(nextWordOrSeparator) is subset of separators  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      is not subset of separators)
     * </pre>
     */
    public static String nextWordOrSeparator(String text, int position,
            Set<Character> separators) {
        assert text != null : "Violation of: text is not null";
        assert separators != null : "Violation of: separators is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < text.length() : "Violation of: position < |text|";

        char first = text.charAt(position);
        boolean isSeparator = separators.contains(first);
        String output = "" + first;
        for (int i = position + 1; i < text.length()
                && isSeparator == separators.contains(text.charAt(i)); i++) {
            output += text.charAt(i);
        }

        return output;
    }

    /*
     * Outputs the opening tags in the generated HTML file.
     *
     * @param out the output stream
     *
     * @param title the title of the HTML page
     *
     * @param inputFile the name of the input file
     */
    public static void outputHeader(SimpleWriter out, String title,
            String inputFile) {
        assert out != null : "Violation of: out is not null";
        assert out.isOpen() : "Violation of: out.is_open";

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>" + title + "</title>");
        out.println("</head>");

        out.println("<body>");
        out.println("<h2>Words Counted in " + inputFile + "</h2>");
        out.println("<hr />");
        out.println("<table border=\"1\">");
        out.println("<tr>");
        out.println("<th>Words</th>");
        out.println("<th>Counts</th>");
        out.println("</tr>");
    }

    /*
     * Outputs the closing tags in the generated HTML file.
     *
     * @param out the output stream
     */
    public static void outputFooter(SimpleWriter out) {
        assert out != null : "Violation of: out is not null";
        assert out.isOpen() : "Violation of: out.is_open";

        //outputs "closing" tags
        out.println("</table>");
        out.println("<hr />");
        out.println("</body>");
        out.println("</html>");

    }

    /*
     * Generates the HTML page with a table of words and their counts.
     *
     * @param map the map containing words and their counts
     *
     * @param output the output stream
     *
     * @param inputFile the name of the input file
     */
    public static void generateIndex(Map<String, Integer> map,
            SimpleWriter output, String inputFile) {
        //comparator to alphabetize all words
        Comparator<String> alphabeticalOrder = new StringLT();

        //creates header
        outputHeader(output, "Word Count", inputFile);

        //create queue to store all words
        Queue<String> words = new Queue1L<String>();

        //for-each loop that adds every word to the queue using its map pair
        for (Pair<String, Integer> pair : map) {
            words.enqueue(pair.key());
        }

        //sort all words into alphabetical order
        words.sort(alphabeticalOrder);

        //for-each loop to output the count of each word
        for (String str : words) {
            if (map.hasKey(str)) {
                output.print("<tr><td>" + str + "</td><td>" + map.value(str)
                        + "</td>\n</tr>\n");
            }
        }

        //create footer
        outputFooter(output);

    }

    /*
     * Scans the input file, counts word occurrences, and fills the map with
     * word-count pairs.
     *
     * @param input the input stream
     *
     * @param output the output stream
     *
     * @param inputFile the name of the input file
     */
    public static void wordCount(SimpleReader input, SimpleWriter output,
            String inputFile) {
        //create set of separators
        Set1L<Character> separators = new Set1L<Character>();
        defineSeparators(separators);

        //create map of every word and its count
        Map<String, Integer> words = new Map1L<String, Integer>();

        //while the file is not empty
        while (!input.atEOS()) {
            //find next line and return position to 0
            String line = input.nextLine();
            int position = 0;

            while (position < line.length()) {
                //finds next word
                String wordOrSeparator = nextWordOrSeparator(line, position,
                        separators).toLowerCase();

                //updates position to next word
                position += wordOrSeparator.length();

                //checks if the 'word' is a separator
                if (!separators.contains(wordOrSeparator.charAt(0))) {
                    //increments count if word is already in the map
                    if (words.hasKey(wordOrSeparator)) {
                        words.replaceValue(wordOrSeparator,
                                words.value(wordOrSeparator) + 1);
                    } else {
                        //if not, add to map with 1 count
                        words.add(wordOrSeparator, 1);
                    }
                }
            }
        }
        //create HTML page using the map
        generateIndex(words, output, inputFile);
    }

    /**
     * Main method
     *
     * This method reads an input file for words, counts their occurrences, and
     * generates an HTML file displaying the results.
     *
     * @param args
     *            the command line arguments; not used
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        //user input
        out.println("Enter the input file name:");
        String inputFileName = in.nextLine();
        SimpleReader intputFile = new SimpleReader1L(inputFileName);

        out.println("Enter the output folder name:");
        SimpleWriter outputFile = new SimpleWriter1L(in.nextLine());

        //'main' method that processes the input file and outputs the HTML page
        wordCount(intputFile, outputFile, inputFileName);

        intputFile.close();
        outputFile.close();
        in.close();
        out.close();
    }

}
