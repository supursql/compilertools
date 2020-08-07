package cn.xq.backend;

import java.util.ArrayList;

/**
 * @author: 紫苏
 * @date: 20-7-30 上午9:22
 * @description:
 */
public class Word {
    public final static String KEY = "关键字";
    public final static String OPERATOR = "操作符";
    public final static String INT_CONST = "整型常量";
    public final static String CHAR_CONST = "字符常量";
    public final static String BOOL_CONST = "布尔常量";
    public final static String IDENTIFIER = "标志符";
    public final static String BOUNDARYSIGN = "界符";
    public final static String END = "结束符";
    public final static String UNIDER = "未知类型";
    public static ArrayList<String> key = new ArrayList<>();
    public static ArrayList<String> boundarySign = new ArrayList<>();
    public static ArrayList<String> operator = new ArrayList<>();

    static {
        Word.operator.add("+");
        Word.operator.add("-");
        Word.operator.add("++");
        Word.operator.add("--");
        Word.operator.add("*");
        Word.operator.add("/");
        Word.operator.add(">");
        Word.operator.add(">=");
        Word.operator.add("<");
        Word.operator.add("<=");
        Word.operator.add("==");
        Word.operator.add("!=");
        Word.operator.add("=");
        Word.operator.add("&&");
        Word.operator.add("||");
        Word.operator.add("!");
        Word.operator.add(".");
        Word.operator.add("?");
        Word.operator.add("!");
        Word.operator.add("&");
        Word.boundarySign.add("(");
        Word.boundarySign.add(")");
        Word.boundarySign.add("{");
        Word.boundarySign.add("}");
        Word.boundarySign.add(";");
        Word.boundarySign.add(",");
        Word.key.add("void");
        Word.key.add("main");
        Word.key.add("int");
        Word.key.add("char");
        Word.key.add("if");
        Word.key.add("else");
        Word.key.add("while");
        Word.key.add("for");
        Word.key.add("printf");
        Word.key.add("scanf");
    }

    int id; //单词序号
    String value; //单词的值
    String type; //单词类型
    int line; //单词所在行
    boolean flag = true;

    public Word() {
    }

    public Word(int id, String value, String type, int line) {
        this.id = id;
        this.value = value;
        this.type = type;
        this.line = line;
    }

    public static boolean isKey(String word) {
        return key.contains(word);
    }

    public static boolean isOperator(String word) {
        return operator.contains(word);
    }

    public static boolean isBoundarySign(String word) {
        return boundarySign.contains(word);
    }

    public static boolean isArOP(String word) {
        if ((word.equals("+") || word.equals("-") || word.equals("*") || word.equals("/"))) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isBoolOP(String word) {
        if ((word.equals(">") || word.equals("<") || word.equals("==")
        || word.equals("!=") || word.equals("!") || word.equals("&&") || word.equals("||"))) {
            return true;
        } else {
            return false;
        }
    }
}
