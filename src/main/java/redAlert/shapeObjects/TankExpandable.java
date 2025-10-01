package redAlert.shapeObjects;

import redAlert.utilBean.CenterPoint;

/**
 * @Author:
 * @Date: 2025-10-01-12:27
 * @Description: 基地车、坦克类等部署后选中再移动可以取消部署
 * （考虑可拓展性，如共和国之辉中中国灰熊坦克可以部署，将此类通用变量放在该接口）
 */
public interface TankExpandable extends Expandable{



    int TANK_STATUS_NORMAL = 0;
    int TANK_STATUS_EXPANDING = 1;

    int TANK_STATUS_UNEXPANDING = 2;


    void unexpandAndTransfer(MovableUnit movableUnit, CenterPoint moveTargetCp);
    MovableUnit getUnexpandUnit();

    Building getExpandBuilding();

    void transferStatus(int newStatus);

    int getExpandStatus();

}
