package sekwah.mods.narutomod.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum EnumNarutoOptions {
    CHAKRA_BAR_OFFSETY("naruto.gui.chakraGUIOffset", true, false),
    CHAKRA_BAR_OFFSETX("naruto.gui.chakraGUIOffset", true, false),
    CHAKRA_BAR_CORNER("naruto.gui.chakraGUICorner", true, false),
    FIRSTPERSON("naruto.gui.firstPerson", false, true),
    JUTSU_DELAY("naruto.gui.jutsuDelay",true,false);

    private final boolean enumFloat;
    private final boolean enumBoolean;
    private final String enumString;

    private EnumNarutoOptions(String par3Str, boolean par4, boolean par5) {
        this.enumString = par3Str;
        this.enumFloat = par4;
        this.enumBoolean = par5;
    }

    public static EnumNarutoOptions getEnumOptions(int par0) {
        EnumNarutoOptions[] aenumoptions = values();
        int j = aenumoptions.length;

        for (int k = 0; k < j; ++k) {
            EnumNarutoOptions enumoptions = aenumoptions[k];

            if (enumoptions.returnEnumOrdinal() == par0) {
                return enumoptions;
            }
        }

        return null;
    }

    public boolean getEnumFloat() {
        return this.enumFloat;
    }

    public boolean getEnumBoolean() {
        return this.enumBoolean;
    }

    public int returnEnumOrdinal() {
        return this.ordinal();
    }

    public String getEnumString() {
        return this.enumString;
    }
}
