package xl.time.mixin;

import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Timer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xl.time.Main;
import xl.time.Utils.TimeHelper;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Nullable
    @Shadow
    public Entity cameraEntity;
    @Nullable
    @Shadow
    public Entity crosshairPickEntity;

    @Shadow
    private Timer timer = new Timer(20.0F, 0L);
    @Shadow
    public LocalPlayer player;
    @Nullable
    @Shadow
    public ClientLevel level;
    @Nullable
    @Shadow
    public Screen screen;
    @Shadow @Final public KeyboardHandler keyboardHandler;



    @Inject(method = "runTick", at = @At("HEAD"))
    private void runTick(boolean p_91384_, CallbackInfo ci) {
        if (player != null && timer != null) {
            if (level != null) {
                if (TimeHelper.TimeStop) {
                    Minecraft mc = Minecraft.getInstance();
                    if (player.getInventory().contains(new ItemStack(Main.time.get()))) {
                        if (mc.screen != null) {
                            mc.missTime = 10000;
                        }
                        if (mc.screen != null) {
                            Screen.wrapScreenError(() -> {
                                mc.screen.tick();
                            }, "Ticking screen", mc.screen.getClass().getCanonicalName());
                        }

                        if (!mc.options.renderDebug) {
                            mc.gui.clearCache();
                        }

                        if (mc.overlay == null && (mc.screen == null || mc.screen.passEvents)) {
                            mc.profiler.popPush("Keybindings");
                            mc.handleKeybinds();
                            if (mc.missTime > 0) {
                                --mc.missTime;
                            }
                        }

                        if (mc.level != null) {
                            mc.profiler.popPush("gameRenderer");
                            if (!mc.pause) {
                                mc.gameRenderer.tick();
                            }

                            mc.profiler.popPush("levelRenderer");
                            if (!mc.pause) {
                                mc.levelRenderer.tick();
                            }
                            mc.profiler.popPush("level");
                            if (!mc.pause) {
                                if (mc.level.getSkyFlashTime() > 0) {
                                    mc.level.setSkyFlashTime(mc.level.getSkyFlashTime() - 1);
                                }

                                mc.level.tickEntities();
                            }
                        } else if (mc.gameRenderer.currentEffect() != null) {
                            mc.gameRenderer.shutdownEffect();
                        }

                        /*if (!mc.pause) {
                            mc.musicManager.tick();
                        }
                        mc.soundManager.tick(mc.pause);*/
                        if (mc.level != null) {
                        /*    if (!mc.pause) {
                                if (!mc.options.joinedFirstServer && mc.isMultiplayerServer()) {
                                    Component component = new TranslatableComponent("tutorial.socialInteractions.title");
                                    Component component1 = new TranslatableComponent("tutorial.socialInteractions.description", new Object[]{Tutorial.key("socialInteractions")});
                                    mc.socialInteractionsToast = new TutorialToast(TutorialToast.Icons.SOCIAL_INTERACTIONS, component, component1, true);
                                    mc.tutorial.addTimedToast(mc.socialInteractionsToast, 160);
                                    mc.options.joinedFirstServer = true;
                                    mc.options.save();
                                }

                                mc.tutorial.tick();
                                ForgeEventFactory.onPreWorldTick(mc.level, () -> {
                                    return true;
                                });

                                try {
                                    mc.level.tick(() -> {
                                        return true;
                                    });
                                } catch (Throwable var4) {
                                    CrashReport crashreport = CrashReport.forThrowable(var4, "Exception in world tick");
                                    if (mc.level == null) {
                                        CrashReportCategory crashreportcategory = crashreport.addCategory("Affected level");
                                        crashreportcategory.setDetail("Problem", "Level is null!");
                                    } else {
                                        mc.level.fillReportDetails(crashreport);
                                    }

                                    throw new ReportedException(crashreport);
                                }

                                ForgeEventFactory.onPostWorldTick(mc.level, () -> {
                                    return true;
                                });
                            }*/
                            mc.profiler.popPush("animateTick");
                            if (!mc.pause && mc.level != null) {
                                if (mc.player != null) {
                                    mc.level.animateTick(mc.player.getBlockX(), mc.player.getBlockY(), mc.player.getBlockZ());
                                }
                            }
                       /*    mc.profiler.popPush("particles");
                            if (!mc.pause) {
                                mc.particleEngine.tick();
                            }
                        } else if (mc.pendingConnection != null) {
                            mc.profiler.popPush("pendingConnection");
                            mc.pendingConnection.tick();
                        }*/

                            mc.profiler.popPush("keyboard");
                            mc.keyboardHandler.tick();
                            mc.profiler.pop();
                        }
                    }

                }
            }
        }
    }

}
