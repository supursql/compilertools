package cn.xq.backend;

/**
 * @author: 紫苏
 * @date: 20-7-30 上午10:26
 * @description:
 */
public class Error {

    int id; //错误序号
    String info; //错误信息
    int line; //错误所在行
    Word word; //错误的单词

    public Error() {
    }

    public Error(int id, String info, int line, Word word) {
        this.id = id;
        this.info = info;
        this.line = line;
        this.word = word;
    }
}
