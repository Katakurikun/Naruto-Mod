package sekwah.mods.narutomod.blocks;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sekwah.mods.narutomod.NarutoMod;
import sekwah.mods.narutomod.worldgeneration.WorldGenSakuraTrees;
import net.minecraft.block.BlockBush;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;
import sekwah.mods.narutomod.worldgeneration.WorldGenBigSakuraTree;

import java.util.Random;

public class BlockSakuraSapling extends BlockBush implements IGrowable {

    public static final PropertyEnum<BlockPlanks.EnumType> TYPE = PropertyEnum.<BlockPlanks.EnumType>create("type", BlockPlanks.EnumType.class);
    public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);
    protected static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);

    // TODO fix because extending flower is breaking at the moment
    protected BlockSakuraSapling() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, BlockPlanks.EnumType.OAK).withProperty(STAGE, Integer.valueOf(0)));
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    /*@SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return blockIcon;
    }*/

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
        if (!par1World.isRemote) {
            super.updateTick(par1World, par2, par3, par4, par5Random);

            if (par1World.getBlockLightValue(par2, par3 + 1, par4) >= 9 && par5Random.nextInt(7) == 0) {
                this.markOrGrowMarked(par1World, par2, par3, par4, par5Random);
            }
        }
    }

    public void markOrGrowMarked(World par1World, int par2, int par3, int par4, Random par5Random) {
        int l = par1World.getBlockMetadata(par2, par3, par4);

        if ((l & 8) == 0) {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, l | 8, 4);
        } else {
            this.growTree(par1World, par2, par3, par4, par5Random);
        }
    }

    // Make it so it detects when bone marrow is used.

    /**
     * Attempts to grow a sapling into a tree
     */
    public void growTree(World par1World, int par2, int par3, int par4, Random par5Random) {
        if (!TerrainGen.saplingGrowTree(par1World, par5Random, par2, par3, par4)) return;

        int l = par1World.getBlockMetadata(par2, par3, par4) & 3;
        Object object = null;
        int i1 = 0;
        int j1 = 0;
        boolean flag = false;

        if (object == null) {
            j1 = 0;
            i1 = 0;
            object = new WorldGenSakuraTrees(true, 4 + par5Random.nextInt(3), 3, 3, false);

            if (par5Random.nextInt(10) == 0) {
                object = new WorldGenBigSakuraTree(true);
            }
        }

        if (flag) {
            par1World.setBlock(par2 + i1, par3, par4 + j1, Blocks.air, 0, 4);
            par1World.setBlock(par2 + i1 + 1, par3, par4 + j1, Blocks.air, 0, 4);
            par1World.setBlock(par2 + i1, par3, par4 + j1 + 1, Blocks.air, 0, 4);
            par1World.setBlock(par2 + i1 + 1, par3, par4 + j1 + 1, Blocks.air, 0, 4);
        } else {
            par1World.setBlock(par2, par3, par4, Blocks.air, 0, 4);
        }

        if (!((WorldGenerator) object).generate(par1World, par5Random, par2 + i1, par3, par4 + j1)) {
            if (flag) {
                par1World.setBlock(par2 + i1, par3, par4 + j1, this, l, 4);
                par1World.setBlock(par2 + i1 + 1, par3, par4 + j1, this, l, 4);
                par1World.setBlock(par2 + i1, par3, par4 + j1 + 1, this, l, 4);
                par1World.setBlock(par2 + i1 + 1, par3, par4 + j1 + 1, this, l, 4);
            } else {
                par1World.setBlock(par2, par3, par4, this, l, 4);
            }
        }
    }

    /**
     * Determines if the same sapling is present at the given location.
     */
    public boolean isSameSapling(World par1World, int par2, int par3, int par4, int par5) {
        return par1World.getBlock(par2, par3, par4) == this && (par1World.getBlockMetadata(par2, par3, par4) & 3) == par5;
    }

    @SideOnly(Side.CLIENT)
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return CreativeTabs.tabDecorations;
    }

    @SideOnly(Side.CLIENT)

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        this.blockIcon = par1IconRegister.registerIcon(NarutoMod.modid + ":" + "sapling_sakura");
    }
}
