package com.banti.framework.utils;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class parses the specified line by comma separator.
 */
public abstract class CSVParser {

    private static final String SEPARATOR_CHA = "\\\\n";

    /**
     * Parses the specified line by the comma separator and returns array 
     * of parsed string. 
     * @param line - one line.
     * @return array of parsed string
     * @throws InvalidParameterException
     */
    public static String[] parse(String line) throws InvalidParameterException {

        if (!validate(line)) {
            throw new InvalidParameterException("Invalide line. " + line);
        }
        int start_pointer = 0;
        int end_pointer = 0;
        List<String> vec = new ArrayList<String>();

        String tmpLine = line;
        int pointer = 0;
        boolean quote = false;

        while (line.length() > pointer) {
            if (tmpLine.charAt(pointer) == ',') {
                if (!quote) {
                    end_pointer = pointer;
                    put(vec, tmpLine, start_pointer, end_pointer);
                    start_pointer = ++end_pointer;
                }
            } else if (tmpLine.charAt(pointer) == '"') {
                quote = quote ? false : true;
            }
            pointer++;
        }
        if (end_pointer != pointer) {
            end_pointer = pointer;
            put(vec, tmpLine, start_pointer, end_pointer);
            start_pointer = ++end_pointer;
        } else {
            vec.add("");
        }
        return vec.toArray(new String[vec.size()]);
    }

    public static String parse(String line, int col) throws InvalidParameterException {
        int start_pointer = 0;
        int end_pointer = 0;
        String tmpLine = line;
        int pointer = 0;
        int cnt = 0;
        boolean quote = false;
        while (line.length() > pointer) {
            if (tmpLine.charAt(pointer) == ',') {
                if (!quote) {
                    end_pointer = pointer;
                    if (cnt == col) {
                        return check(tmpLine, start_pointer, end_pointer);
                    }
                    cnt++;
                    start_pointer = ++end_pointer;
                }
            } else if (tmpLine.charAt(pointer) == '"') {
                quote = quote ? false : true;
            }
            pointer++;
        }
        if (end_pointer != pointer) {
            end_pointer = pointer;
            if (cnt == col) {
                return check(tmpLine, start_pointer, end_pointer);
            }
            cnt++;
            start_pointer = ++end_pointer;
        } else {
            return "";
        }
        return null;
    }

    /**
     * <pre>
     *  '"''"' -> '"'
     *  '\''n' -> '\n'
     *  '\''r' -> '\r'
     *  '\''\''n' -> '\''n' 
     * </pre>
     */
    private static void put(List<String> vec, String line, int start_pointer, int end_pointer) {
        String tmpLine = line.substring(start_pointer, end_pointer);

        if (tmpLine.length() > 1 && tmpLine.startsWith("\"") && tmpLine.endsWith("\"")) {
            tmpLine = tmpLine.substring(1, tmpLine.length() - 1);
        }

        tmpLine = tmpLine.replaceAll("\"\"", "\"");
        tmpLine = tmpLine.replaceAll("(?<=[^\\\\])\\\\n", "\n");
        tmpLine = tmpLine.replaceAll("(?<=[^\\\\])\\\\r", "\r");

        tmpLine = tmpLine.replaceAll("\\\\\\\\n", "\\\\n");
        tmpLine = tmpLine.replaceAll("\\\\\\\\r", "\\\\r");

        vec.add(tmpLine);
    }

    private static String check(String line, int start_pointer, int end_pointer) {
        String tmpLine = line.substring(start_pointer, end_pointer);
        if (tmpLine.length() > 1 && tmpLine.startsWith("\"") && tmpLine.endsWith("\"")) {
            tmpLine = tmpLine.substring(1, tmpLine.length() - 1);
        }
        tmpLine = tmpLine.replaceAll("\"\"", "\"");
        tmpLine = tmpLine.replaceAll("(?<=[^\\\\])\\\\n", "\n");
        tmpLine = tmpLine.replaceAll("(?<=[^\\\\])\\\\r", "\r");
        tmpLine = tmpLine.replaceAll("\\\\\\\\n", "\\\\n");
        tmpLine = tmpLine.replaceAll("\\\\\\\\r", "\\\\r");
        return tmpLine;
    }

    private static boolean validate(String line) {
        char[] arr = line.toCharArray();

        int numberOfQuotes = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == '"') {
                numberOfQuotes++;
            }
        }
        if ((numberOfQuotes % 2) == 1) {
            return false;
        }
        return true;
    }

    public static String convert(String[] strs) {
        if (strs == null || strs.length < 1) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strs.length; i++) {
            sb.append(convert(strs[i]));
            if (i + 1 < strs.length) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /**
     * Convert specified line to csv format.
     * <pre>
     * '\''n' -> '\''\''n'
     * '\''r' -> '\''\''r'
     * '\n' -> '\''n'
     * '\r' -> '\''r'
     * '\'  -> '\'
     * '"'  -> '"''"'
     * </pre>
     * @param str
     * @return converted value.
     */
    public static String convert(String str) {

        if (str == null) {
            return "";
        }

        str = str.replaceAll("\\\\\\\\r", "\\\\\\\\r");
        str = str.replaceAll("\\\\n", "\\\\\\\\n");
        str = str.replaceAll(System.getProperty("line.separator"), SEPARATOR_CHA);
        str = str.replaceAll("\r", "\\\\r");
        str = str.replaceAll("\n", SEPARATOR_CHA);

        if (str.indexOf("\"") > -1) {
            str = str.replaceAll("\"", "\"\"");
            str = "\"" + str + "\"";
        } else if (str.indexOf(",") > -1) {
            str = "\"" + str + "\"";
        }
        return str;
    }

    public static void main(String[] args) {

        System.out.println("Input:" + args[0]);
        String s[] = parse(args[0]);
        System.out.println("Total Number of Tokens:" + s.length);
        System.out.println("Following are the Tokens:");
        for (int i = 0; i < s.length; i++) {
            System.out.println(">>" + s[i] + "<<");
        }

        //        String teststr = "\"aaa\raaaa\\naaa\\\\aaa\\aaa\\\\naaa\naaa\"";
        //
        //        String teststr2 = parse(teststr)[0];
        //        String teststr3 = convert(teststr2);
        //        System.out.println(teststr3);
        //        if (teststr.equals(teststr3)) {
        //            System.out.println("OK");
        //        }

    }

}