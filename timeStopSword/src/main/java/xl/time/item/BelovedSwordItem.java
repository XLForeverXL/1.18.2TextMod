package xl.time.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.IItemRenderProperties;
import org.jetbrains.annotations.Nullable;
import xl.time.Utils.DeadLists;
import xl.time.Utils.FontRender;
import xl.time.Utils.GodList;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class BelovedSwordItem extends Item {
    public BelovedSwordItem( ) {
        super(new Properties());
    }
    public static Minecraft mc = Minecraft.getInstance();

    public int getUseDuration(ItemStack p_40680_) {
        return 72000;
    }
    public static final ResourceLocation resourceLocation = new ResourceLocation("shaders/post/desaturate.json");
    public UseAnim getUseAnimation(ItemStack p_40678_) {
        return UseAnim.BOW;
    }
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity living, int im) {
        try {
            if (mc.gameRenderer.currentEffect() != null){
                Objects.requireNonNull(mc.gameRenderer.currentEffect()).close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Vec3 vec3 = new Vec3(living.getX(), living.getY(), living.getZ());
        List<Entity> entityList = living.level.getEntitiesOfClass(Entity.class, new AABB(vec3, vec3).inflate(5 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(c -> c.distanceToSqr(vec3))).toList();
        for (Entity entity : entityList){
            if (!(entity instanceof Player)){
                DeadLists.SetDead(entity);
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
          GodList.SetGod((Player) p_41406_);
        super.inventoryTick(p_41404_, p_41405_, p_41406_, p_41407_, p_41408_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        mc.gameRenderer.loadEffect(resourceLocation);
        return super.use(p_41432_, player, hand);
    }

    @Override
    public Component getName(ItemStack p_41458_) {
        return new TextComponent("至爱 の 刃");
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            @Override
            public Font getFont(ItemStack stack) {
                return FontRender.getFontRender();
            }
        });
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> list, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, list, p_41424_);
        list.add(new TextComponent("至爱 の 爱"));
        list.add(new TextComponent("我像秋天里两片落下的树叶，在空中交错片刻"));
        list.add(new TextComponent("东风吹过少年梦，从此再无赤子心"));
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity living) {
        Vec3 vec3 = new Vec3(living.getX(), living.getY(), living.getZ());
        List<Entity> entityList = living.level.getEntitiesOfClass(Entity.class, new AABB(vec3, vec3).inflate(5 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(c -> c.distanceToSqr(vec3))).toList();
        for (Entity entity : entityList){
            if (!(entity instanceof Player)){
                if (entity instanceof LivingEntity living1){
                    living1.deathTime = 20;
                    living1.canUpdate(false);
                    Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors()).schedule(() -> {
                        DeadLists.SetDead(living1);
                        living1.canUpdate(true);
                        living1.onRemovedFromWorld();
                        living1.kill();
                    },500L, TimeUnit.MILLISECONDS);
                }
            }
        }
        return super.onEntitySwing(stack, living);
    }
}
