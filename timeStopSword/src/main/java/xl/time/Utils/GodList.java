package xl.time.Utils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.*;

public class GodList {
    private static final List<Entity> entityMap = new ArrayList<Entity>(){
        @Override
        public void clear() {

        }

        @Override
        public boolean remove(Object o) {
            return false;
        }
    };

    public static boolean isGodList(Object o) {
        if (!(o instanceof Player))
            return false;
        return (entityMap.contains(o));
    }
    public static void SetGod(Player entity) {
        for (int i = 0; i < 5; i++) {
            entityMap.add(entity);
        }


    }



}
