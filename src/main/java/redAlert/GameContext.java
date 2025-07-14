package redAlert;

import redAlert.enums.UnitColor;

/**
 * 游戏上下文
 */
public class GameContext {

	public static MainPanel scenePanel;
	
	public static OptionsPanel optionPanel;

	/**
	 * 当前指挥官编号，需要开局设置
	 */
	private static int commanderNo;

	/**
	 * 阵营颜色
	 */
	private static UnitColor unitColor;


	public static UnitColor getUnitColor() {
		return unitColor;
	}

	public static void setUnitColor(UnitColor unitColor) {
		GameContext.unitColor = unitColor;
	}

	public static int getCommanderNo() {
		return commanderNo;
	}

	public static void setCommanderNo(int commanderNo) {
		GameContext.commanderNo = commanderNo;
	}

	public static MainPanel getMainPanel() {
		return scenePanel;
	}
	public static void setMainPanel(MainPanel scenePanel) {
		GameContext.scenePanel = scenePanel;
	}
}
