package sekwah.mods.narutomod.common;

import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import sekwah.mods.narutomod.blocks.BlockSakuraSapling;
import sekwah.mods.narutomod.blocks.NarutoBlocks;
import sekwah.mods.narutomod.player.extendedproperties.PlayerInfo;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;


public class EventServerHook {

    /**
     * @ForgeSubscribe public void bonemealUsed(LivingFallEvent event) {
     * if(event.distance <= 2F){ // change the value to increase the safe falling distance
     * event.setCanceled(true); // also maybe get some code for water walkint to remove the glitch damage
     * event.setResult(Result.DENY);
     * }
     * else{
     * event.distance -= 2F; // reduce the damage after falling(so its not like an instant death after the long distance)
     * }
     * }
     */

    @SubscribeEvent
    public void bonemealUsed(BonemealEvent event) {

        Side side = FMLCommonHandler.instance().getEffectiveSide();

        //Block block = event.world.getBlock(event.x, event.y, event.z);

        Block block = event.block.getBlock();

        if (side == Side.SERVER) {
            if (block == NarutoBlocks.Sakura_Sapling) {
                ((BlockSakuraSapling) NarutoBlocks.Sakura_Sapling).markOrGrowMarked(event.world, event.x, event.y, event.z, event.world.rand);

                event.setResult(Event.Result.ALLOW);
                event.setCanceled(true);
                return;
            }
        } else if (side == Side.CLIENT) {
            if (block == NarutoBlocks.Sakura_Sapling) {
                for (int j1 = 0; j1 < 15; ++j1) {
                    double d0 = event.world.rand.nextGaussian() * 0.02D;
                    double d1 = event.world.rand.nextGaussian() * 0.02D;
                    double d2 = event.world.rand.nextGaussian() * 0.02D;
                    event.world.spawnParticle("happyVillager", (double) ((float) event.x + event.world.rand.nextFloat()), (double) event.y + (double) event.world.rand.nextFloat() * NarutoBlocks.Sakura_Sapling.getBlockBoundsMaxY(), (double) ((float) event.z + event.world.rand.nextFloat()), d0, d1, d2);
                }
            }
        }
    }

    private static final DataParameter<String> JUTSU_POSE = EntityDataManager.<String>createKey(EntityPlayer.class, DataSerializers.STRING);
    private static final DataParameter<String> CLAN = EntityDataManager.<String>createKey(EntityPlayer.class, DataSerializers.STRING);

    // TODO Try monster attributes or whatever for the max chakra, make it more like maxhealth on player, could solve sending unneeded data
    private static final DataParameter<Float> MAX_CHAKRA = EntityDataManager.<Float>createKey(EntityPlayer.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> CHAKRA = EntityDataManager.<Float>createKey(EntityPlayer.class, DataSerializers.FLOAT);

    private static final DataParameter<String> CURRENT_POSE = EntityDataManager.<String>createKey(EntityPlayer.class, DataSerializers.STRING);


    // TODO make saving system for clans and other stuff
    @SubscribeEvent
    public void handleConstruction(EntityConstructing event) {


        if (event.entity instanceof EntityPlayer) {
            EntityDataManager dm = event.entity.getDataManager();

            dm.register(JUTSU_POSE, "default");
            dm.register(CLAN, "Undefined");

            dm.register(MAX_CHAKRA, 100f);
            dm.register(CHAKRA, 100f);

            dm.register(CURRENT_POSE, "default");

            /*dm.addObject(20, "default"); // jutsu pose id (such as charging)
            dm.addObject(21, "Undefined"); // current clan
            // current player
            dm.addObject(22, 50);
            //dw.addObject(22, 50); // max amount of stamina
            //dw.addObject(23, 50); // amount of chakra
            dm.addObject(24, 0); // amount of kunai in player
            // Float.valueOf(0)
            dm.addObject(25, Float.valueOf(0)); // animationTick (used to add smooth animation for players to different poses, is currently edited by the client :P)
            dm.addObject(26, "default"); // lastpose (so the smooth animation works between poses)
            dm.addObject(27, "default"); // poseClient (the last pose the client updated(so it can change the animationTick back to 0))
            //dw.addObject(27, 0); // could also possibly add a kunai throw tick.*/

            //event.entity.registerExtendedProperties(PlayerInfo.IDENTIFIER, new PlayerInfo((EntityPlayer) event.entity));

            /*Side side = FMLCommonHandler.instance().getEffectiveSide();
            if (side == Side.CLIENT) {
                event.entity.getEntityData().setString("lastposeClient", "default"); // this stores the last pose for the client but only client side
            }*/
        }
        // May be for whenever the player first joins

    }

    // Takes damage after 3
    @SubscribeEvent
    public void livingFall(LivingFallEvent event)
    {
        if (event.entityLiving instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            //NarutoMod.logger.info(event.distance);
            if(event.distance < 9){
                event.distance /= 3;
            }
            if(event.distance > 3){
                event.distance -= 3f;
                event.distance *= 0.7f;
                event.distance += 3f;
            }


        }
    }

    @SubscribeEvent
    public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving.isPotionActive(NarutoEffects.chakraRestore)) {
            if (event.entityLiving.getActivePotionEffect(NarutoEffects.chakraRestore).getDuration() == 0) {
                event.entityLiving.removePotionEffect(NarutoEffects.chakraRestore.id);
            }
            /**else{
             // Could add code to add effects here
             }*/
        }
    }

    @SubscribeEvent
    public void onClonePlayer(PlayerEvent.Clone event) {
        PlayerInfo.get(event.entityPlayer).copyData(PlayerInfo.get(event.original));
    }

    @SubscribeEvent
    public void onJoinWorld(EntityJoinWorldEvent event) {
        // If you have any non-DataWatcher fields in your extended properties that
        // need to be synced to the client, you must send a packet each time the
        // player joins the world; this takes care of dying, changing dimensions, etc.
        if (event.entity instanceof EntityPlayerMP) {
            EntityPlayer player = (EntityPlayer) event.entity;
            PlayerInfo.get(player).reloadDW();
            //PacketDispatcher.sendTo(new SyncPlayerPropsMessage((EntityPlayer) event.entity), (EntityPlayerMP) event.entity);
        }
    }

    @SubscribeEvent
    public void playerPunch(AttackEntityEvent event) {
        //EntityLivingBase attackedEntity = event.entityLiving;
        //event.target.attackEntityFrom(new EntityDamageSource("player", event.entityPlayer), 1);
    }

}
