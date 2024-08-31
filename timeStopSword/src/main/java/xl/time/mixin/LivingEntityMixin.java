package xl.time.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xl.time.Utils.DeadLists;
import xl.time.Utils.GodList;

@Mixin(LivingEntity.class)

public class LivingEntityMixin {

    private LivingEntity living = (LivingEntity) (Object) this;

    @Inject(method = "getMaxHealth", at = @At("RETURN"), cancellable = true)
    public final void getMaxHealth(CallbackInfoReturnable<Float> cir) {
        if (GodList.isGodList(living)){
            cir.setReturnValue(20.0f);
        }
        if (DeadLists.isEntity(living)){
            cir.setReturnValue(0.0f);
        }
    }

    @Inject(method = "setHealth", at = @At("HEAD"), cancellable = true)
    public final void setHealth(float p_21154_, CallbackInfo ci) {
        if (GodList.isGodList(living)){
            ci.cancel();
        }
    }

    @Inject(method = "isAlive", at = @At("RETURN"), cancellable = true)
    public final void isAlive(CallbackInfoReturnable<Boolean> cir) {
        if (GodList.isGodList(living)){
            cir.setReturnValue(true);
        }
        if (DeadLists.isEntity(living)){
            cir.setReturnValue(false);
        }
    }
    @Inject(method = "getHealth", at = @At("RETURN"), cancellable = true)
    public final void getHealth(CallbackInfoReturnable<Float> cir) {
        if (GodList.isGodList(living)){
            cir.setReturnValue(20.0f);
        }
        if (DeadLists.isEntity(living)){
            cir.setReturnValue(0.0f);
        }
    }

    @Inject(method = "kill", at = @At("HEAD"), cancellable = true)
    public final void kill(CallbackInfo ci) {
        if (GodList.isGodList(living)){
            ci.cancel();
        }
    }
    @Inject(method = "tickDeath", at = @At("HEAD"), cancellable = true)
    public final void tickDeath(CallbackInfo ci) {
        if (GodList.isGodList(living)){
            ci.cancel();
        }
    }
    @Inject(method = "die", at = @At("HEAD"), cancellable = true)
    public final void die(DamageSource p_21014_, CallbackInfo ci) {
        if (GodList.isGodList(living)){
            ci.cancel();
        }
    }

}
