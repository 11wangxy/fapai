package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GameJFrame extends JFrame implements ActionListener {
    //获取界面中的隐藏容器，现在统一获取了，后面直接用就可以了
    public static Container container = null;
    //集合嵌套集合
    //大集合中有三个小集合
    //小集合中装着每一个玩家当前要出的牌
    //0索引：左边的电脑玩家
    //1索引：中间的自己
    //2索引：右边的电脑玩家
    ArrayList<ArrayList<Poker>> playerList = new ArrayList<>();
    //底牌
    ArrayList<Poker> lordList = new ArrayList<>();
    //牌盒，装所有的牌
    ArrayList<Poker> pokerList = new ArrayList();
    public GameJFrame() throws Exception{
        //设置界面
        initJframe();
        //界面显示出来
        //先展示界面再发牌，因为发牌里面有动画，界面不展示出来，动画无法展示
        this.setVisible(true);
        //初始化牌
        //准备牌，洗牌，发牌，排序
        initCard();

    }
    //初始化牌（准备牌，洗牌，发牌，排序）
    public void initCard() {
        //准备牌
        //把所有的牌，包括大小王都添加到牌盒pokerList当中
        for (int i = 1; i <= 5; i++) {//花色
            for (int j = 1; j <= 13; j++) {
                if ((i == 5) && (j > 2)) {
                    break;
                } else {
                    Poker poker = new Poker(i + "-" + j, false);
                    poker.setLocation(350, 150);
                    pokerList.add(poker);
                    container.add(poker);
                }
            }
        }
        //洗牌
        Collections.shuffle(pokerList);

        //创建三个集合用来装三个玩家的牌，并把三个小集合放到大集合中方便管理
        ArrayList<Poker> player0 = new ArrayList<>();
        ArrayList<Poker> player1 = new ArrayList<>();
        ArrayList<Poker> player2 = new ArrayList<>();

        //发牌
        for (int i = 0; i < pokerList.size(); i++) {
            //获取当前遍历的牌
            Poker poker = pokerList.get(i);

            //发三张底牌
            if (i <= 2) {
                //把底牌添加到集合中
                lordList.add(poker);
                Common.move(poker, poker.getLocation(), new Point(270 + (75 * i), 10));
                continue;
            }

            //给三个玩家发牌
            if (i % 3 == 0) {
                //给左边的电脑发牌
                Common.move(poker, poker.getLocation(), new Point(50, 60 + i * 5));
                player0.add(poker);
            } else if (i % 3 == 1) {
                //给中间的自己发牌
                Common. move(poker, poker.getLocation(), new Point(180 + i * 7, 450));
                player1.add(poker);
                //把自己的牌展示正面
                poker.turnFront();
            } else if (i % 3 == 2) {
                //给右边的电脑发牌
                Common. move(poker, poker.getLocation(), new Point(700, 60 + i * 5));
                player2.add(poker);
            }

            //把三个装着牌的小集合放到大集合中方便管理
            playerList.add(player0);
            playerList.add(player1);
            playerList.add(player2);

            //把当前的牌至于最顶端，这样就会有牌依次错开且叠起来的效果这段代码的作用是将一个名为poker的组件放置在容器的第0层，也就是最底层。
            //在容器中存在多个组件时，通过设置组件的Z轴顺序可以控制它们的显示顺序。如果一个组件的Z轴顺序较高，则它会覆盖在其他组件之上。
            //在这里，将poker组件的Z轴顺序设置为0，确保它在容器中最底部。
            container.setComponentZOrder(poker, 0);
        }

        //排序
        //其中this代表当前对象，playerList.get(i)代表获取列表playerList中索引为i的元素，i则代表当前循环的索引值。
        for (int i = 0; i < 3; i++) {
            order(playerList.get(i));
            Common.rePosition(this,playerList.get(i),i);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
    }
    //设置界面
    public void initJframe() {
        //设置标题
        this.setTitle("发牌");
        //设置大小
        this.setSize(830, 620);
        //设置关闭模式
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //设置窗口无法进行调节
        this.setResizable(false);
        //界面居中
        this.setLocationRelativeTo(null);
        //获取界面中的隐藏容器，以后直接用无需再次调用方法获取了
        container = this.getContentPane();
        //取消内部默认的居中放置
        container.setLayout(null);
        //设置背景颜色
        container.setBackground(Color.LIGHT_GRAY);
    }

    public void order(ArrayList<Poker> list) {
        //此处可以改为lambda表达式
        Collections.sort(list, new Comparator<Poker>() {
            @Override
            public int compare(Poker o1, Poker o2) {
                //获取o1的花色和价值
                int color1 = Integer.parseInt(o1.getName().substring(0, 1));
                int value1 = getValue(o1);

                //获取o2的花色和价值
                int color2 = Integer.parseInt(o2.getName().substring(0, 1));
                int value2 = getValue(o2);

                //倒序排列
                //细节：
                //图形化界面当中，牌倒着摆放
                int flag = value2 - value1;

                //如果牌的价值一样，则按照花色排序
                if (flag == 0){
                    return color2 - color1;
                }else {
                    return flag;
                }
            }
        });
    }

    //获取每一张牌的价值
    public int getValue(Poker poker) {
        //获取牌的名字 1-1
        String name = poker.getName();
        //获取牌的花色
        int color = Integer.parseInt(poker.getName().substring(0, 1));
        //获取牌对应的数字,同时也是牌的价值
        int value = Integer.parseInt(name.substring(2));

        //在本地文件中，每张牌的文件名为：数字1-数字2
        //数字1表示花色，数字2表示牌的数字
        //其中3~K对应的数字2，可以视为牌的价值
        //所以，我们单独判断大小王，A，2即可

        //计算大小王牌的价值
        if (color == 5){
            //小王的初始价值为1，在1的基础上加100，小王价值为：101
            //大王的初始价值为2，在2的基础上加100，大王价值为：102
            return value += 100;
        }

        //计算A的价值
        if (value == 1){
            //A的初始价值为1，在1的基础上加20，大王价值为：21
            return value += 20;
        }

        //计算2的价值
        if (value == 2){
            //2的初始价值为2，在2的基础上加30，大王价值为：32
            return value += 30;
        }
        //如果不是大小王，不是A，不是2，牌的价值就是牌对应的数字
        return value;
    }
}