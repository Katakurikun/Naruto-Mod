package sekwah.mods.narutomod.items;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import sekwah.mods.narutomod.NarutoMod;
import sekwah.mods.narutomod.common.DataWatcherIDs;
import sekwah.mods.narutomod.items.itemmodels.ModelGourd;

public class ItemArmourGourd extends ItemArmor {

    public ItemArmourGourd(ArmorMaterial par2EnumArmorMaterial,
                           int par3, int par4) {
        super(par2EnumArmorMaterial, par3, par4);

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {

        this.itemIcon = par1IconRegister.registerIcon(NarutoMod.modid + ":" + (this.getUnlocalizedName().substring(5)));

    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot,
                                  String type) {
        if (stack.getItem() == NarutoItems.gourd && slot == 1) {
            return NarutoMod.modid + ":textures/armour/sand_gourd.png";
        }
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving,
                                         ItemStack itemStack, int armorSlot) {

        ModelGourd armorModel = null;
        if (itemStack != null) {
            if (itemStack.getItem() instanceof ItemArmourGourd) {
                int type = ((ItemArmor) itemStack.getItem()).armorType;

                if (type == 1) {
                    armorModel = new ModelGourd();
                    if (entityLiving instanceof EntityPlayer) {
                        DataWatcher dw = entityLiving.getDataWatcher();
                        armorModel.animationID = dw.getWatchableObjectString(DataWatcherIDs.jutsuPose);
                        armorModel.animationlastID = dw.getWatchableObjectString(DataWatcherIDs.lastPose);
                        armorModel.animationTick = dw.getWatchableObjectFloat(DataWatcherIDs.animationTick);
                    }
                }
            }
        }

        if (armorModel != null) {
            armorModel.bipedBody.showModel = armorSlot == 1;

            armorModel.isSneak = entityLiving.isSneaking();
            armorModel.isRiding = entityLiving.isRiding();
            armorModel.isChild = entityLiving.isChild();
            armorModel.heldItemRight = entityLiving.getEquipmentInSlot(0) != null ? 1 : 0;
            armorModel.isSprinting = entityLiving.isSprinting();
            if (entityLiving instanceof EntityPlayer) {
                EntityPlayer entityPlayer = (EntityPlayer) entityLiving;
                if (itemStack != null && entityPlayer.getItemInUseCount() > 0) {
                    EnumAction enumaction = itemStack.getItemUseAction();

                    if (enumaction == EnumAction.block) {
                        armorModel.heldItemRight = 3;
                    } else if (enumaction == EnumAction.bow) {
                        armorModel.aimedBow = true;
                    } else if (enumaction == NarutoItems.Throw) {
                        if (FMLClientHandler.instance().getClient().thePlayer == entityPlayer) {
                            armorModel.isClientThrowing = true;
                        } else {
                            armorModel.isThrowing = true;
                        }
                    }
                }
            }
        }

        return armorModel;
    }

}
