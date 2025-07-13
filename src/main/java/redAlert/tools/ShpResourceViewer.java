package redAlert.tools;

import redAlert.ShapeUnitFrame;
import redAlert.resourceCenter.ShpResourceCenter;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author zeromi
 * @date 2025/7/12
 */
public class ShpResourceViewer extends JFrame {
    private JList<String> imageList;
    private JLabel imageView;
    private ImageIcon originalIcon;
    private double scale = 1.0; // 初始缩放比例
    private final String imagePath = "H:\\mytemp"; // 替换为你的图片路径

    // shp文件解析图集合
    private List<ShapeUnitFrame> shapeUnitFrames;

    public ShpResourceViewer() {
        setTitle("Image List Viewer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建并设置图像列表模型
        DefaultListModel<String> listModel = new DefaultListModel<>();
        this.loadLeftItemList(listModel);

        imageList = new JList<>(listModel);
        imageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        imageList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedImage();
            }
        });

        JScrollPane leftPane = new JScrollPane(imageList);
        leftPane.setPreferredSize(new Dimension(200, 600));

        imageView = new JLabel();
        imageView.setHorizontalAlignment(JLabel.CENTER);
        imageView.setVerticalAlignment(JLabel.CENTER);

        JButton zoomInButton = new JButton("+");
        zoomInButton.addActionListener(e -> zoom(true));

        JButton zoomOutButton = new JButton("-");
        zoomOutButton.addActionListener(e -> zoom(false));

        JPanel controlPanel = new JPanel();
        controlPanel.add(zoomInButton);
        controlPanel.add(zoomOutButton);

        JScrollPane rightPane = new JScrollPane(imageView);
        rightPane.setRowHeaderView(controlPanel); // 将控制面板放在滚动面板的左侧

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPane, rightPane);
        add(splitPane);

        setVisible(true);
    }

    private void zoom(boolean zoomIn) {
        if (zoomIn) {
            scale *= 1.2; // 放大
        } else {
            scale /= 1.2; // 缩小
        }
        scaleImage(scale);
    }

    private void scaleImage(double scale) {
        if (originalIcon != null) {
            Image scaledImage = originalIcon.getImage().getScaledInstance(
                    (int) (originalIcon.getIconWidth() * scale),
                    (int) (originalIcon.getIconHeight() * scale),
                    Image.SCALE_SMOOTH);
            imageView.setIcon(new ImageIcon(scaledImage));
        }
    }

    private void showSelectedImage() {
        String selectedImage = imageList.getSelectedValue();
        if (selectedImage != null) {
            ShapeUnitFrame shapeUnitFrame = shapeUnitFrames.get(Integer.valueOf(selectedImage));
//            originalIcon = new ImageIcon(imagePath + File.separator + selectedImage);
            originalIcon = new ImageIcon(shapeUnitFrame.getImg());
            scaleImage(scale);
        }
    }

    /**
     * 加载左面板列表项
     */
    private void loadLeftItemList(DefaultListModel<String> listModel){
        // 读取文件夹文件名
//        File folder = new File(imagePath);
//        File[] listOfFiles = folder.listFiles();
//        if (listOfFiles != null) {
//            for (File file : listOfFiles) {
//                if (file.isFile()) {
//                    listModel.addElement(file.getName());
//                }
//            }
//        }
        shapeUnitFrames = ShpResourceCenter.loadShpResource("mouse", "mousepal", false);
        for(int i=0;i<shapeUnitFrames.size();i++){
//            int index = 18+(int) MainPanel.frameCount/4%13;//除以4目的在于控制帧率
//            ShapeUnitFrame suf = shapeUnitFrames.get(index);
            listModel.addElement(i+"");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ShpResourceViewer::new);
    }
}