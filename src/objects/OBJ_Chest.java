package objects;

import utils.ObjectImageUtils;

public class OBJ_Chest extends SuperObject {
    public OBJ_Chest() {
        name = "Chest";
        image = ObjectImageUtils.loadImage("/res/objects/chest.png");
    }
}
