package bwie.com.shopcar.Beans;

/**
 * 文 件 名: MyApplication
 * 创 建 人: 谢兴张
 * 创建日期: 2017/10/19
 * 邮   箱:
 * 博   客:
 * 修改时间：
 * 修改备注：
 */

public class BaseInfo {
    protected String Id;
    protected String name;
    protected boolean isChoosed;

    public BaseInfo() {
        super();
    }

    public BaseInfo(String id, String name) {
        super();
        Id = id;
        this.name = name;

    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean isChoosed) {
        this.isChoosed = isChoosed;
    }
}
