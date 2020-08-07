package cn.xq.backend;

import java.io.*;
import java.util.ArrayList;

/**
 * @author: 紫苏
 * @date: 20-7-30 上午10:25
 * @description:
 */
public class LexAnalyse {
    ArrayList<Word> wordList = new ArrayList<>();
    ArrayList<Error> errorList = new ArrayList<>();
    int wordCount = 0; //统计单词个个数
    int errorCount = 0; //统计错误个数
    boolean noteFlag = false; //多行注释标志
    boolean lexErrorFlag = false; //词法分析器出错标志

    /**
     * 数学字符判断
     * @param ch
     * @return
     */
    private static boolean isDigit(char ch) {
        boolean flag = false;
        if ('0' <= ch && ch <= '9') {
            flag = true;
        }
        return flag;
    }

    /**
     * 判断单词是否为int常量
     * @param word
     * @return
     */
    private static boolean isDigits(String word) {
        int i;
        boolean flag = false;
        for (i = 0; i < word.length(); i++) {
            if (Character.isDigit(word.charAt(i))) {
                continue;
            } else {
                break;
            }
        }

        if (i == word.length()) {
            flag = true;
        }

        return flag;
    }

    /**
     * 判断单词是否为char常量
     * @param word
     * @return
     */
    private static boolean isChar(String word) {
        boolean flag = false;
        int i = 0;
        char temp = word.charAt(i);
        if (temp == '\'') {
            for (i = 1; i < word.length(); i++) {
                temp = word.charAt(i);
                if (0 <= temp && temp <= 255) {
                    continue;
                } else {
                    break;
                }
            }

            if (i == word.length() && word.charAt(i) == '\'') {
                flag = true;
            }
        } else {
            return flag;
        }

        return flag;
    }

    /**
     * 判断字符是否为字母
     * @param ch
     * @return
     */
    private static boolean isLetter(char ch) {
        boolean flag = false;
        if (('a' <= ch && ch <= 'z') || ('A' <= ch && ch <= 'Z')) {
            flag = true;
        }

        return flag;
    }

    /**
     * 判断单词是否为合法标识符
     * @param word
     * @return
     */
    private static boolean isID(String word) {
        boolean flag = false;
        int i = 0;
        if (Word.isKey(word)) {
            return flag;
        }
        char temp = word.charAt(i);
        if (isLetter(temp) || temp == ' ') {
            for (i = 1; i < word.length(); i++) {
                temp = word.charAt(i);
                if (isLetter(temp) || temp == '_' || isDigit(temp)) {
                    continue;
                } else {
                    break;
                }
            }
            if (i >= word.length()) {
                flag = true;
            }
        } else {
            return flag;
        }

        return flag;
    }

    /**
     * 判断词法分析是否通过
     * @return
     */
    public boolean isFail() {
        return lexErrorFlag;
    }

    public void analyse(String str, int line) {
        int beginIndex;
        int endIndex;
        int index = 0;
        int length = str.length();
        Word word = null;
        Error error;

        char temp;
        while (index < length) {
            temp = str.charAt(index);
            if (!noteFlag) {
                if (isLetter(temp) || temp == '_') {
                    beginIndex = index;
                    index ++;

                    while ((index < length)
                            && (!Word.isOperator(str.substring(index, index + 1)))
                            && (!Word.isBoundarySign(str.substring(index, index + 1)))
                            && (str.charAt(index) != ' ')
                            && (str.charAt(index) != '\n')
                            && (str.charAt(index) != '\t')
                            && (str.charAt(index) != '\r')) {
                        index++;
                    }

                    endIndex = index;
                    word = new Word();
                    wordCount++;
                    word.id = wordCount;
                    word.line = line;
                    word.value = str.substring(beginIndex, endIndex);
                    if (Word.isKey(word.value)) {
                        word.type = Word.KEY;
                    } else if (isID(word.value)) {
                        word.type = Word.IDENTIFIER;
                    } else {
                        word.type = Word.UNIDER;
                        word.flag = false;
                        errorCount ++;
                        error = new Error(errorCount, "非法标识符", word.line, word);
                        errorList.add(error);
                        lexErrorFlag = true;
                    }

                    index --;
                } else if (isDigit(temp)) {
                    beginIndex = index;
                    index++;

                    while ((index < length)
                        && (!Word.isBoundarySign(str.substring(index, index + 1)))
                        && (!Word.isOperator(str.substring(index, index + 1)))
                            &&(str.charAt(index) != ' ')
                            &&(str.charAt(index) != '\t')
                            &&(str.charAt(index) != '\n')
                    ) {
                        index++;
                    }

                    endIndex = index;
                    word = new Word();
                    wordCount++;
                    word.id = wordCount;
                    word.line = line;
                    word.value = str.substring(beginIndex, endIndex);
                    if (isDigits(word.value)) {
                        word.type = Word.INT_CONST;
                    } else {
                        word.type = Word.UNIDER;
                        word.flag = false;
                        errorCount ++;
                        error = new Error(errorCount, "非法标识符", word.line, word);
                        errorList.add(error);
                        lexErrorFlag = true;
                    }
                    index --;
                } else if (String.valueOf(str.charAt(index)).equals("'")) {
                    beginIndex = index;
                    index ++;
                    temp = str.charAt(index);
                    while ((index < length) && (0 < temp && temp <= 255)) {
                        if (String.valueOf(str.charAt(index)) == "'") {
                            break;
                        }

                        index++;
                    }
                    if (index < length) {
                        endIndex = index;
                        word = new Word();
                        wordCount++;
                        word.id = wordCount;
                        word.line = line;
                        word.value = str.substring(beginIndex, endIndex);
                        word.type = Word.CHAR_CONST;
                        index --;
                    } else {
                        endIndex = index;
                        word = new Word();
                        wordCount ++;
                        word.id = wordCount;
                        word.line = wordCount;
                        word.value = str.substring(beginIndex, endIndex);
                        word.type = Word.UNIDER;
                        word.flag = false;
                        errorCount ++;
                        error = new Error(errorCount, "非法标识符", word.line, word);
                        errorList.add(error);
                        lexErrorFlag = true;
                        index --;
                    }
                } else if (temp == '=') {
                    beginIndex = index;
                    index++;
                    if (index < length && str.charAt(index) == '=') {
                        endIndex = index + 1;
                        word = new Word();
                        wordCount ++;
                        word.id = wordCount;
                        word.line = line;
                        word.value = str.substring(beginIndex, endIndex);
                        word.type = Word.OPERATOR;
                    } else {
                        word = new Word();
                        wordCount ++;
                        word.id = wordCount;
                        word.line = line;
                        word.value = str.substring(index - 1, index);
                        word.type = Word.OPERATOR;
                        index --;
                    }
                } else if (temp == '!') {
                    beginIndex = index;
                    index ++;

                    if (index < length && str.charAt(index) == '=') {
                        endIndex = index + 1;
                        word = new Word();
                        wordCount ++;
                        word.id = wordCount;
                        word.line = line;
                        word.value = str.substring(beginIndex, endIndex);
                        word.type = Word.OPERATOR;
                    } else {
                        word = new Word();
                        wordCount ++;
                        word.id = wordCount;
                        word.line = line;
                        word.value = str.substring(index - 1, index);
                        word.type = Word.OPERATOR;
                        index --;
                    }
                } else if (temp == '&') {
                    beginIndex = index;
                    index ++;
                    if (index < length && str.charAt(index) == '&') {
                        endIndex = index + 1;
                        word = new Word();
                        wordCount ++;
                        word.id = wordCount;
                        word.line = line;
                        word.value = str.substring(beginIndex, endIndex);
                        word.type = Word.OPERATOR;
                    } else {
                        word = new Word();
                        wordCount ++;
                        word.line = line;
                        word.value = str.substring(index - 1, index);
                        word.type = Word.OPERATOR;
                        index --;
                    }
                } else if (temp == '|') {
                    beginIndex = index;
                    index ++;
                    if (index < length && str.charAt(index) == '|') {
                        endIndex = index + 1;
                        word = new Word();
                        wordCount ++;
                        word.id = wordCount;
                        word.line = line;
                        word.value = str.substring(beginIndex, endIndex);
                        word.type = Word.OPERATOR;
                    } else {
                        word = new Word();
                        wordCount ++;
                        word.id = wordCount;
                        word.line = line;
                        word.value = str.substring(index - 1, index);
                        word.type = Word.OPERATOR;
                        index --;
                    }
                } else if (temp == '+') {
                    beginIndex = index;
                    index ++;
                    if (index < length && str.charAt(index) == '+') {
                        endIndex = index + 1;
                        word = new Word();
                        wordCount ++;
                        word.id = wordCount;
                        word.line = line;
                        word.value = str.substring(beginIndex, endIndex);
                        word.type = Word.OPERATOR;
                    } else {
                        word = new Word();
                        wordCount ++;
                        word.id = wordCount;
                        word.line = line;
                        word.value = str.substring(index - 1, index);
                        word.type = Word.OPERATOR;
                        index --;
                    }
                } else if (temp == '-') {
                    beginIndex = index;
                    index ++;
                    if (index < length && str.charAt(index) == '-') {
                        endIndex = index + 1;
                        word = new Word();
                        wordCount ++;
                        word.id = wordCount;
                        word.line = line;
                        word.value = str.substring(beginIndex, endIndex);
                        word.type = Word.OPERATOR;
                    } else {
                        word = new Word();
                        wordCount ++;
                        word.id = wordCount;
                        word.line = line;
                        word.value = str.substring(index - 1, index);
                        word.type = Word.OPERATOR;
                        index--;
                    }
                } else if (temp == '/') {
                    index ++;
                    if (index < length && str.charAt(index) == '/') {
                        break;
                    } else if (index < length && str.charAt(index) == '*') {
                        noteFlag = true;
                    } else {
                        word = new Word();
                        wordCount++;
                        word.id = wordCount;
                        word.line = line;
                        word.value = str.substring(index - 1, index);
                        word.type = Word.OPERATOR;
                    }

                    index --;
                } else {
                    switch (temp) {
                        case ' ':
                        case '\t':
                        case '\r':
                        case '\n':
                            word = null;
                            break;
                        case '[':
                        case ']':
                        case '(':
                        case ')':
                        case '{':
                        case '}':
                        case ',':
                        case '"':
                        case '.':
                        case ';':
                        case '*':
                        case '%':
                        case '>':
                        case '<':
                        case '?':
                        case '#':
                            word = new Word();
                            word.id = ++wordCount;
                            word.line = line;
                            word.value = String.valueOf(temp);
                            if (Word.isOperator(word.value)) {
                                word.type = Word.OPERATOR;
                            } else if (Word.isBoundarySign(word.value)) {
                                word.type = Word.BOUNDARYSIGN;
                            } else {
                                word.type = Word.END;
                            }
                            break;
                        default:
                            word = new Word();
                            wordCount ++;
                            word.id = wordCount;
                            word.line = line;
                            word.value = String.valueOf(temp);
                            word.type = Word.UNIDER;
                            word.flag = false;
                            errorCount ++;
                            error = new Error(errorCount, "非法标识符", word.line, word);
                            errorList.add(error);
                            lexErrorFlag = true;
                    }
                }
            } else {
                int i = str.indexOf("*/");
                if (i != -1) {
                    noteFlag = false;
                    index = i + 2;
                    continue;
                } else {
                    break;
                }
            }

            if (word == null) {
                index ++;
                continue;
            }

            wordList.add(word);
            index ++;
        }
    }


    public ArrayList<Word> lexAnalyse(String str) {
        String buffer[];
        buffer = str.split("\n");
        int line = 1;
        for (int i = 0; i < buffer.length; i++) {
            analyse(buffer[i].trim(), line);
            line++;
        }
        if (!wordList.get(wordList.size() - 1).type.equals(Word.END)) {
            Word word = new Word(++wordCount, "#", Word.END, line++);
            wordList.add(word);
        }

        return wordList;
    }

    public ArrayList<Word> lexAnalyseByFilePath(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        BufferedInputStream bis = new BufferedInputStream(fis);
        InputStreamReader isr = new InputStreamReader(bis, "utf-8");
        BufferedReader inbr = new BufferedReader(isr);

        String str = "";
        int line = 1;

        while ((str = inbr.readLine()) != null) {
            analyse(str.trim(), line);
            line ++;
        }

        inbr.close();
        if (!wordList.get(wordList.size() - 1).type.equals(Word.END)) {
            Word word = new Word(++wordCount, "#", Word.END, line++);
            wordList.add(word);
        }

        return wordList;
    }

    public String outputWordList() throws IOException {
        File file = new File("./output/");
        if (!file.exists()) {
            file.mkdir();
            file.createNewFile();
        }
        String path = file.getAbsolutePath();
        FileOutputStream fos = new FileOutputStream(path + "/wordList.txt");
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        OutputStreamWriter osw1 = new OutputStreamWriter(bos, "utf-8");
        PrintWriter pw1 = new PrintWriter(osw1);
        pw1.println("单词序号\t单词的值\t单词类型\t单词所在行\t单词是否合法");
        Word word;
        for (int i = 0; i < wordList.size(); i++) {
            word = wordList.get(i);
            pw1.println(word.id + "\t\t" + word.value + "\t" + word.type + "\t"
                    + "\t" + word.line + "\t" + word.flag);
        }

        if (lexErrorFlag) {
            Error error;
            pw1.println("错误信息如下:");

            pw1.println("错误序号\t错误信息\t错误所在行 \t错误单词");
            for (int i = 0; i < errorList.size(); i++) {
                error = errorList.get(i);
                pw1.println(error.id + "\t" + error.info + "\t\t" + error.line
                        + "\t" + error.word.value);
            }
        } else {
            pw1.println("词法分析通过：");
        }

        pw1.close();
        return path + "/wordList.txt";
    }

    public LexAnalyse() {

    }

    public LexAnalyse(String str) {
        lexAnalyse(str);
    }

    public static void main(String[] args) throws IOException {
        LexAnalyse lex = new LexAnalyse();
        lex.lexAnalyseByFilePath("/home/joe/IdeaProjects/compilertools/src/main/java/cn/xq/resources/a.c");
        lex.outputWordList();
    }

}
