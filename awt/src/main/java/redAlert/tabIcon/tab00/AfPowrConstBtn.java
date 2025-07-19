package redAlert.tabIcon.tab00;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import redAlert.CustomToolTip;
import redAlert.enums.ConstConfig;
import redAlert.militaryBuildings.AfCnst;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.tabIcon.Tab00ConstIcon;

/**
 * 发电厂图标
 */
public class AfPowrConstBtn extends Tab00ConstIcon{

	private static final long serialVersionUID = 1L;
	
	private CustomToolTip toolTip;  
    private ActionListener toolTipBtnListener;  
    
	private BufferedImage img;
	
	public AfPowrConstBtn() {
		super(ConstConfig.AfPowr);
		
		
//		 ToolTipManager toolTipManager = ToolTipManager.sharedInstance(); // 获取ToolTipManager实例
//        toolTipManager.setInitialDelay(500); // 设置显示延迟时间（毫秒）
//        toolTipManager.setDismissDelay(15000); // 设置消失延迟时间（毫秒）
//        toolTipManager.setReshowDelay(1000); // 设置重新显示延迟时间（毫秒）
//        toolTipManager.registerComponent(this); // 注册组件以显示tooltip
    
        
//		this.setToolTipText("<html style=\"padding:0px\">"
//				+ "<div style=\"background:black\">"
//				+ "<span>发电厂<br>$800</span>"
//				+ "</div>"
//				+ "</html>");
		
//		this.setToolTipText("发电厂$800");
		
	}

//	@Override
//	public JToolTip createToolTip() {
//		toolTip = new CustomToolTip();
//		toolTip.setSize(300, 100);
//		return toolTip;
//	}
	
	/**
	 * 发电厂图标的展示条件
	 * 有盟军基地
	 */
	@Override
	public boolean isDisplay() {
		if(ShapeUnitResourceCenter.containsBuildingClass(AfCnst.class)) {
			return true;
		}else {
			return false;
		}
	}
	
}
