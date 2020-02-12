package com.scut.mywindows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

/**
 * MyWindows
 * 直接调用构造方法 new MyWindows()即可构造并初始化好可视化操作的Windows窗口
 * 需要实现的论文title、author、cooperator搜索功能，在setMyButton()方法中的按钮监听方法中对应位置实现即可
 * 需要实现的论文分析功能作者统计、关键字分析等，在setMyComBox()方法中的事件监听方法中对应位置实现即可
 */

public class MyWindows extends JFrame {
    public static void main(String[] args) {
        new MyWindows();
    }

    Box frame = Box.createVerticalBox();   //窗口盒式布局
    JPanel searchJPanel = new JPanel();    //显示搜索层的容器，包含下拉列表框、文本框、按钮
    JComboBox jComboBox;   //下拉列表框
    JTextField jTextField;   //文本框
    JButton jButton;    //"查询"提交按钮
    LinkedList<String> titleHistory = new LinkedList<String>();    //title搜索记录列表
    LinkedList<String> authorHistory = new LinkedList<String>();   //author搜索记录列表
    LinkedList<String> cooperatorHistory = new LinkedList<String>();   //cooperatot搜索记录列表
    LinkedList<JLabel> jLabelHistory = new LinkedList<JLabel>();   //显示搜索记录的JLabel列表
    JPanel historyJPanel = new JPanel();   //显示历史搜索记录JLabel的中间层容器
    Box showBox = Box.createVerticalBox();    //   显示查询结果的垂直盒式布局容器
    private String hintText = "请输入论文题目";   //文本框默认提示文字


    public MyWindows() {
        setTitle("科学文献管理系统");    //设置显示窗口标题
        setBounds(200, 200, 1200, 800);    //设置窗口显示尺寸
        add(frame);   //将窗口盒式布局添加至窗口

        setMyImage();  //设置页面上方图片
        frame.add(searchJPanel);

        setMyComBox(searchJPanel);   //设置下拉菜单
        setMyTextField(searchJPanel);   //设置单行输入文本框
        setMyButton(searchJPanel);    //设置提交按钮
        initHistoryJLabel();   //初始化历史记录标签
        initShowBox();    //初始化内容展示框

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //设置窗口可关闭
        setVisible(true);    //设置窗口可见
    }

    /**
     * 设置显示结果
     * @param resultList
     */
    public void setShowResult(LinkedList<JPanel> resultList) {
        Box showBox = getShowBox();   //获取显示查询结果的垂直盒式布局容器
        showBox.removeAll();    //清空布局容器原有组件
        //将列表组件依次添加至result容器
        for (int i = 0; i < resultList.size(); i++) {
            showBox.add(resultList.get(i));
        }
    }

    /**
     * 返回显示查询结果的垂直盒式布局容器
     * 可对该容器操作以改变或更新查询结果
     * @return
     */
    public Box getShowBox() {
        return showBox;
    }

    /**
     * 设置页面上方图片
     */
    private void setMyImage() {
        JLabel jLabel = new JLabel(new ImageIcon("src\\com.scut.mywindows\\titleImage.png"));
        JPanel jp = new JPanel();
        jp.add(jLabel);
        frame.add(jp);   //将图片添加至页面上方

    }

    /**
     * 设置下拉菜单以及监听事件
     */
    private void setMyComBox(JPanel jPanel) {
        JPanel jp = new JPanel();
        jComboBox = new JComboBox();    //创建JComboBox
        jComboBox.addItem("论文搜索");    //向下拉列表中添加子项
        jComboBox.addItem("作者搜索");
        jComboBox.addItem("合作关系");
        jComboBox.addItem("作者统计");
        jComboBox.addItem("热点分析");
        jComboBox.addItem("关键字统计");
        jp.add(jComboBox);
        jComboBox.setFont(new Font("楷体", Font.BOLD, 24));    //修改字体样式

        jPanel.add(jp);

        //设置下拉列表监听事件
        jComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = jComboBox.getSelectedIndex();
                switch (index) {
                    case 0:
                        setLabel(titleHistory);
                        hintText = "请输入论文题目";
                        break;
                    case 1:
                        setLabel(authorHistory);
                        hintText = "请输入论文作者";
                        break;
                    case 2:
                        setLabel(cooperatorHistory);
                        hintText = "请输入合作者";
                        break;
                    case 3: {
                        hintText = "请选择查询方式";
                        /**
                         *此处实现作者统计功能，返回写文章最多的前100名作者
                         */
                        break;
                    }
                    case 4: {
                        hintText = "请选择查询方式";
                        /**
                         *此处实现热点分析功能，分析每一年发表的文章中，题目所包含的单词中，出现频率排名前10的关键词。
                         */
                        //break;
                    }
                    case 5: {
                        hintText = "请选择查询方式";
                        /**
                         *此处实现热点分析功能
                         * 作者之间的合作关系可以看成是一个图，每个作者对应一个顶点，任两个作者之间如果存在合作关系，
                         * 则在两个顶点之间建立连边。这个图中的每一个完全子图我们称为一个聚团（所谓完全子图指的是
                         * 该子图的任意顶点都和该子图的其他顶点有连边，完全子图的顶点个数称为该完全子图的阶数），
                         * 请统计整个图中各阶完全子图的个数。
                         */
                        //break;
                    }
                }
                jTextField.setText(hintText);  //设置文本框提示文字
                jTextField.setForeground(Color.GRAY);   //文字设为灰色
            }
        });

    }

    /**
     * 设置文本框
     */
    private void setMyTextField(JPanel jPanel) {
        jTextField = new JTextField(hintText, 40);    //创建包含默认提示的文本框
        jTextField.setFont(new Font("宋体", Font.BOLD, 28));    //修改字体样式);
        jTextField.setForeground(Color.GRAY);  //设置hint字体为灰色

        jPanel.add(jTextField);    //将标签组件添加到内容窗格上

        //设置焦点监听事件
        jTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                //获取焦点时，清空提示内容，字体颜色设为黑色
                String temp = jTextField.getText();
                if (temp.equals(hintText)) {
                    jTextField.setText("");
                    jTextField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                //失去焦点时，没有输入内容，显示提示内容，字体设为灰色
                String temp = jTextField.getText();
                if (temp.equals("")) {
                    jTextField.setForeground(Color.GRAY);
                    jTextField.setText(hintText);
                }

            }
        });
    }

    /**
     * 设置提交按钮
     */
    private void setMyButton(JPanel jPanel) {
        jButton = new JButton("查询");
        jButton.setFont(new Font("黑体", Font.BOLD, 24));    //修改字体样式;
        jPanel.add(jButton);    //将按钮组件添加到内容窗格上

        //设置按钮监听事件
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String temp = jTextField.getText();    //获取文本框输入文字
                int index = jComboBox.getSelectedIndex();   //获取当前选择的下拉列表索引
                if (temp.equals(hintText)) {   //未输入查询内容，提示重新输入
                    JOptionPane.showMessageDialog(jPanel, "请输入需要搜索的内容", "提示", JOptionPane.PLAIN_MESSAGE);
                } else if (index == 0)//执行论文搜索
                {
                    preSearch(titleHistory,temp);   //初始化一些查询前的准备
                    /**
                     *此处实现传入完整的论文的题目，能展示该论文的其他相关信息的功能
                     * (扩展实现)实现部分匹配搜索功能。给定若干个关键字，能快速搜索到题目中包含该关键字的文章信息
                     * 传入类型为String的参数temp
                     */
                } else if (index == 1)//执行作者搜索
                {
                    preSearch(authorHistory,temp);   //初始化一些查询前的准备

                    /**
                     *此处实现传入作者名，能展示该作者发表的所有论文信息的功能
                     * 传入类型为String的参数temp
                     */
                } else if (index == 2)//查询合作关系
                {
                    preSearch(cooperatorHistory,temp);   //初始化一些查询前的准备

                    /**
                     *此处实现传入作者名，能展示于该作者有合作关系的其他所以作者
                     * 传入类型为String的参数temp
                     */
                } else {
                    /**
                     *  此时下拉列表选择的是作者统计、热点分析、关键词统计
                     *  此时不能查询，需提示选择正确的查询方式
                     */
                    JOptionPane.showMessageDialog(jPanel, "请选择正确的查询方式", "提示", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });

    }

    /**
     * 初始化历史记录标签
     */
    private void initHistoryJLabel() {
        historyJPanel = new JPanel();
        for (int i = 0; i < 6; i++) {    //创建6个空标签
            JLabel jLabel = new JLabel("");
            jLabel.setFont(new Font("宋体", Font.BOLD, 16));
            jLabel.addMouseListener(new MouseListener() {    //对标签设置鼠标点击监听器
                @Override
                public void mouseClicked(MouseEvent e) {
                    int index = jComboBox.getSelectedIndex();    //获取当前下拉菜单的所选子项的索引
                    if (jLabel.getText().equals("  清空")) {    //如果点击的是"清空"，则清空历史搜索记录
                        switch (index) {
                            case 0:
                                titleHistory.clear();
                                setLabel(titleHistory);
                                break;
                            case 1:
                                authorHistory.clear();
                                setLabel(authorHistory);
                                break;
                            case 2:
                                cooperatorHistory.clear();
                                setLabel(cooperatorHistory);
                                break;
                        }
                    } else {    //如果点击的是历史记录，则将该记录赋值给文本框，并且同步更新历史记录列表
                        jTextField.setText(jLabel.getText());
                        switch (index) {
                            case 0:
                                addHistory(titleHistory, jLabel.getText());
                                setLabel(titleHistory);
                                break;
                            case 1:
                                addHistory(authorHistory, jLabel.getText());
                                setLabel(authorHistory);
                                break;
                            case 2:
                                addHistory(cooperatorHistory, jLabel.getText());
                                setLabel(cooperatorHistory);
                                break;
                        }
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            jLabelHistory.add(jLabel);
            historyJPanel.add(jLabel);
        }
        frame.add(historyJPanel);    //将历史记录标签容器添加至窗口容器
    }

    /**
     * 更新历史记录标签
     * 此方法在每次更新搜索历史记录的时候都需要调用，以同步更新
     *
     * @param history
     */
    private void setLabel(LinkedList<String> history) {
        for (int i = 0; i < 6; i++) {    //首先清空原有标签的值
            jLabelHistory.get(i).setText("");
        }
        for (int i = 0; i < history.size(); i++) {    //给标签重新赋值
            jLabelHistory.get(i).setText(history.get(i));
            jLabelHistory.get(i).setForeground(Color.BLACK);
        }
        if (history.size() != 0) {    //设置"清空"标签
            jLabelHistory.get(history.size()).setText("  清空");
            jLabelHistory.get(history.size()).setForeground(Color.BLUE);
            //jLabelHistory.get(history.size()).setIcon(new ImageIcon("E:\\大一课程学习\\数据结构\\数据结构大作业\\源代码\\src\\com.scut.mywindows\\deleteHistory.png"));
        }
        // JScrollPane jScrollPane = new JScrollPane();
    }

    /**
     * 添加新历史搜索记录
     * @param history
     * @param temp
     */
    private void addHistory(LinkedList<String> history, String temp) {
        if (history.contains(temp)) {   //如果当前列表中已经存在相同的记录，则先移除列表中已存在记录
            history.remove(temp);
        }
        history.addFirst(temp);   //添加记录值列表末尾
        if (history.size() >= 6) {    //保持列表存储的记录最多不超过5个
            history.removeLast();
        }
    }

    /**
     * 初始化显示查询结果的垂直盒式布局容器
     */
    private void initShowBox() {
        Box horizontalBox = Box.createHorizontalBox();
        horizontalBox.add(Box.createVerticalStrut(535));   //设置左侧高为535的占位框架
//        Box showBox =Box.createVerticalBox();    //   查询内容显示容器
//        for(int i = 0;i<100;i++){
//            showBox.add(new JLabel("test"+i));
//        }
//        showBox.removeAll();
        JPanel jsPanel = new JPanel();
        jsPanel.add(showBox);
        showBox.add(Box.createHorizontalStrut(1100));   //在容器底部添加宽为1100的占位框架
        horizontalBox.add(new JScrollPane(jsPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS));
        horizontalBox.add(Box.createVerticalStrut(535));    //设置右侧高为535的占位框架
        frame.add(horizontalBox);

    }

    /**
     * 查询前的一些准备：更新历史记录标签、设置查询进度条
     */
    private void preSearch(LinkedList<String>history, String temp){
        addHistory(history, temp);   //将搜索记录添加至历史搜索记录列表
        setLabel(history);   //更新历史记录标签
        new Thread(new Runnable() {  //设置查询进度条
            @Override
            public void run() {
                JProgressBar progressBar = showProgressBar();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                progressBar.setVisible(false);
            }
        }).start();
//        LinkedList<JPanel> resultList = new LinkedList<JPanel>();
//        for(int i = 0;i<10;i++){
//            JPanel jPanel = new JPanel();
//            JLabel title = new JLabel("这是查询结果"+i);
//            title.setForeground(Color.blue);
//            title.setFont(new Font("黑体",Font.BOLD,20));
//            jPanel.add(title);
//
//            JLabel desc = new JLabel("这是详细信息"+i+"这是详细信息"+i+"\n这是详细信息"+i+"这是详细信息"+i+"这是详细信息"+
//                    i+"这是详细信息\n"+i+"这是详细信息"+i+"这是详细信息"+i+"这是详细信息\n");
//            jPanel.add(desc);
//            resultList.add(jPanel);
//        }
//        setShowResult(resultList);
    }

    /**
     * 设置查询过程中的进度条
     * @param args
     */
    private JProgressBar showProgressBar() {
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setString("正在查询中");
        progressBar.setStringPainted(true);
        getShowBox().add(progressBar);
        return progressBar;
    }
}
