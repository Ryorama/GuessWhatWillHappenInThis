package fr.anatom3000.gwwhit.materials.ryqo;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
public class RyqoArmorMaterial implements ArmorMaterial {
public static final int[] BASE_DURABILITY = new int[] {13, 15, 16, 11};
public static final int[] PROTECTION_VALUES = new int[] {1, 2, 3, 1};

@Override
public int getDurability(EquipmentSlot slot) {
return BASE_DURABILITY[slot.getEntitySlotId()] * 119;
}
@Override
public int getProtectionAmount(EquipmentSlot slot) {
return PROTECTION_VALUES[slot.getEntitySlotId()];
}
@Override
public int getEnchantability() {
return 14;
}
@Override
public Ingredient getRepairIngredient() {
return Ingredient.ofItems(RyqoRoot.RYQO_DUST);
}
@Override
public String getName() {
return "ryqo";
}
@Override
public float getToughness() {
return 2.032326508265414f;
}
@Override
public float getKnockbackResistance() {
return 1.1796678035263695f;
}
@Override
public SoundEvent getEquipSound() {
return SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;
}
}