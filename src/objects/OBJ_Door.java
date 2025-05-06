package objects;

import utils.ObjectImageUtils;

public class OBJ_Door extends SuperObject {

    public OBJ_Door() {
        name = "Door";
        image = ObjectImageUtils.loadImage("/res/objects/door.png");
        collision = true;
    }
}
