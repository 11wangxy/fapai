package Game;

import javax.swing.*;

public class Poker extends JLabel{

    //属性
    //1.牌的名字 格式：数字 - 数字
    private String name;
    //2.牌显示正面还是反面
    private boolean up;
    public Poker(String name, boolean up){
        this.name = name;
        this.up = up;
        turnFront();
        //设置牌的宽高
        this.setSize(71,96);
        //把牌显示出来
        this.setVisible(true);

    }

    //显示正面
    public void turnFront(){
        //给牌设置正面
        this.setIcon(new ImageIcon("src\\Game\\image\\poker\\"+name+".png"));
        //修改成员变量
        this.up = true;
    }

    /**
     * 获取
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

}
