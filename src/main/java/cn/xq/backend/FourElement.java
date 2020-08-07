package cn.xq.backend;

/**
 * @author: 紫苏
 * @date: 20-8-4 上午9:57
 * @description:
 */
public class FourElement {

    int id;
    String op;
    String arg1;
    String arg2;
    Object result;

    public FourElement() {
    }

    public FourElement(int id, String op, String arg1, String arg2, Object result) {
        this.id = id;
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }
}
