package xl.time.Utils;

import net.minecraft.world.entity.Entity;

import java.util.*;


public class DeadLists {



    private static final List<Entity> Death = new ArrayList<>(){
        public void clear() {}

        public boolean remove(Object o) {
            return false;
        }
    };


    private static final List<Entity> list = new ArrayList();


    public static boolean isEntity(Object o) {
        return o != null && o instanceof Entity ? (list.contains(o) || Death.contains(o)) : false;
    }



    public static void SetDead(Entity entity) {
        list.add(entity);
        Death.add(entity);
    }

}